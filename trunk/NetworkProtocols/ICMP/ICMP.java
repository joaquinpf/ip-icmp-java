package NetworkProtocols.ICMP;

import Exceptions.*;
import Interface.*;
import Utils.*;
import NetworkProtocols.*;
import NetworkProtocols.IP.*;
import NetworkProtocols.IP.Address.*;

public class ICMP implements ProtocolInterface, ICMPInterface {
	static Queue buffRem; // Buffer de entrada remota a ICMP, lo llenan las
							// interfaces
	static Queue buffLoc; // Buffer de entrada local a ICMP, lo llenan las apps
							// o ICMP
	private IP ip;
	private Reader rdr;
	
//	public ICMP(Interfaces ifs, NetworkProtocols nt) throws NodeException {
//	}

	// Metodo invocado por IP agregar un evento a la cola remota
	public void addRem(eventoN3 x) {
		buffRem.pushBack(x);
	}

	// Metodo invocado por el nivel superior o IP para agregar un evento a la cola
	// local
	public void addLoc(eventoN3 x) {
		buffLoc.pushBack(x);
	}

	// //////////////////////////////////////////////////////////////

	public ICMP(){//IP ip) {
		NetworkProtocols.addProtocol(NetworkProtocols.PROTO_ICMP, this);
		this.ip = (IP) NetworkProtocols.getProtocol(NetworkProtocols.PROTO_IP);
		this.rdr = new Reader(this);
	}

	public boolean handle(eventoN3 p) { // Es invocado cuando IP recibe un
										// datagram que corresponde a ICMP
		ICMPMessage icmpmsg = null;
		try {
			icmpmsg = new ICMPMessage(p);
		} catch (MalformedPacketException e) {
			return false; // El paquete recibido estaba mal formado o no era un
							// paquete ICMP
		}
		System.out.println("ICMP  " + icmpmsg.toString());
		switch (icmpmsg.type) {
			case ICMPMessage.ECHO_REQUEST: {
				System.out
						.println("Se encontro un paquete ICMP ECHO REQUEST proveniente de "
								+ icmpmsg.getSourceAdrr());
				System.out.println("Paquete ICMP: " + icmpmsg.toString());
				ICMPMessage rp = icmpmsg.reply();
				System.out.println("REP " + rp.toString());
				try {
					Datagram datagram = new Datagram((byte[]) p.getInfo());
					datagram.setsourceAddress(datagram.getDestAddress());
					datagram.setDestAddress(icmpmsg.getSourceAdrr());
					datagram.setData(rp.toByteArray());
					// El resto de los campos teoricamente los completa IP antes de
					// mandar el datagram.
					eventoN3 evReply = new eventoN3(eventoN3.SEND, datagram);
					ip.addLoc(evReply);
				} catch (Exception e) {
					System.out
							.println("Error en la generacion y envio de ECHO REPLY. Posible ruta no encontrada.");
				}
			}
				;
				break;
	
			case ICMPMessage.ECHO_REPLY: {
				System.out
						.println("Se encontro un paquete ICMP ECHO REPLY proveniente de "
								+ icmpmsg.getSourceAdrr());
				System.out.println("Paquete ICMP: " + icmpmsg.toString());
			}
				;
				break;
	
			case ICMPMessage.DESTINATION_UNREACHABLE: {
				System.out
						.println("Se encontro un paquete ICMP DESTINATION UNREACHABLE proveniente de "
								+ icmpmsg.getSourceAdrr());
				System.out.println("Paquete ICMP: " + icmpmsg.toString());
	
			}
				;
				break;
	
			case ICMPMessage.SOURCE_QUENCH: {
				System.out
						.println("Se encontro un paquete ICMP SOURCE QUENCH proveniente de "
								+ icmpmsg.getSourceAdrr());
				System.out.println("Paquete ICMP: " + icmpmsg.toString());
	
			}
				;
				break;
	
			case ICMPMessage.REDIRECT: {
				System.out
						.println("Se encontro un paquete ICMP REDIRECT proveniente de "
								+ icmpmsg.getSourceAdrr());
				System.out.println("Paquete ICMP: " + icmpmsg.toString());
	
			}
				;
				break;
	
			case ICMPMessage.ROUTER_ADVERT: {
				System.out
						.println("Se encontro un paquete ICMP ROUTER ADVERT proveniente de "
								+ icmpmsg.getSourceAdrr());
				System.out.println("Paquete ICMP: " + icmpmsg.toString());
	
			}
				;
				break;
	
			case ICMPMessage.ROUTER_SOLICIT: {
				System.out
						.println("Se encontro un paquete ICMP ROUTER SOLICIT proveniente de "
								+ icmpmsg.getSourceAdrr());
				System.out.println("Paquete ICMP: " + icmpmsg.toString());
	
			}
				;
				break;
	
			case ICMPMessage.TIME_EXCEEDED: {
				System.out
						.println("Se encontro un paquete ICMP TIME EXCEEDED proveniente de "
								+ icmpmsg.getSourceAdrr());
				System.out.println("Paquete ICMP: " + icmpmsg.toString());
	
			}
				;
				break;
	
			case ICMPMessage.PARAMETER_PROBLEM: {
				System.out
						.println("Se encontro un paquete ICMP PARAMETER PROBLEM proveniente de "
								+ icmpmsg.getSourceAdrr());
				System.out.println("Paquete ICMP: " + icmpmsg.toString());
	
			}
				;
				break;
	
			case ICMPMessage.TIMESTAMP: {
				System.out
						.println("Se encontro un paquete ICMP TIMESTAMP proveniente de "
								+ icmpmsg.getSourceAdrr());
				System.out.println("Paquete ICMP: " + icmpmsg.toString());
	
			}
				;
				break;
	
			case ICMPMessage.TIMESTAMP_REPLY: {
				System.out
						.println("Se encontro un paquete ICMP TIMESTAMP REPLY proveniente de "
								+ icmpmsg.getSourceAdrr());
				System.out.println("Paquete ICMP: " + icmpmsg.toString());
	
			}
				;
				break;
	
			case ICMPMessage.INFORMATION_REQUEST: {
				System.out
						.println("Se encontro un paquete ICMP INFORMATION REQUEST proveniente de "
								+ icmpmsg.getSourceAdrr());
				System.out.println("Paquete ICMP: " + icmpmsg.toString());
	
			}
				;
				break;
	
			case ICMPMessage.INFORMATION_REPLY: {
				System.out
						.println("Se encontro un paquete ICMP INFORMATION REPLY proveniente de "
								+ icmpmsg.getSourceAdrr());
				System.out.println("Paquete ICMP: " + icmpmsg.toString());
	
			}
				;
				break;
	
			default: {
				System.out.print("El codigo del paquete ICMP no es válido. Tipo: "
						+ icmpmsg.type);
				try {
					System.out.print(" tipo no implementado. Etiqueta: "
							+ ICMPInterface.typeLabels[icmpmsg.type] + "/n");
				} catch (Exception e) {
				}
				return false; // No se proceso el paquete como un paquete ICMP,
								// posiblemente sea un paquete correspondiente a IP
			}
		} // End del switch
		return true; // Se trato el paquete como ICMP
	}

	public void send(byte type, byte code, IpAddress dest, Datagram data) {
		ICMPMessage icmpp = new ICMPMessage();
		icmpp.data = new byte[60]; // Mirar segun tipo y codigo
		icmpp.type = type;
		icmpp.code = code;

		// Armar los mensajes dependiendo del Tipo y Codigo
		switch (type) {
			/*   El caso del echo reply no se da en el send ya que se envia automaticamente
			 *   cuando se recibe el echo request
			case ICMP.ECHO_REPLY: {
				System.out.println("Envio de mensaje ICMP ECHO_REPLY, tipo " + type + " código " + code);
				
			}
				;
				break;
			*/
			case ICMP.DESTINATION_UNREACHABLE: {
				System.out.println("Envio de mensaje ICMP DESTINATION_UNREACHABLE, tipo " + type + " código " + code);
				
			}
				;
				break;
	
			case ICMP.SOURCE_QUENCH: {
				System.out.println("Envio de mensaje ICMP SOURCE_QUENCH, tipo " + type + " código " + code);
	
			}
				;
				break;
	
			case ICMP.REDIRECT: {
				System.out.println("Envio de mensaje ICMP REDIRECT, tipo " + type + " código " + code);
	
			}
				;
				break;
	
			case ICMP.ECHO_REQUEST: {
				System.out.println("Envio de mensaje ICMP ECHO_REQUEST, tipo " + type + " código " + code);
				ping(dest);
			}
				;
				break;
	
			case ICMP.ROUTER_ADVERT: {
				System.out.println("Envio de mensaje ICMP ROUTER_ADVERT, tipo " + type + " código " + code);
	
			}
				;
				break;
	
			case ICMP.ROUTER_SOLICIT: {
				System.out.println("Envio de mensaje ICMP ROUTER_SOLICIT, tipo " + type + " código " + code);
	
			}
				;
				break;
	
			case ICMP.TIME_EXCEEDED: {
				System.out.println("Envio de mensaje ICMP TIME_EXCEEDED, tipo " + type + " código " + code);
	
			}
				;
				break;
	
			case ICMP.PARAMETER_PROBLEM: {
				System.out.println("Envio de mensaje ICMP PARAMETER_PROBLEM, tipo " + type + " código " + code);
	
			}
				;
				break;
	
			case ICMP.TIMESTAMP: {
				System.out.println("Envio de mensaje ICMP TIMESTAMP, tipo " + type + " código " + code);
	
			}
				;
				break;
	
			case ICMP.TIMESTAMP_REPLY: {
				System.out.println("Envio de mensaje ICMP TIMESTAMP_REPLY, tipo " + type + " código " + code);
	
			}
				;
				break;
	
			case ICMP.INFORMATION_REQUEST: {
				System.out.println("Envio de mensaje ICMP INFORMATION_REQUEST, tipo " + type + " código " + code);
	
			}
				;
				break;
	
			case ICMP.INFORMATION_REPLY: {
				System.out.println("Envio de mensaje ICMP INFORMATION_REPLY, tipo " + type + " código " + code);
	
			}
				;
				break;
	
			default: {
				System.out
						.println("El envio de mensaje ICMP fallo. No se encontro el tipo de mensaje. Tipo " + type + " código " + code);
				return;
			}
		}
		
	}

	public void ping(IpAddress dest) {
		ICMPMessage icmpmsg = new ICMPMessage();
		icmpmsg.data = new byte[28];
		icmpmsg.type = (byte) 8;
		icmpmsg.code = 0;
		icmpmsg.idMessage = 1000;
		icmpmsg.sequenceNumber = 1;
		for (int i = 8; i < 20; i++)
			icmpmsg.datosEcho[i - 8] = (byte)5; //completamos el mensaje con 0101 por cada byte
		icmpmsg.update();
		try {
			// CREAR EL DATAGRAM A MANDAR CON SUS RESPECTIVOS CAMPOS.
			Datagram datagram = new Datagram(200);
			datagram.setDestAddress(dest);
			datagram.setData(icmpmsg.toByteArray(), true);
			datagram.genChecksum();
			eventoN3 evReq = new eventoN3(eventoN3.SEND, datagram);
			ip.addLoc(evReq);
		} catch (Exception e) {
			System.out
					.println("Error en la generacion y envio de ECHO REQUEST. Posible ruta no encontrada.");
		}
	}

	
	
	//COMPLETAR CORRECTAMENTE LOS METODOS INFERIORES
	
	
	// Procesa un requerimiento de envio u otro del nivel superior. Recibe en un
	// byte la info de
	//Este evento es llamado por el thread de lectura de las colas remotas y locales.
	//Cada vez q se encuentra algun elemento en la cola, el thread lo pasa a este metodo.

	public void receive_loc(eventoN3 pack) {
		System.out.println("Encontro requerimiento ICMP en cola local");
		// Segun la primitiva recibida, toma la accion que corresponda
		int opcion = pack.getControl();
		// Ver que es lo que hace esto porq no hace nada...
		// byte[] bb = (byte[]) idu.getInfo();
		switch (opcion) {
		case eventoN3.SEND: // Recibe info para enviar
			// aca se debria ver el nexthop, la interfaz, mtu, fragmentar, etc
			System.out.println("ICMP recibe info a enviar");
			ICMPPacketSend infoSend = (ICMPPacketSend) pack.getInfo();
			send(infoSend.getType(), infoSend.getCode(), infoSend.getDest(),
					infoSend.getData());
			break;
		default:
			System.out
			.println("El tipo del requerimiento recibido localmente no es 'informacion a enviar'. TIPO ERRONEO");
			break;
		}
	}

	// Procesa un datagram remoto o alguan indicacion del nivel inferior. Esto
	// esta en buffRem.
	//Este evento es llamado por el thread de lectura de las colas remotas y locales.
	//Cada vez q se encuentra algun elemento en la cola, el thread lo pasa a este metodo.

	public void receive_rem(eventoN3 indN2) {
		int opcion = indN2.getControl();
		System.out
				.println("Encontro requerimiento en cola remota de ICMP tipo "
						+ opcion);
		// Segun la primitiva recibida, toma la accion que corresponda
		switch (opcion) {
		case eventoN3.INFO_RECEIVED: // Recepcion de un frame link layer con
										// indicacion de
			handle(indN2);
			break;
		default:
			System.out
			.println("El tipo del requerimiento recibido remotamente no es 'informacion recibida'. TIPO ERRONEO");
			break;
		}
	}
	
	//READER DE LAS COLAS DE PEDIDOS
	//COMPLETAR BIEN ESTO
	
	
	class Reader extends Thread {
		ICMP icmp; // Instancia ICMP
		eventoN3 inforem;// info remota recibida(buffInp), un datagram mas otras
							// cosas
		eventoN3 infoloc;// info local recibida por IP

		public Reader(ICMP icmp) {
			this.icmp = icmp;
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
					icmp.receive_rem(inforem);
				}
				// Si hay info en la cola de entrada local, nivel superior,
				// procesa
				if ((infoloc = (eventoN3) buffLoc.peekpopBack()) != null) {
					// recibe req de n4
					icmp.receive_loc(infoloc);
				}
			}
		}

	}

	
	
}