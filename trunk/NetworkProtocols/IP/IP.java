package NetworkProtocols.IP;

import java.util.List;

import Exceptions.*;
import Interface.*;
import Utils.*;
import NetworkProtocols.*;
import NetworkProtocols.IP.Address.*;
import NetworkProtocols.IP.RoutingTable.*;
import NetworkProtocols.ICMP.*;

// IP se comunica con el nivel inferior, a traves de una cola de datagrams recibidos (buffRem),
// y a con el nivel superior, a través de una cola que en principio tiene informacion
// a ser encapsulada en datagrama para ser enviada (buffLoc).
// El thread (reader) de IP, lee en orden de estas dos colas, y cuando encuentra algun requerimiento
// (p.ej. un datagram que llego o un requerimiento de enviar algo por parte de UDP etc), lo procesa
// y recien despues continua con otro requerimiento. Es decir, la entrada al IP esta sincronizada
// con lo que viene por l alinea y lo que le entregan las aplicaciones, pero el proceso que de 
// todo esto hace IP, se realiza aca, a ritmo del IP
// Mas adelante, desde el nivel superior se podrá recibir otro tipo de info, por ejemplo
// la que proviene de opciones de configuracion, etc.
// De igual manera, las interfaces podran anunciar situaciones anormales (ver si todo esto
// va directamenet a IP o queda reflejado en los flags.
// Las clases que representan al nivel superior a IP, tendran mettodos para que les sea pasada
// la info (frames recibidos o info de control)

public class IP implements ProtocolInterface {
	static Queue buffRem;// Buffer de entrada remota a IP, lo llenan las
							// interfaces
	static Queue buffLoc;// Buffer de entrada local a IP, lo llenan las apps o
							// ICMP
	public RoutingTable rTable; // Tabla de ruteo del nodo
	Interfaces ifaces; // Interfaces de red
	Reader rdr;// Thread de lectura de events del nivel superior e inferior
	ICMP icmp;
	IpAddress localAddress = null;
	private DatagramPool datagramPool = new DatagramPool();
	
	public IP(Interfaces ifs) throws NodeException {
		buffRem = new Queue(); // Instanciacion del buffer de recepcion de
								// eventos remoto
		buffLoc = new Queue(); // Instanciacion del buffer de recepcion de
								// eventos locales
		// rTable = RT;
		rTable = new RoutingTable(); // Instanciacion de la tabla de ruteo
		rdr = new Reader(this);// Instanciacion del thread de lectura de eventos
		ifaces = ifs;// Interfaces de red del router
		@SuppressWarnings("unused")
		NetworkProtocols Nt = new NetworkProtocols(); // Elementos comunes a los protocolos de nivel de red
		NetworkProtocols.addProtocol(NetworkProtocols.PROTO_IP, this);
		icmp = new ICMP();
	}

	// Establecer direccion local
	public void setLocalIpAddress(IpAddress loc) {
		this.localAddress = loc;
	}

	// Obtener direccion local
	public IpAddress getLocalIpAddress() {
		return this.localAddress;
	}

	// Metodo invocado por la interfaz para agregar un evento a la cola remota
	public void addRem(eventoN3 x) {
		buffRem.pushBack(x);
	}

	// Metodo invocado por el nivel superior para agregar un evento a la cola
	// local
	public void addLoc(eventoN3 x) {
		buffLoc.pushBack(x);
	}

	// Procesa un datagram remoto o alguan indicacion del nivel inferior. Esto
	// esta en buffRem
	// El objeto contiene un datagram, y podria contener ademas otras cosas que
	// seran definidas
	// mas adelante (por ejemplo, primitivas indication con las cuales la
	// interfaz le informe que
	// esta caida, etc
	public void receive_rem(eventoN3 indN2) {
		System.out.println("Encontro requerimiento en cola remota ip tipo "
				+ indN2.getControl());
		// Segun la primitiva recibida, toma la accion que corresponda
		int ici = indN2.getControl();
		byte[] bb;
		bb = (byte[]) indN2.getInfo();
		switch (ici) {
		case eventoN3.INFO_RECEIVED: // Recepcion de un frame link layer con
										// indicacion de
			// payload = protocolo IP
			// Determina longitud del arreglo de bytes recibido, longitud total
			// del IP y longitud
			// del header IP, luego hace los cheqieos para determinar si el
			// dgram es correcto
			Datagram dd = new Datagram(bb);
			int longrec = bb.length;
			System.out.println("DATAGRAM recibido por IP long total recibida ("
					+ longrec + " ) " + dd.toString());
			// 1-Longiud de header menor que 20 o version distinta de 4 o long
			// recibida es menor que long del datagram
			// o falla checksum, descartar silenciosamente (solo podria
			// registrarse en estadisticas)
			// dd.genChecksum();
			if ((dd.getHeaderLength() < 5) || (dd.getVersion() != 4)
					|| (longrec < dd.getTotalLength())
					|| (longrec < dd.getHeaderLength() * 4)
					|| (dd.verifyChecksum() == false)) {
				System.out
						.println("Error al chequear datagram, se descarta datagram silenciosamente");
				break;
			}
			// Si el header IP contiene opciones, procesarlas. El proceso agota
			// las consecuencias de las
			// opciones (envio de icmp, etc). Si hay errores que impiden que se
			// procese el paquete, el
			// proceso de opciones retorna false.
			if (dd.getHeaderLength() > 5) {
				if (proc_opts(dd) == false)
					break;
			}
			// Proceso del datagram
			// 1- Si no hay entrada en la tabla de ruteo, generar error (ICMP al
			// origen)
			// 2- Si hay entrada en tabla de ruteo,
			// 2.1- Si el envio es indirecto, nexthop es el de la tabla de ruteo
			// 2.2- Si envio es directo:
			// 2.2.1- si la direccion de destino del datagram NO es de este
			// equipo, nexthop
			// es la dir de destino del datagram
			// 2.2.2- si la direccion de destino del datagram es local, lo pasa
			// a nivel
			// superior (local_delivery)
			IpAddress nxthop;
			RoutingEntry re = rTable.getNextHop(dd.getDestAddress());
			if (re == null) {
				// Solicitar a ICMP el envio de aviso de error
				// Teoricamente este send tendria que ser un
				// icmp.addLoc(eventoN3);
				// icmp.send(ICMP.DESTINATION_UNREACHABLE,
				// ICMP.SOURCE_ROUTE_FAILED, dd.getSourceAddr(), dd);
				ICMPPacketSend pack = new ICMPPacketSend(
						ICMP.DESTINATION_UNREACHABLE, ICMP.SOURCE_ROUTE_FAILED,
						dd.getSourceAddress(), dd);
				icmp.addLoc(new eventoN3(eventoN3.SEND, pack));
				break;
			}
			if (re.getType()) {
				// ruteo directo, puede ser para este equipo o para otro
				if (re.getInterface().getIPAddress().equals(dd.getDestAddress())) {
					// envío local, detiene el datagram hasta recibir todos los
					// fragmentos (si es fragmento)
					// Mensajes ICMP posibles: Tiempo superado (Tiempo de
					// reensamblaje de fragmentos superado)
					// una vez que se tiene el datagram completo, se pasa al
					// nivel superior
					
					Datagram assembled = datagramPool .addDatagram(dd);
					
					System.out.println("Envio local para IP: "
							+ dd.getDestAddress().toString());
					// local_delivery(dd)
					if (assembled != null && dd.getProtocol() == 1) { // Si el protocolo es 1
													// entonces es un paquete
													// ICMP
						System.out.println("Recepcion de un mensaje ICMP.");
						icmp.addRem(indN2); // .handle(indN2);
						return;
					}
				} else {
					// envio al host de destino del datagram, chequea TTL,
					// fragmenta si es necesario ,
					// chequea condiciones de error
					// Mensajes ICMP posibles: Destino inaccesible (Se
					// necesitaba fragmentacion), Tiempo superado (TTL superado)

					dd.decrementTtl();
					if (dd.getTtl() == 0) {
						System.out
								.println("Tiempo de vida superado. Se envia mensaje de aviso ICMP al emisor. Datagram: "
										+ dd.toString());
						// Teoricamente este send tendria que ser un
						// icmp.addLoc(eventoN3);
						// icmp.send(ICMP.TIME_EXCEEDED,
						// ICMP.TTL_COUNT_EXCEEDED_TRANSMISION, dd.getSourceAddr(),
						// dd);
						ICMPPacketSend pack = new ICMPPacketSend(
								ICMP.TIME_EXCEEDED,
								ICMP.TTL_COUNT_EXCEEDED_TRANSMISION, dd
										.getSourceAddress(), dd);
						icmp.addLoc(new eventoN3(eventoN3.SEND, pack));
						return;
					}
					
					List<Datagram> fragments = FragAssembler.fragmentar(dd, re.getInterface().getMTU());
					nxthop = re.getNextHop();
					
					for(Datagram fragment: fragments) {
						re.getInterface().send(NetworkProtocols.PROTO_IP, fragment.toByte());

						System.out.println("Envio directo para IP: "
								+ fragment.getDestAddress().toString());
					}
				}
			} else {
				// ruteo indirecto, nexthop el de la entrada de ruteo,
				// decrementa TTL, fragmenta si es
				// necesario chequea condiciones de error
				// Si al decrementar el TTL este se hace cero se debe solicitar
				// a ICMP el envio de aviso de error
				dd.decrementTtl();
				if (dd.getTtl() == 0) {
					System.out
							.println("Tiempo de vida superado. Se envia mensaje de aviso ICMP al emisor. Datagram: "
									+ dd.toString());
					// Teoricamente este send tendria que ser un
					// icmp.addLoc(eventoN3);
					// icmp.send(ICMP.TIME_EXCEEDED,
					// ICMP.TTL_COUNT_EXCEEDED_TRANSMISION, dd.getSourceAddr(),
					// dd);
					ICMPPacketSend pack = new ICMPPacketSend(
							ICMP.TIME_EXCEEDED,
							ICMP.TTL_COUNT_EXCEEDED_TRANSMISION, dd
									.getSourceAddress(), dd);
					icmp.addLoc(new eventoN3(eventoN3.SEND, pack));
					return;
				}
				
				List<Datagram> fragments = FragAssembler.fragmentar(dd, re.getInterface().getMTU());
				nxthop = re.getNextHop();
				
				for(Datagram fragment: fragments) {
					re.getInterface().send(NetworkProtocols.PROTO_IP, fragment.toByte());
					
					System.out.println("Envio indirecto para IP: "
							+ fragment.getDestAddress().toString() + " por router "
							+ nxthop.toString());
				}
				
			}
			break;
		default:
			break;
		}
	}

	// Procesa un requerimiento de envio u otro del nivel superior. Recibe en un
	// byte la info de
	// control de l ainterfa, y un objeto conteniendo la idu
	public void receive_loc(eventoN3 idu) {
		System.out.println("Encontro requerimiento en cola local ip");
		// Segun la primitiva recibida, toma la accion que corresponda
		int ici = idu.getControl();
		// Ver que es lo que hace esto porq no hace nada...
		byte[] datagrambytes;
		if (idu.getInfo().getClass() == Datagram.class)
			datagrambytes = ((Datagram) idu.getInfo()).toByte();
		else
			datagrambytes = (byte[]) idu.getInfo();
		switch (ici) {
		case eventoN3.SEND: // Recibe info para enviar
			// aca se debria ver el nexthop, la interfaz, mtu, fragmentar, etc
			Datagram dd = new Datagram(datagrambytes);
			IpAddress nxthop;
			RoutingEntry re = rTable.getNextHop(dd.getDestAddress());
			System.out.println("Requerimiento local destino a " + dd.getDestAddress().toString());

			if (re == null) {
				// Solicitar a ICMP el envio de aviso de error
				// Teoricamente este send tendria que ser un
				// icmp.addLoc(eventoN3);
				// icmp.send(ICMP.DESTINATION_UNREACHABLE,
				// ICMP.SOURCE_ROUTE_FAILED, dd.getSourceAddr(), dd);
				if (!dd.getSourceAddress().toString().equals(this.localAddress.toString())){
					ICMPPacketSend pack = new ICMPPacketSend(
							ICMP.DESTINATION_UNREACHABLE, ICMP.SOURCE_ROUTE_FAILED,
							dd.getSourceAddress(), dd);
					icmp.addLoc(new eventoN3(eventoN3.SEND, pack));
				}
				else{
					String msg = new String("Type: " + ICMP.DESTINATION_UNREACHABLE + " ");
					msg = msg.concat("DESTINATION_UNREACHABLE");
					msg = msg.concat("  -  Codigo: " + ICMP.SOURCE_ROUTE_FAILED);
					System.out.println(msg);
				}
				break;
			}
			if (re.getType()) {
				// ruteo directo, puede ser para este equipo o para otro

				// envio al host de destino del datagram, chequea TTL,
				// fragmenta si es necesario ,
				// chequea condiciones de error
				// Mensajes ICMP posibles: Destino inaccesible (Se
				// necesitaba fragmentacion), Tiempo superado (TTL superado)

				List<Datagram> fragments = FragAssembler.fragmentar(dd, re.getInterface().getMTU());
				nxthop = re.getNextHop();

				for(Datagram fragment: fragments) {
					re.getInterface().send(NetworkProtocols.PROTO_IP, fragment.toByte());

					System.out.println("Envio directo para IP: "
							+ fragment.getDestAddress().toString());
				}
			} else {
				// ruteo indirecto, nexthop el de la entrada de ruteo,
				// decrementa TTL, fragmenta si es
				// necesario chequea condiciones de error
				// Si al decrementar el TTL este se hace cero se debe solicitar
				// a ICMP el envio de aviso de error

				List<Datagram> fragments = FragAssembler.fragmentar(dd, re.getInterface().getMTU());
				nxthop = re.getNextHop();

				for(Datagram fragment: fragments) {
					re.getInterface().send(NetworkProtocols.PROTO_IP, fragment.toByte());

					System.out.println("Envio indirecto para IP: "
							+ fragment.getDestAddress().toString() + " por router "
							+ nxthop.toString());
				}

			}
			break;

		default:
			break;
		}
	}

	public ICMP getICMPProtocol(){
		return icmp;
	}
	


	// Agregado de una entrada a la tabla de ruteo. Este metodo se invocaria
	// cuando se coloca el
	// evento correspondiente en la cola de eventos locales, y el thread de
	// lectura de eventos
	// lo detecta y hace el correpondiente llamado a este metodo. Por ejemplo,
	// se daria cuando el
	// administrador de red agrega manualmente entradas en la tabla de ruteo o
	// cuando lo hace un
	// protocolo de ruteo como RIP.
	// Por el momento, solo se agrega la entrada, mas adelante, antes del
	// agregado se deberian
	// realizar chequeos de consistencia
	public void addRoute(IpAddress dstNet, Mask mask, boolean routeType,
			IpAddress nextHop, Interface ifc) {
		rTable.addEntry(dstNet, mask, routeType, nextHop, ifc);
	}

	public void rt() {
		rTable.toString();
	}

	public void addDefault(IpAddress nextHop, Interface ifc)
			throws NodeException {
		rTable.addDefault(nextHop, ifc);
	}

	public void delRoutingEntry(IpAddress dstNet, Mask mask) {
		rTable.delRoute(dstNet, mask);
	}

	class Reader extends Thread {
		IP miIp; // Instancia IP
		Queue buffInp; // Buffer de entrada desde equipos remotos
		eventoN3 inforem;// info remota recibida(buffInp), un datagram mas otras
							// cosas
		eventoN3 infoloc;// info local recibida por IP

		public Reader(IP Ip) {
			miIp = Ip;
			this.start();
		}

		// Si hay info en alguna de las colas, procesa el paquete recibido o el
		// requerimiento de
		// nivel superior. Chequea las dos colas (remota y local) en orden, y
		// para el chequeo hasta
		// procesar el requerimiento, de manera de tener su propio sincronismo,
		// independiente
		// de la linea o de los req del nivel superior
		public void run() {
			while (true) {
				// Si hay info en la cola de entrada remota, nivel inferior,
				// procesa
				// Esto hay q cambiarlo y recibir algo mas general que sea una
				// primitiva, y
				// despues discriminar si es p.ej un request o un response, etc
				if ((inforem = (eventoN3) buffRem.peekpopBack()) != null) {
					// Obtiene la info de control de la interfaz y el objeto
					// remoto
					miIp.receive_rem(inforem);
				}
				// Si hay info en la cola de entrada local, nivel superior,
				// procesa
				if ((infoloc = (eventoN3) buffLoc.peekpopBack()) != null) {
					// recibe req de n4
					miIp.receive_loc(infoloc);
				}
			}
		}
		
		// Muestar variables
		public void show() {
			//
		}
	}

	// Procesa las opciones del datagram, incluye envio de icmps, etc. Devuelve
	// false si hay
	// errores que impidan el proceso del datagram, y true en caso contrario.
	public boolean proc_opts(Datagram dd) {
		return true;
	}

}
