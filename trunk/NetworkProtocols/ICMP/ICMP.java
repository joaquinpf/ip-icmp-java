package NetworkProtocols.ICMP;

//import java.util.Date;

import Exceptions.*;
import Forms.Principal;
//import Interface.*;
import Utils.*;
import NetworkProtocols.*;
import NetworkProtocols.IP.*;
import NetworkProtocols.IP.Address.*;

public class ICMP implements ProtocolInterface, ICMPInterface {
	static Queue buffRem; // Buffer de entrada remota a ICMP
	static Queue buffLoc; // Buffer de entrada local a ICMP
	private IP ip;
	@SuppressWarnings("unused")
	private Reader rdr;
	private int msgId = 0;
	
	// Metodo invocado por IP agregar un evento a la cola remota
	public void addRem(eventoN3 x) {
		buffRem.pushBack(x);
	}

	// Metodo invocado por el nivel superior o IP para agregar un evento a la cola local
	public void addLoc(eventoN3 x) {
		buffLoc.pushBack(x);
	}

	public ICMP(){
		NetworkProtocols.addProtocol(NetworkProtocols.PROTO_ICMP, this);
		this.ip = (IP) NetworkProtocols.getProtocol(NetworkProtocols.PROTO_IP);
		buffRem = new Queue();
		buffLoc = new Queue();
		this.rdr = new Reader(this);
	}

	public boolean handle(eventoN3 p) { // Es invocado cuando IP recibe un datagram que corresponde a ICMP
		ICMPMessage icmpmsg = null;
		try {
			icmpmsg = new ICMPMessage(p);
		} catch (MalformedPacketException e) {
			e.printStackTrace();
			return false; // El paquete recibido estaba mal formado o no era un paquete ICMP
		}
		System.out.println("Se encontro un paquete ICMP en la cola remota");
		Principal.addReceived("Se encontro un paquete ICMP en la cola remota\n");
		switch (icmpmsg.type) {
			case ICMPMessage.ECHO_REQUEST: {
				System.out.println("Paquete ICMP: " + icmpmsg.toString());
				Principal.addReceived("Paquete ICMP: " + icmpmsg.toString() + "\n");
				ICMPMessage rp = icmpmsg.reply();
				System.out.println("Se generó respuesta ICMP y se procederá al envio del mensaje: " + rp.toString());
				Principal.addSended("Se generó respuesta ICMP y se procederá al envio del mensaje: " + rp.toString() + "\n");
				try {
					Datagram datagram = new Datagram((byte[]) ((Datagram)p.getInfo()).toByte());
					
					IpAddress auxSrc = datagram.getSourceAddress();
					datagram.setsourceAddress(datagram.getDestAddress());
					datagram.setDestAddress(auxSrc);
					datagram.setData(rp.toByteArray());
					// El resto de los campos teoricamente los completa IP antes de mandar el datagram.
					eventoN3 evReply = new eventoN3(eventoN3.SEND, datagram);
					ip.addLoc(evReply);
				} catch (Exception e) {
					e.printStackTrace();
					System.out
							.println("Error en la generacion y envio de ECHO REPLY. Posible ruta no encontrada.");
					Principal.addSended("Error en la generacion y envio de ECHO REPLY. Posible ruta no encontrada.\n");
				}
			}
				;
				break;
	
			case ICMPMessage.ECHO_REPLY: {
				System.out.println("Paquete ICMP: " + icmpmsg.toString());
				Principal.addReceived("Paquete ICMP: " + icmpmsg.toString() + "\n");
			}
				;
				break;
	
			case ICMPMessage.DESTINATION_UNREACHABLE: {
				System.out.println("Paquete ICMP: " + icmpmsg.toString());
				Principal.addReceived("Paquete ICMP: " + icmpmsg.toString() + "\n");
			}
				;
				break;
	
			case ICMPMessage.SOURCE_QUENCH: {
				System.out.println("Paquete ICMP: " + icmpmsg.toString());
				Principal.addReceived("Paquete ICMP: " + icmpmsg.toString() + "\n");
			}
				;
				break;
	
			case ICMPMessage.REDIRECT: {
				System.out.println("Paquete ICMP: " + icmpmsg.toString());
				Principal.addReceived("Paquete ICMP: " + icmpmsg.toString() + "\n");
			}
				;
				break;
	
			case ICMPMessage.ROUTER_ADVERT: {
				System.out.println("Paquete ICMP: " + icmpmsg.toString());
				Principal.addReceived("Paquete ICMP: " + icmpmsg.toString() + "\n");
			}
				;
				break;
	
			case ICMPMessage.ROUTER_SOLICIT: {
				System.out.println("Paquete ICMP: " + icmpmsg.toString());
				Principal.addReceived("Paquete ICMP: " + icmpmsg.toString() + "\n");
			}
				;
				break;
	
			case ICMPMessage.TIME_EXCEEDED: {
				System.out.println("Paquete ICMP: " + icmpmsg.toString());
				Principal.addReceived("Paquete ICMP: " + icmpmsg.toString() + "\n");
			}
				;
				break;
	
			case ICMPMessage.PARAMETER_PROBLEM: {
				System.out.println("Paquete ICMP: " + icmpmsg.toString());
				Principal.addReceived("Paquete ICMP: " + icmpmsg.toString() + "\n");
			}
				;
				break;
	
			case ICMPMessage.TIMESTAMP: {
				System.out.println("Paquete ICMP: " + icmpmsg.toString());
				Principal.addReceived("Paquete ICMP: " + icmpmsg.toString());
				ICMPMessage rp = icmpmsg.reply();
				System.out.println("Se generó respuesta ICMP y se procederá al envio del mensaje: " + rp.toString());
				Principal.addSended("Se generó respuesta ICMP y se procederá al envio del mensaje: " + rp.toString() + "\n");
				try {
					Datagram datagram = new Datagram((byte[]) ((Datagram)p.getInfo()).toByte());
					IpAddress auxSrc = datagram.getSourceAddress();
					datagram.setsourceAddress(datagram.getDestAddress());
					datagram.setDestAddress(auxSrc);
					datagram.setData(rp.toByteArray());
					// El resto de los campos teoricamente los completa IP antes de mandar el datagram.
					eventoN3 evReply = new eventoN3(eventoN3.SEND, datagram);
					ip.addLoc(evReply);
				} catch (Exception e) {
					e.printStackTrace();
					System.out
							.println("Error en la generacion y envio de TIMESTAMP REPLY. Posible ruta no encontrada.");
					Principal.addSended("Error en la generacion y envio de TIMESTAMP REPLY. Posible ruta no encontrada.\n");
				}
			}
				;
				break;
	
			case ICMPMessage.TIMESTAMP_REPLY: {
				System.out.println("Paquete ICMP: " + icmpmsg.toString());
				Principal.addReceived("Paquete ICMP: " + icmpmsg.toString() + "\n");
			}
				;
				break;
	
			case ICMPMessage.INFORMATION_REQUEST: {
				System.out.println("Paquete ICMP: " + icmpmsg.toString());
				Principal.addReceived("Paquete ICMP: " + icmpmsg.toString() + "\n");
				ICMPMessage rp = icmpmsg.reply();
				System.out.println("Se generó respuesta ICMP y se procederá al envio del mensaje: " + rp.toString());
				Principal.addSended("Se generó respuesta ICMP y se procederá al envio del mensaje: " + rp.toString() + "\n");
				try {
					Datagram datagram = new Datagram((byte[]) ((Datagram)p.getInfo()).toByte());
					IpAddress auxSrc = datagram.getSourceAddress();
					datagram.setsourceAddress(datagram.getDestAddress());
					datagram.setDestAddress(auxSrc);
					datagram.setData(rp.toByteArray());
					// El resto de los campos teoricamente los completa IP antes de mandar el datagram.
					eventoN3 evReply = new eventoN3(eventoN3.SEND, datagram);
					ip.addLoc(evReply);
				} catch (Exception e) {
					e.printStackTrace();
					System.out
							.println("Error en la generacion y envio de INFORMATION REPLY. Posible ruta no encontrada.");
					Principal.addSended("Error en la generacion y envio de INFORMATION REPLY. Posible ruta no encontrada.\n");
				}
			}
				;
				break;
	
			case ICMPMessage.INFORMATION_REPLY: {
				System.out.println("Paquete ICMP: " + icmpmsg.toString());
				Principal.addReceived("Paquete ICMP: " + icmpmsg.toString() + "\n");
			}
				;
				break;

			case ICMPMessage.ADDRESS_MASK_REQUEST: {
				System.out.println("Paquete ICMP: " + icmpmsg.toString());
				Principal.addReceived("Paquete ICMP: " + icmpmsg.toString() + "\n");
				ICMPMessage rp = icmpmsg.reply();
				System.out.println("Se generó respuesta ICMP y se procederá al envio del mensaje: " + rp.toString());
				Principal.addSended("Se generó respuesta ICMP y se procederá al envio del mensaje: " + rp.toString() + "\n");
				try {
					Datagram datagram = new Datagram((byte[]) ((Datagram)p.getInfo()).toByte());
					IpAddress auxSrc = datagram.getSourceAddress();
					datagram.setsourceAddress(datagram.getDestAddress());
					datagram.setDestAddress(auxSrc);
					datagram.setData(rp.toByteArray());
					// El resto de los campos teoricamente los completa IP antes de mandar el datagram.
					eventoN3 evReply = new eventoN3(eventoN3.SEND, datagram);
					ip.addLoc(evReply);
				} catch (Exception e) {
					e.printStackTrace();
					System.out
							.println("Error en la generacion y envio de ADDRESS MASK REPLY. Posible ruta no encontrada.");
					Principal.addSended("Error en la generacion y envio de ADDRESS MASK REPLY. Posible ruta no encontrada.\n");
				}
			}
				;
				break;
	
			case ICMPMessage.ADDRESS_MASK_REPLY: {
				System.out.println("Paquete ICMP: " + icmpmsg.toString());
				Principal.addReceived("Paquete ICMP: " + icmpmsg.toString() + "\n");
			}
				;
				break;

			default: {
				System.out.print("El codigo del paquete ICMP no es válido. Tipo: "
						+ icmpmsg.type);
				Principal.addReceived("El codigo del paquete ICMP no es válido. Tipo: " + icmpmsg.type + "\n");
				try {
					System.out.print(" tipo no implementado. Etiqueta: "
							+ ICMPInterface.typeLabels[icmpmsg.type] + "/n");
					Principal.addReceived(" tipo no implementado. Etiqueta: " + ICMPInterface.typeLabels[icmpmsg.type] + "/n");
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
			case ICMP.DESTINATION_UNREACHABLE: {
				System.out.println("Envio de mensaje ICMP DESTINATION_UNREACHABLE, tipo " + type + " código " + code + " direccion " + dest.toString());
				Principal.addSended("Envio de mensaje ICMP DESTINATION_UNREACHABLE, tipo " + type + " código " + code + " direccion " + dest.toString() + "\n");
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
				Principal.addSended("Envio de mensaje ICMP SOURCE_QUENCH, tipo " + type + " código " + code + "\n");
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
				Principal.addSended("Envio de mensaje ICMP REDIRECT, tipo " + type + " código " + code + "\n");
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
				Principal.addSended("Envio de mensaje ICMP ECHO_REQUEST, tipo " + type + " código " + code + " direccion destino " + dest.toString() + "\n");
				ping(dest);
			}
				;
				break;
	
			case ICMP.ROUTER_ADVERT: {
				System.out.println("Envio de mensaje ICMP ROUTER_ADVERT, tipo " + type + " código " + code);
				Principal.addSended("Envio de mensaje ICMP ROUTER_ADVERT, tipo " + type + " código " + code + "\n");
			}
				;
				break;
	
			case ICMP.ROUTER_SOLICIT: {
				System.out.println("Envio de mensaje ICMP ROUTER_SOLICIT, tipo " + type + " código " + code);
				Principal.addSended("Envio de mensaje ICMP ROUTER_SOLICIT, tipo " + type + " código " + code + "\n");
			}
				;
				break;
	
			case ICMP.TIME_EXCEEDED: {
				System.out.println("Envio de mensaje ICMP TIME_EXCEEDED, tipo " + type + " código " + code);
				Principal.addSended("Envio de mensaje ICMP TIME_EXCEEDED, tipo " + type + " código " + code + "\n");
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
				Principal.addSended("Envio de mensaje ICMP TIMESTAMP, tipo " + type + " código " + code + "\n");
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

			case ICMP.INFORMATION_REQUEST: {
				System.out.println("Envio de mensaje ICMP INFORMATION_REQUEST, tipo " + type + " código " + code);
				Principal.addSended("Envio de mensaje ICMP INFORMATION_REQUEST, tipo " + type + " código " + code + "\n");
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

			case ICMP.ADDRESS_MASK_REQUEST: {
				System.out.println("Envio de mensaje ICMP ADDRESS_MASK_REQUEST, tipo " + type + " código " + code);
				Principal.addSended("Envio de mensaje ICMP ADDRESS_MASK_REQUEST, tipo " + type + " código " + code + "\n");
				icmpp.data = new byte[12];
				icmpp.type = type;
				icmpp.code = code;
				icmpp.identificador = new byte[2];
				icmpp.nroSecuencia = new byte[2];
				icmpp.identificador[0] = 0x00;
				icmpp.identificador[1] = 0x00;
				icmpp.nroSecuencia[0] = 0x00;
				icmpp.nroSecuencia[1] = 0x00;
				icmpp.subnetAddressMask = new byte[4];
				icmpp.subnetAddressMask[0] = 0x00;
				icmpp.subnetAddressMask[1] = 0x00;
				icmpp.subnetAddressMask[2] = 0x00;
				icmpp.subnetAddressMask[3] = 0x00;
			}
				;
				break;

			case ICMP.PARAMETER_PROBLEM: {
				send(type, code, (byte)0x01, data);
			}
				;
				break;

			default: {
				System.out
						.println("El envio de mensaje ICMP fallo. No se encontro el tipo de mensaje. Tipo " + type + " código " + code);
				Principal.addSended("El envio de mensaje ICMP fallo. No se encontro el tipo de mensaje. Tipo " + type + " código " + code + "\n");
				return;
			}
		}
		// CREAR EL DATAGRAM A MANDAR CON SUS RESPECTIVOS CAMPOS
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
				Principal.addSended("Envio de mensaje ICMP PARAMETER_PROBLEM, tipo " + type + " código " + code + "\n");
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
				Principal.addSended("El envio de mensaje ICMP fallo. No se encontro el tipo de mensaje. Tipo " + type + " código " + code + "\n");
				return;
			}
		}
		// CREAR EL DATAGRAM A MANDAR CON SUS RESPECTIVOS CAMPOS
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
			Principal.addSended("Ping a " + dest.toString() + "\n");
			
			eventoN3 evReq = new eventoN3(eventoN3.SEND, datagram.toByte());
			ip.addLoc(evReq);
		} catch (Exception e) {
			System.out
					.println("Error en la generacion y envio de ECHO REQUEST. Posible ruta no encontrada.");
			Principal.addSended("Error en la generacion y envio de ECHO REQUEST. Posible ruta no encontrada.\n");
		}
	}

	// Procesa un requerimiento de envio u otro del nivel superior. Recibe en un byte la info
	//Este evento es llamado por el thread de lectura de las colas remotas y locales.
	//Cada vez q se encuentra algun elemento en la cola, el thread lo pasa a este metodo.

	public void receive_loc(eventoN3 pack) {
		System.out.println("Encontro requerimiento ICMP en cola local");
		Principal.addSended("Encontro requerimiento ICMP en cola local\n");
		// Segun la primitiva recibida, toma la accion que corresponda
		int opcion = pack.getControl();
		switch (opcion) {
		case eventoN3.SEND: // Recibe info para enviar
			System.out.println("ICMP envía la informacion de la cola");
			Principal.addSended("ICMP envía la informacion de la cola\n");
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
		Principal.addSended("El tipo del requerimiento recibido localmente no es 'informacion a enviar'. TIPO ERRONEO\n");
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
				.println("Encontro requerimiento en cola remota de ICMP");
		Principal.addReceived("Encontro requerimiento en cola remota de ICMP\n");
		// Segun la primitiva recibida, toma la accion que corresponda
		switch (opcion) {
		case eventoN3.INFO_RECEIVED:
			handle(indN2);
			break;
		default:
			System.out
			.println("El tipo del requerimiento recibido remotamente no es 'informacion recibida'. TIPO ERRONEO");
		Principal.addReceived("El tipo del requerimiento recibido remotamente no es 'informacion recibida'. TIPO ERRONEO\n");
			break;
		}
	}
	
	//READER DE LAS COLAS DE PEDIDOS
	class Reader extends Thread {
		ICMP icmp; // Instancia ICMP
		eventoN3 inforem;// info remota recibida
		eventoN3 infoloc;// info local recibida

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
				// Si hay info en la cola de entrada remota, nivel inferior, procesa
				if ((inforem = (eventoN3) buffRem.peekpopBack()) != null) {
					icmp.receive_rem(inforem);
				}
				// Si hay info en la cola de entrada local, nivel superior, procesa
				if ((infoloc = (eventoN3) buffLoc.peekpopBack()) != null) {
					icmp.receive_loc(infoloc);
				}
			}
		}
	}
}