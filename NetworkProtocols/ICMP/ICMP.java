package NetworkProtocols.ICMP;

//import java.util.Date;

import Exceptions.*;
//import Interface.*;
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
	@SuppressWarnings("unused")
	private Reader rdr;
	private int msgId = 0;
	
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
		buffRem = new Queue();
		buffLoc = new Queue();
	}

	public boolean handle(eventoN3 p) { // Es invocado cuando IP recibe un
										// datagram que corresponde a ICMP
		ICMPMessage icmpmsg = null;
		try {
			icmpmsg = new ICMPMessage(p);
		} catch (MalformedPacketException e) {
			e.printStackTrace();
			return false; // El paquete recibido estaba mal formado o no era un
							// paquete ICMP
		}
		System.out.println("Se encontro un paquete ICMP en la cola remota");
//				+ icmpmsg.getSourceAdrr());
		switch (icmpmsg.type) {
			case ICMPMessage.ECHO_REQUEST: {
				System.out.println("Paquete ICMP: " + icmpmsg.toString());
				ICMPMessage rp = icmpmsg.reply();
				System.out.println("Se generó respuesta ICMP y se procederá al envio del mensaje: " + rp.toString());
				try {
					Datagram datagram = new Datagram((byte[]) ((Datagram)p.getInfo()).toByte());
					
					IpAddress auxSrc = datagram.getSourceAddress();
					datagram.setsourceAddress(datagram.getDestAddress());
					datagram.setDestAddress(auxSrc);
					datagram.setData(rp.toByteArray());
					// El resto de los campos teoricamente los completa IP antes de
					// mandar el datagram.
					eventoN3 evReply = new eventoN3(eventoN3.SEND, datagram);
					ip.addLoc(evReply);
				} catch (Exception e) {
					e.printStackTrace();
					System.out
							.println("Error en la generacion y envio de ECHO REPLY. Posible ruta no encontrada.");
				}
			}
				;
				break;
	
			case ICMPMessage.ECHO_REPLY: {
				System.out.println("Paquete ICMP: " + icmpmsg.toString());
			}
				;
				break;
	
			case ICMPMessage.DESTINATION_UNREACHABLE: {
				System.out.println("Paquete ICMP: " + icmpmsg.toString());
			}
				;
				break;
	
			case ICMPMessage.SOURCE_QUENCH: {
				System.out.println("Paquete ICMP: " + icmpmsg.toString());
			}
				;
				break;
	
			case ICMPMessage.REDIRECT: {
				System.out.println("Paquete ICMP: " + icmpmsg.toString());
			}
				;
				break;
	
			case ICMPMessage.ROUTER_ADVERT: {
				System.out.println("Paquete ICMP: " + icmpmsg.toString());
			}
				;
				break;
	
			case ICMPMessage.ROUTER_SOLICIT: {
				System.out.println("Paquete ICMP: " + icmpmsg.toString());
			}
				;
				break;
	
			case ICMPMessage.TIME_EXCEEDED: {
				System.out.println("Paquete ICMP: " + icmpmsg.toString());
			}
				;
				break;
	
			case ICMPMessage.PARAMETER_PROBLEM: {
				System.out.println("Paquete ICMP: " + icmpmsg.toString());
			}
				;
				break;
	
			case ICMPMessage.TIMESTAMP: {
				System.out.println("Paquete ICMP: " + icmpmsg.toString());
				ICMPMessage rp = icmpmsg.reply();
				System.out.println("Se generó respuesta ICMP y se procederá al envio del mensaje: " + rp.toString());
				try {
					Datagram datagram = new Datagram((byte[]) p.getInfo());
					datagram.setsourceAddress(datagram.getDestAddress());
					datagram.setDestAddress(datagram.getSourceAddress());
					datagram.setData(rp.toByteArray());
					// El resto de los campos teoricamente los completa IP antes de
					// mandar el datagram.
					eventoN3 evReply = new eventoN3(eventoN3.SEND, datagram);
					ip.addLoc(evReply);
				} catch (Exception e) {
					System.out
							.println("Error en la generacion y envio de TIMESTAMP REPLY. Posible ruta no encontrada.");
				}
			}
				;
				break;
	
			case ICMPMessage.TIMESTAMP_REPLY: {
				System.out.println("Paquete ICMP: " + icmpmsg.toString());
			}
				;
				break;
	
			case ICMPMessage.INFORMATION_REQUEST: {
				System.out.println("Paquete ICMP: " + icmpmsg.toString());
				ICMPMessage rp = icmpmsg.reply();
				System.out.println("Se generó respuesta ICMP y se procederá al envio del mensaje: " + rp.toString());
				try {
					Datagram datagram = new Datagram((byte[]) p.getInfo());
					datagram.setsourceAddress(datagram.getDestAddress());
					datagram.setDestAddress(datagram.getSourceAddress());
					datagram.setData(rp.toByteArray());
					// El resto de los campos teoricamente los completa IP antes de
					// mandar el datagram.
					eventoN3 evReply = new eventoN3(eventoN3.SEND, datagram);
					ip.addLoc(evReply);
				} catch (Exception e) {
					System.out
							.println("Error en la generacion y envio de INFORMATION REPLY. Posible ruta no encontrada.");
				}

			}
				;
				break;
	
			case ICMPMessage.INFORMATION_REPLY: {
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

	// Armar los mensajes dependiendo del Tipo y Codigo
	public void send(byte type, byte code, IpAddress dest, Datagram data) {
		ICMPMessage icmpp = new ICMPMessage();
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
				System.out.println("Envio de mensaje ICMP DESTINATION_UNREACHABLE, tipo " + type + " código " + code + " direccion " + dest.toString());
				icmpp.data = new byte[36];
				icmpp.type = type;
				icmpp.code = code;
				icmpp.ipHeader = new byte[data.getHeaderLength() * 4];
				System.arraycopy(data.getHeaderBytes(), 0, icmpp.ipHeader, 0, 20);
				icmpp.bitsDatosDatagramOriginal = new byte[8];
				System.arraycopy(data.getData(), 0, icmpp.bitsDatosDatagramOriginal, 0, 8);
			}
				;
				break;
	
			case ICMP.SOURCE_QUENCH: {
				System.out.println("Envio de mensaje ICMP SOURCE_QUENCH, tipo " + type + " código " + code);
				icmpp.data = new byte[36];
				icmpp.type = type;
				icmpp.code = code;
				icmpp.ipHeader = new byte[data.getHeaderLength() * 4];
				System.arraycopy(data.getHeaderBytes(), 0, icmpp.ipHeader, 0, 20);
				icmpp.bitsDatosDatagramOriginal = new byte[8];
				System.arraycopy(data.getData(), 0, icmpp.bitsDatosDatagramOriginal, 0, 8);
			}
				;
				break;
	
			case ICMP.REDIRECT: {
				System.out.println("Envio de mensaje ICMP REDIRECT, tipo " + type + " código " + code);
				icmpp.data = new byte[36];
				icmpp.type = type;
				icmpp.code = code;
				icmpp.direccionIpGateway = new byte[4];
				System.arraycopy(dest.toByte(), 0, icmpp.direccionIpGateway, 0, 4);
				icmpp.ipHeader = new byte[data.getHeaderLength() * 4];
				System.arraycopy(data.getHeaderBytes(), 0, icmpp.ipHeader, 0, 20);
				icmpp.bitsDatosDatagramOriginal = new byte[8];
				System.arraycopy(data.getData(), 0, icmpp.bitsDatosDatagramOriginal, 0, 8);
			}
				;
				break;
	
			case ICMP.ECHO_REQUEST: {
				System.out.println("Envio de mensaje ICMP ECHO_REQUEST, tipo " + type + " código " + code + " direccion destino " + dest.toString());
				ping(dest);
			}
				;
				break;
	
			case ICMP.ROUTER_ADVERT: {
				System.out.println("Envio de mensaje ICMP ROUTER_ADVERT, tipo " + type + " código " + code);
				//Falta implementacion
			}
				;
				break;
	
			case ICMP.ROUTER_SOLICIT: {
				System.out.println("Envio de mensaje ICMP ROUTER_SOLICIT, tipo " + type + " código " + code);
				//Falta implementacion
			}
				;
				break;
	
			case ICMP.TIME_EXCEEDED: {
				System.out.println("Envio de mensaje ICMP TIME_EXCEEDED, tipo " + type + " código " + code);
				icmpp.data = new byte[36];
				icmpp.type = type;
				icmpp.code = code;
				icmpp.ipHeader = new byte[data.getHeaderLength() * 4];
				System.arraycopy(data.getHeaderBytes(), 0, icmpp.ipHeader, 0, 20);
				icmpp.bitsDatosDatagramOriginal = new byte[8];
				System.arraycopy(data.getData(), 0, icmpp.bitsDatosDatagramOriginal, 0, 8);
			}
				;
				break;

			case ICMP.TIMESTAMP: {
				System.out.println("Envio de mensaje ICMP TIMESTAMP, tipo " + type + " código " + code);
				icmpp.data = new byte[20];
				icmpp.type = type;
				icmpp.code = code;
				icmpp.identificador = new byte[2];
				icmpp.nroSecuencia = new byte[2];
				icmpp.identificador[0] = 0x00;
				icmpp.identificador[1] = 0x00;
				icmpp.nroSecuencia[0] = 0x00;
				icmpp.nroSecuencia[1] = 0x00;

				icmpp.marcaTiempoOrigen = new byte[4];
				
				Long m = System.currentTimeMillis();
				for(int i= 0; i < 4; i++){  
					icmpp.marcaTiempoOrigen[3 - i] = (byte)(m >>> (i * 8));  
				}  

				icmpp.marcaTiempoRecepcion = new byte[4];
				icmpp.marcaTiempoRecepcion[0] = 0x00;
				icmpp.marcaTiempoRecepcion[1] = 0x00;
				icmpp.marcaTiempoRecepcion[2] = 0x00;
				icmpp.marcaTiempoRecepcion[3] = 0x00;
				icmpp.marcaTiempoTransmision = new byte[4];
				icmpp.marcaTiempoTransmision[0] = 0x00;
				icmpp.marcaTiempoTransmision[1] = 0x00;
				icmpp.marcaTiempoTransmision[2] = 0x00;
				icmpp.marcaTiempoTransmision[3] = 0x00;
			}
				;
				break;
				
			/*Timestamp Reply se genera automaticamente en ICMPMessage cuando se detecta
			 * Un Timestamp Request	
			 *
			case ICMP.TIMESTAMP_REPLY: {
				System.out.println("Envio de mensaje ICMP TIMESTAMP_REPLY, tipo " + type + " código " + code);
	
			}
				;
				break;
			*/
				
			case ICMP.INFORMATION_REQUEST: {
				System.out.println("Envio de mensaje ICMP INFORMATION_REQUEST, tipo " + type + " código " + code);
				icmpp.data = new byte[8];
				icmpp.type = type;
				icmpp.code = code;
				icmpp.identificador = new byte[2];
				icmpp.nroSecuencia = new byte[2];
				icmpp.identificador[0] = 0x00;
				icmpp.identificador[1] = 0x00;
				icmpp.nroSecuencia[0] = 0x00;
				icmpp.nroSecuencia[1] = 0x00;
			}
				;
				break;
	
				/*INFORMATION_REPLY se genera automaticamente en ICMPMessage cuando se detecta
				 * Un INFORMATION Request	
				 *
			case ICMP.INFORMATION_REPLY: {
				System.out.println("Envio de mensaje ICMP INFORMATION_REPLY, tipo " + type + " código " + code);
	
			}
				;
				break;
				 */
			default: {
				System.out
						.println("El envio de mensaje ICMP fallo. No se encontro el tipo de mensaje. Tipo " + type + " código " + code);
				return;
			}
		}
		// CREAR EL DATAGRAM A MANDAR CON SUS RESPECTIVOS CAMPOS.
		if (type != ICMP.ECHO_REQUEST) {
			
			Datagram datagram = new Datagram(data.getVersion(), data.getHeaderLength(), data.getPrecedence(), data.isDelay(), data.isThroughput(), 
					data.isReliability(), data.isCost(), data.isFlags_nousado(), data.getTotalLength(), data.getDatagramId(), data.isFlags_nousado(), 
					data.isFlags_ultfrag(), data.isFlags_fragm(), data.getFragOffset(), data.getTtl(), 1, data.getChecksum(), 
					this.ip.getLocalIpAddress(), dest, data.getOptions());

			datagram.setData(icmpp.toByteArray(),true);
			datagram.genChecksum();
			eventoN3 evReq = new eventoN3(eventoN3.SEND, datagram);
			ip.addLoc(evReq);
		}
	}

	/**
	 * Este send se utiliza solamente para enviar el mensaje de problemas de parametro
	 * @param type
	 * @param code
	 * @param ptr Puntero
	 * @param data
	 */
	public void send(byte type, byte code, byte ptr, Datagram data) {
		ICMPMessage icmpp = new ICMPMessage();
		switch (type) {
			case ICMP.PARAMETER_PROBLEM: {
				System.out.println("Envio de mensaje ICMP PARAMETER_PROBLEM, tipo " + type + " código " + code);
				icmpp.data = new byte[36];
				icmpp.type = type;
				icmpp.code = code;
				icmpp.puntero = ptr;
				icmpp.ipHeader = new byte[data.getHeaderLength() * 4];
				System.arraycopy(data.getHeaderBytes(), 0, icmpp.ipHeader, 0, 20);
				icmpp.bitsDatosDatagramOriginal = new byte[8];
				System.arraycopy(data.getData(), 0, icmpp.bitsDatosDatagramOriginal, 0, 8);
			}
				;
				break;
			default: {
				System.out
						.println("El envio de mensaje ICMP fallo. No se encontro el tipo de mensaje. Tipo " + type + " código " + code);
				return;
			}
		}
		// CREAR EL DATAGRAM A MANDAR CON SUS RESPECTIVOS CAMPOS.
		Datagram datagram = new Datagram(36);
		datagram.setDestAddress(data.getSourceAddress()); //La direccion origen del datagram original es la destino de este
		datagram.setsourceAddress(this.ip.getLocalIpAddress());
		datagram.setProtocol(1);
		datagram.setData(icmpp.toByteArray(), true);
		datagram.genChecksum();
		eventoN3 evReq = new eventoN3(eventoN3.SEND, datagram);
		ip.addLoc(evReq);
	}

	public void ping(IpAddress dest) {
		ICMPMessage icmpmsg = new ICMPMessage();
		icmpmsg.data = new byte[28];
		icmpmsg.type = (byte) 8;
		icmpmsg.code = 0x00;
		icmpmsg.idMessage = msgId++;
		icmpmsg.sequenceNumber = 1;
		icmpmsg.datosEcho = new byte[20];
		for (int i = 0; i < 20; i++)
			icmpmsg.datosEcho[i] = 0x55; //completamos el mensaje con 01010101 por cada byte
		//icmpmsg.update();
		try {
			// CREAR EL DATAGRAM A MANDAR CON SUS RESPECTIVOS CAMPOS.
			Datagram datagram = new Datagram(28);
			datagram.setDestAddress(dest);
			datagram.setData(icmpmsg.toByteArray(), true);
			datagram.setsourceAddress(this.ip.getLocalIpAddress());
			datagram.setProtocol(1);
			datagram.genChecksum();
			System.out
			.println("Ping a " + dest.toString());
			
			eventoN3 evReq = new eventoN3(eventoN3.SEND, datagram.toByte());
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
			System.out.println("ICMP envía la informacion de la cola");
			ICMPPacketSend infoSend = (ICMPPacketSend) pack.getInfo();
			if (infoSend.getType() == PARAMETER_PROBLEM)
				send(infoSend.getType(), infoSend.getCode(), (byte)0x05 /*Valor seteado segun el octeto del problema*/,
						infoSend.getData());
			else
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