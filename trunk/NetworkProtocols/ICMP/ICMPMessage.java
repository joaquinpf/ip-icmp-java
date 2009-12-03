package NetworkProtocols.ICMP;

import java.io.*;

import Exceptions.MalformedPacketException;
import Forms.Principal;
import NetworkProtocols.*;
import NetworkProtocols.IP.Datagram;
import NetworkProtocols.IP.IP;
import NetworkProtocols.IP.Address.IpAddress;

class ICMPMessage implements ICMPInterface {
	public int HEADER_LENGTH = 8;
	int protocol = 0x1; // Setea el tipo de protocolo al tipo de ICMP
	byte type; // Tipo
	byte code; // Codigo
	int checksum; // Checksum del header mas los datos
	int idMessage; // Id del mansaje (utilizado para el Echo, Timestamp o
	// Information)
	int sequenceNumber; // Numero de secuencia (utilizado para el Echo,
	// Timestamp o Information)
	int gatewayAddress; // Direccion internet del Gateway (usada para Redirect
	// Message)
	byte pointer; // Puntero utilizado para Parameters Problem Message
	byte[] data; // Datos del paquete ICMP

	// Datos especificos del mensaje ICMP
	byte[] ipHeader; // 20 bytes
	byte[] postHeader; // 4 bytes     son todos ceros
	byte[] postHeader3; // 3 bytes    son todos ceros
	byte puntero; // 1 byte
	byte[] bitsDatosDatagramOriginal; // 8 bytes
	byte[] identificador; // 2 bytes
	byte[] nroSecuencia; // 2 bytes
	byte[] marcaTiempoOrigen; // 4 bytes
	byte[] marcaTiempoRecepcion; // 4 bytes
	byte[] marcaTiempoTransmision; // 4 bytes
	byte[] direccionIpGateway; // 4 bytes
	byte[] datosEcho; // 20 bytes
	byte[] subnetAddressMask; //4bytes
	
	IpAddress src = null;
	
	public ICMPMessage() {
		data = null;
	}

	public ICMPMessage(byte[] raw) throws MalformedPacketException {
		data = raw;
		parse();
	}

	public ICMPMessage(eventoN3 p) throws MalformedPacketException {
		Datagram datagram;
		if (p.getInfo().getClass().equals(Datagram.class))
			datagram = (Datagram) p.getInfo();
		else
			datagram = new Datagram((byte[]) p.getInfo());
		data = datagram.getData();
		parse();
	}

	private void parse() throws MalformedPacketException {
		if (validChecksum() == false)
			throw new MalformedPacketException();
		DataInputStream ds = new DataInputStream(new ByteArrayInputStream(data,
				0, 4));
		try {
			type = (byte) ds.readUnsignedByte();
			code = (byte) ds.readUnsignedByte();
			checksum = ds.readUnsignedShort();
			
			//Aca hay que crear el mensaje segun el tipo y codigo recibido
			switch (type) {
				case ECHO_REQUEST:
					ds = new DataInputStream(new ByteArrayInputStream(data,
							0, 28));
					ds.readUnsignedByte();
					ds.readUnsignedByte();
					ds.readUnsignedShort();
					idMessage = ds.readUnsignedShort();
					sequenceNumber = ds.readUnsignedShort();
					datosEcho = new byte[20];
					for (int i = 0; i < 20; i++)
						datosEcho[i] = (byte)ds.readUnsignedByte();
					break;
				case ECHO_REPLY:
					ds = new DataInputStream(new ByteArrayInputStream(data,
							0, 28));
					ds.readUnsignedByte();
					ds.readUnsignedByte();
					ds.readUnsignedShort();
					idMessage = ds.readUnsignedShort();
					sequenceNumber = ds.readUnsignedShort();
					datosEcho = new byte[20];
					for (int i = 0; i < 20; i++)
						datosEcho[i] = (byte)ds.readUnsignedByte();
					break;
				case ICMP.DESTINATION_UNREACHABLE: {
					ds = new DataInputStream(new ByteArrayInputStream(data,
							0, 36));
					ds.readUnsignedByte();
					ds.readUnsignedByte();
					ds.readUnsignedShort();
					postHeader = new byte[4];
					for (int i = 0; i < 4; i++)
						postHeader[i] = (byte)ds.readUnsignedByte();
					ipHeader = new byte[20];
					for (int i = 0; i < 20; i++)
						ipHeader[i] = (byte)ds.readUnsignedByte();
					bitsDatosDatagramOriginal = new byte[8];
					for (int i = 0; i < 8; i++)
						bitsDatosDatagramOriginal[i] = (byte)ds.readUnsignedByte();
				}
					;
					break;
				case ICMP.SOURCE_QUENCH: {
					ds = new DataInputStream(new ByteArrayInputStream(data,
							0, 36));
					ds.readUnsignedByte();
					ds.readUnsignedByte();
					ds.readUnsignedShort();
					postHeader = new byte[4];
					for (int i = 0; i < 4; i++)
						postHeader[i] = (byte)ds.readUnsignedByte();
					ipHeader = new byte[20];
					for (int i = 0; i < 20; i++)
						ipHeader[i] = (byte)ds.readUnsignedByte();
					bitsDatosDatagramOriginal = new byte[8];
					for (int i = 0; i < 8; i++)
						bitsDatosDatagramOriginal[i] = (byte)ds.readUnsignedByte();
				}
					;
					break;
				case ICMP.REDIRECT: {
					ds = new DataInputStream(new ByteArrayInputStream(data,
							0, 36));
					ds.readUnsignedByte();
					ds.readUnsignedByte();
					ds.readUnsignedShort();
					direccionIpGateway = new byte[4];
					for (int i = 0; i < 4; i++)
						direccionIpGateway[i] = (byte)ds.readUnsignedByte();
					ipHeader = new byte[20];
					for (int i = 0; i < 20; i++)
						ipHeader[i] = (byte)ds.readUnsignedByte();
					bitsDatosDatagramOriginal = new byte[8];
					for (int i = 0; i < 8; i++)
						bitsDatosDatagramOriginal[i] = (byte)ds.readUnsignedByte();
				}
					;
					break;
				case ICMP.ROUTER_ADVERT: {
					//Opcional, no implementado en este caso ya que es obsoleto
				}
					;
					break;
		
				case ICMP.ROUTER_SOLICIT: {
					//Opcional, no implementado en este caso ya que es obsoleto
				}
					;
					break;
		
				case ICMP.TIME_EXCEEDED: {
					ds = new DataInputStream(new ByteArrayInputStream(data,
							0, 36));
					ds.readUnsignedByte();
					ds.readUnsignedByte();
					ds.readUnsignedShort();
					postHeader = new byte[4];
					for (int i = 0; i < 4; i++)
						postHeader[i] = (byte)ds.readUnsignedByte();
					ipHeader = new byte[20];
					for (int i = 0; i < 20; i++)
						ipHeader[i] = (byte)ds.readUnsignedByte();
					bitsDatosDatagramOriginal = new byte[8];
					for (int i = 0; i < 8; i++)
						bitsDatosDatagramOriginal[i] = (byte)ds.readUnsignedByte();
				}
					;
					break;
				case ICMP.PARAMETER_PROBLEM: {
					ds = new DataInputStream(new ByteArrayInputStream(data,
							0, 36));
					ds.readUnsignedByte();
					ds.readUnsignedByte();
					ds.readUnsignedShort();
					puntero = (byte)ds.readUnsignedByte();
					postHeader3 = new byte[4];
					for (int i = 0; i < 4; i++)
						postHeader3[i] = (byte)ds.readUnsignedByte();
					ipHeader = new byte[20];
					for (int i = 0; i < 20; i++)
						ipHeader[i] = (byte)ds.readUnsignedByte();
					bitsDatosDatagramOriginal = new byte[8];
					for (int i = 0; i < 8; i++)
						bitsDatosDatagramOriginal[i] = (byte)ds.readUnsignedByte();
				}
					;
					break;
				case ICMP.TIMESTAMP: {
					ds = new DataInputStream(new ByteArrayInputStream(data,
							0, 20));
					ds.readUnsignedByte();
					ds.readUnsignedByte();
					ds.readUnsignedShort();
					identificador = new byte[2];
					for (int i = 0; i < 2; i++)
						identificador[i] = (byte)ds.readUnsignedByte();
					nroSecuencia = new byte[2];
					for (int i = 0; i < 2; i++)
						nroSecuencia[i] = (byte)ds.readUnsignedByte();
					marcaTiempoOrigen = new byte[4];
					for (int i = 0; i < 4; i++)
						marcaTiempoOrigen[i] = (byte)ds.readUnsignedByte();
					marcaTiempoRecepcion = new byte[4];
					for (int i = 0; i < 4; i++)
						marcaTiempoRecepcion[i] = (byte)ds.readUnsignedByte();
					marcaTiempoTransmision = new byte[4];
					for (int i = 0; i < 4; i++)
						marcaTiempoTransmision[i] = (byte)ds.readUnsignedByte();
				}
					;
					break;
				case ICMP.TIMESTAMP_REPLY: {
					ds = new DataInputStream(new ByteArrayInputStream(data,
							0, 20));
					ds.readUnsignedByte();
					ds.readUnsignedByte();
					ds.readUnsignedShort();
					identificador = new byte[2];
					for (int i = 0; i < 2; i++)
						identificador[i] = (byte)ds.readUnsignedByte();
					nroSecuencia = new byte[2];
					for (int i = 0; i < 2; i++)
						nroSecuencia[i] = (byte)ds.readUnsignedByte();
					marcaTiempoOrigen = new byte[4];
					for (int i = 0; i < 4; i++)
						marcaTiempoOrigen[i] = (byte)ds.readUnsignedByte();
					marcaTiempoRecepcion = new byte[4];
					for (int i = 0; i < 4; i++)
						marcaTiempoRecepcion[i] = (byte)ds.readUnsignedByte();
					marcaTiempoTransmision = new byte[4];
					for (int i = 0; i < 4; i++)
						marcaTiempoTransmision[i] = (byte)ds.readUnsignedByte();
				}
					;
					break;
				case ICMP.INFORMATION_REQUEST: {
					ds = new DataInputStream(new ByteArrayInputStream(data,
							0, 8));
					ds.readUnsignedByte();
					ds.readUnsignedByte();
					ds.readUnsignedShort();
					identificador = new byte[2];
					for (int i = 0; i < 2; i++)
						identificador[i] = (byte)ds.readUnsignedByte();
					nroSecuencia = new byte[2];
					for (int i = 0; i < 2; i++)
						nroSecuencia[i] = (byte)ds.readUnsignedByte();
				}
					;
					break;
				case ICMP.INFORMATION_REPLY: {
					ds = new DataInputStream(new ByteArrayInputStream(data,
							0, 8));
					ds.readUnsignedByte();
					ds.readUnsignedByte();
					ds.readUnsignedShort();
					identificador = new byte[2];
					for (int i = 0; i < 2; i++)
						identificador[i] = (byte)ds.readUnsignedByte();
					nroSecuencia = new byte[2];
					for (int i = 0; i < 2; i++)
						nroSecuencia[i] = (byte)ds.readUnsignedByte();
				}
					;
					break;
				case ICMP.ADDRESS_MASK_REQUEST: {
					ds = new DataInputStream(new ByteArrayInputStream(data,
							0, 12));
					ds.readUnsignedByte();
					ds.readUnsignedByte();
					ds.readUnsignedShort();
					identificador = new byte[2];
					for (int i = 0; i < 2; i++)
						identificador[i] = (byte)ds.readUnsignedByte();
					nroSecuencia = new byte[2];
					for (int i = 0; i < 2; i++)
						nroSecuencia[i] = (byte)ds.readUnsignedByte();
					subnetAddressMask = new byte[4];
					for (int i = 0; i < 4; i++)
						subnetAddressMask[i] = (byte)ds.readUnsignedByte();
				}
					;
					break;
				case ICMP.ADDRESS_MASK_REPLY: {
					ds = new DataInputStream(new ByteArrayInputStream(data,
							0, 12));
					ds.readUnsignedByte();
					ds.readUnsignedByte();
					ds.readUnsignedShort();
					identificador = new byte[2];
					for (int i = 0; i < 2; i++)
						identificador[i] = (byte)ds.readUnsignedByte();
					nroSecuencia = new byte[2];
					for (int i = 0; i < 2; i++)
						nroSecuencia[i] = (byte)ds.readUnsignedByte();
					subnetAddressMask = new byte[4];
					for (int i = 0; i < 4; i++)
						subnetAddressMask[i] = (byte)ds.readUnsignedByte();
				}
					;
					break;
		
				default: {
					System.out
							.println("No se pudo crear el mensaje. No se encontro el tipo de mensaje. Tipo " + type + " código " + code);
					Principal.addReceived("No se pudo crear el mensaje. No se encontro el tipo de mensaje. Tipo " + type + " código " + code + "\n");
					return;
				}
			}
			this.update();
		} catch (EOFException e) {
		} catch (IOException e) {
		}
	}

	public byte[] toByteArray() {
		this.update();
		return data.clone();
	}

	//Crea el mensaje ICMP a enviar.
	public void update() {
		data[0] = type;
		data[1] = code;
		data[2] = 0;
		data[3] = 0;
		switch (type) {
			case ECHO_REQUEST:
				data[4] = (byte) (idMessage >> 8);
				data[5] = (byte) (idMessage & 0xff);
				data[6] = (byte) (sequenceNumber >> 8);
				data[7] = (byte) (sequenceNumber & 0xff);
				System.arraycopy(datosEcho, 0, data, 8, 20);
				break;
			case ECHO_REPLY:
				data[4] = (byte) (idMessage >> 8);
				data[5] = (byte) (idMessage & 0xff);
				data[6] = (byte) (sequenceNumber >> 8);
				data[7] = (byte) (sequenceNumber & 0xff);
				System.arraycopy(datosEcho, 0, data, 8, 20);
				break;
			case ICMP.DESTINATION_UNREACHABLE: {
				data[4] = 0x00;
				data[5] = 0x00;
				data[6] = 0x00;
				data[7] = 0x00;
				System.arraycopy(ipHeader, 0, data, 8, 20);
				System.arraycopy(bitsDatosDatagramOriginal, 0, data, 28, 8);
			}
				;
				break;
	
			case ICMP.SOURCE_QUENCH: {
				data[4] = 0x00;
				data[5] = 0x00;
				data[6] = 0x00;
				data[7] = 0x00;
				System.arraycopy(ipHeader, 0, data, 8, 20);
				System.arraycopy(bitsDatosDatagramOriginal, 0, data, 28, 8);
			}
				;
				break;
	
			case ICMP.REDIRECT: {
				System.arraycopy(direccionIpGateway, 0, data, 4, 4);
				System.arraycopy(ipHeader, 0, data, 8, 20);
				System.arraycopy(bitsDatosDatagramOriginal, 0, data, 28, 8);
			}
				;
				break;
	
			case ICMP.ROUTER_ADVERT: {
				//Opcional, no implementado en este caso ya que es obsoleto
			}
				;
				break;
	
			case ICMP.ROUTER_SOLICIT: {
				//Opcional, no implementado en este caso ya que es obsoleto
			}
				;
				break;
	
			case ICMP.TIME_EXCEEDED: {
				data[4] = 0x00;
				data[5] = 0x00;
				data[6] = 0x00;
				data[7] = 0x00;
				System.arraycopy(ipHeader, 0, data, 8, 20);
				System.arraycopy(bitsDatosDatagramOriginal, 0, data, 28, 8);
			}
				;
				break;
	
			case ICMP.PARAMETER_PROBLEM: {
				data[4] = puntero;
				data[5] = 0x00;
				data[6] = 0x00;
				data[7] = 0x00;
				System.arraycopy(ipHeader, 0, data, 8, 20);
				System.arraycopy(bitsDatosDatagramOriginal, 0, data, 28, 8);
			}
				;
				break;
	
			case ICMP.TIMESTAMP: {
				data[4] = identificador[0];
				data[5] = identificador[1];
				data[6] = nroSecuencia[0];
				data[7] = nroSecuencia[1];
				System.arraycopy(marcaTiempoOrigen, 0, data, 8, 4);
				System.arraycopy(marcaTiempoRecepcion, 0, data, 12, 4);
				System.arraycopy(marcaTiempoTransmision, 0, data, 16, 4);
			}
				;
				break;
	
			case ICMP.TIMESTAMP_REPLY: {
				data[4] = identificador[0];
				data[5] = identificador[1];
				data[6] = nroSecuencia[0];
				data[7] = nroSecuencia[1];
				System.arraycopy(marcaTiempoOrigen, 0, data, 8, 4);
				System.arraycopy(marcaTiempoRecepcion, 0, data, 12, 4);
				System.arraycopy(marcaTiempoTransmision, 0, data, 16, 4);
			}
				;
				break;
	
			case ICMP.INFORMATION_REQUEST: {
				data[4] = identificador[0];
				data[5] = identificador[1];
				data[6] = nroSecuencia[0];
				data[7] = nroSecuencia[1];
			}
				;
				break;
	
			case ICMP.INFORMATION_REPLY: {
				data[4] = identificador[0];
				data[5] = identificador[1];
				data[6] = nroSecuencia[0];
				data[7] = nroSecuencia[1];
			}
				;
				break;
	
			case ICMP.ADDRESS_MASK_REQUEST: {
				data[4] = identificador[0];
				data[5] = identificador[1];
				data[6] = nroSecuencia[0];
				data[7] = nroSecuencia[1];
				data[8] = subnetAddressMask[0];
				data[9] = subnetAddressMask[1];
				data[10] = subnetAddressMask[2];
				data[11] = subnetAddressMask[3];
			}
				;
				break;

			case ICMP.ADDRESS_MASK_REPLY: {
				data[4] = identificador[0];
				data[5] = identificador[1];
				data[6] = nroSecuencia[0];
				data[7] = nroSecuencia[1];
				data[8] = subnetAddressMask[0];
				data[9] = subnetAddressMask[1];
				data[10] = subnetAddressMask[2];
				data[11] = subnetAddressMask[3];
			}
				;
				break;

			default: {
				System.out
						.println("No se pudo crear el mensaje. No se encontro el tipo de mensaje. Tipo " + type + " código " + code);
				Principal.addReceived("No se pudo crear el mensaje. No se encontro el tipo de mensaje. Tipo " + type + " código " + code + "\n");
				return;
			}
			
			
		}
		checksum = checksum(data.length);
		data[2] = (byte) (checksum >> 8);
		data[3] = (byte) (checksum & 0xff);
	}

	public ICMPMessage reply() {
		ICMPMessage rep = null;
		switch(type) {
			case ECHO_REQUEST:
				rep = new ICMPMessage();
				rep.data = new byte[data.length];
				rep.datosEcho = new byte[20];
				System.arraycopy(datosEcho, 0, rep.datosEcho, 0, 20);
				rep.type = ECHO_REPLY;
				rep.code = code;
				rep.idMessage = idMessage;
				rep.sequenceNumber = sequenceNumber;
				rep.update();
			break;
			case TIMESTAMP:
				rep = new ICMPMessage();
				rep.marcaTiempoRecepcion = new byte[4];
				Long m = System.currentTimeMillis();
				for(int i= 0; i < 4; i++){  
					rep.marcaTiempoRecepcion[3 - i] = (byte)(m >>> (i * 8));  
				}  
				rep.data = new byte[data.length];
				rep.type = TIMESTAMP_REPLY;
				rep.code = code;
				rep.identificador = new byte[2];
				rep.nroSecuencia = new byte[2];
				rep.identificador[0] = identificador[0];
				rep.identificador[1] = identificador[1];
				rep.nroSecuencia[0] = nroSecuencia[0];
				rep.nroSecuencia[1] = nroSecuencia[1];
				rep.marcaTiempoOrigen = new byte[4];
				for(int i= 0; i < 4; i++){  
					rep.marcaTiempoOrigen[i] = marcaTiempoOrigen[i];  
				}  
				try {
					Thread.sleep((int)(Math.random() * 200));
				} catch (InterruptedException e) {
				}
				m = System.currentTimeMillis();
				rep.marcaTiempoTransmision = new byte[4];
				for(int i= 0; i < 4; i++){  
					rep.marcaTiempoTransmision[3 - i] = (byte)(m >>> (i * 8));  
				}  
				rep.update();
			break;
			case INFORMATION_REQUEST:
				rep = new ICMPMessage();
				rep.data = new byte[data.length];
				rep.type = INFORMATION_REPLY;
				rep.code = code;
				rep.identificador = new byte[2];
				rep.nroSecuencia = new byte[2];
				rep.identificador[0] = identificador[0];
				rep.identificador[1] = identificador[1];
				rep.nroSecuencia[0] = nroSecuencia[0];
				rep.nroSecuencia[1] = nroSecuencia[1];
				rep.update();
			break;
			case ADDRESS_MASK_REQUEST:
				rep = new ICMPMessage();
				rep.data = new byte[data.length];
				rep.type = ADDRESS_MASK_REPLY;
				rep.code = code;
				rep.identificador = new byte[2];
				rep.nroSecuencia = new byte[2];
				rep.identificador[0] = identificador[0];
				rep.identificador[1] = identificador[1];
				rep.nroSecuencia[0] = nroSecuencia[0];
				rep.nroSecuencia[1] = nroSecuencia[1];
				rep.subnetAddressMask = ((IP)NetworkProtocols.getProtocol(NetworkProtocols.PROTO_IP)).getNetMask().getBytesMask();
				rep.update();
			break;
			default:
				return null;
		}
		return rep;
	}

	public String toString() {
		String msg = new String("Type: " + type + " ");
		switch (type) {
		case ECHO_REQUEST:
			msg = msg.concat("(ECHO_REQUEST)");
			msg = msg.concat("  -  Codigo: " + code + " ");
			break;
		case ECHO_REPLY:
			msg = msg.concat("(ECHO_REPLY)");
			msg = msg.concat("  -  Codigo: " + code + " ");
			break;
		case ICMP.DESTINATION_UNREACHABLE: 
			msg = msg.concat("(DESTINATION_UNREACHABLE)");
			try{
				msg = msg.concat("  -  Codigo: " + code + " ("+ unreachableLabels[code] +") ");
			} catch (Exception e){
				msg = msg.concat("  -  Codigo: " + code + " (tipo desconocido) ");
			}
			break;

		case ICMP.SOURCE_QUENCH: 
			msg = msg.concat("(SOURCE_QUENCH)");
			msg = msg.concat("  -  Codigo: " + code + " ");
			break;

		case ICMP.REDIRECT: 
			msg = msg.concat("(REDIRECT)");
			try{
				msg = msg.concat("  -  Codigo: " + code + " ("+ redirectLabels[code] +") ");
			} catch (Exception e){
				msg = msg.concat("  -  Codigo: " + code + " (tipo desconocido) ");
			}
			break;

		case ICMP.ROUTER_ADVERT: 
			msg = msg.concat("(ROUTER_ADVERT)");
			msg = msg.concat("  -  Codigo: " + code + " ");
			break;

		case ICMP.ROUTER_SOLICIT: 
			msg = msg.concat("(ROUTER_SOLICIT)");
			msg = msg.concat("  -  Codigo: " + code + " ");
			break;

		case ICMP.TIME_EXCEEDED: 
			msg = msg.concat("(TIME_EXCEEDED)");
			try{
				msg = msg.concat("  -  Codigo: " + code + " ("+ timeExceededLabels[code] +") ");
			} catch (Exception e){
				msg = msg.concat("  -  Codigo: " + code + " (tipo desconocido) ");
			}
			break;

		case ICMP.PARAMETER_PROBLEM: 
			msg = msg.concat("(PARAMETER_PROBLEM)");
			msg = msg.concat("  -  Codigo: " + code + " ");
			break;

		case ICMP.TIMESTAMP: 
			msg = msg.concat("(TIMESTAMP_REQUEST)");
			msg = msg.concat("  -  Codigo: " + code + " ");
			msg = msg.concat("\nIdentificador: " + toByteValueString(identificador));
			msg = msg.concat(" nroSecuencia: " + toByteValueString(nroSecuencia));
			msg = msg.concat(" marcaTiempoOrigen: " + toByteValueString(marcaTiempoOrigen));
			msg = msg.concat(" marcaTiempoRecepcion: " + toByteValueString(marcaTiempoRecepcion));
			msg = msg.concat(" marcaTiempoTransmision: " + toByteValueString(marcaTiempoTransmision));
			break;

		case ICMP.TIMESTAMP_REPLY: 
			msg = msg.concat("(TIMESTAMP_REPLY)");
			msg = msg.concat("  -  Codigo: " + code + " ");
			msg = msg.concat("\nIdentificador: " + toByteValueString(identificador));
			msg = msg.concat(" nroSecuencia: " + toByteValueString(nroSecuencia));
			msg = msg.concat(" marcaTiempoOrigen: " + toByteValueString(marcaTiempoOrigen));
			msg = msg.concat(" marcaTiempoRecepcion: " + toByteValueString(marcaTiempoRecepcion));
			msg = msg.concat(" marcaTiempoTransmision: " + toByteValueString(marcaTiempoTransmision));
			break;

		case ICMP.INFORMATION_REQUEST: 
			msg = msg.concat("(INFORMATION_REQUEST)");
			msg = msg.concat("  -  Codigo: " + code + " ");
			break;

		case ICMP.INFORMATION_REPLY: 
			msg = msg.concat("(INFORMATION_REPLY)");
			msg = msg.concat("  -  Codigo: " + code + " ");
			break;

		case ICMP.ADDRESS_MASK_REQUEST: 
			msg = msg.concat("(ADDRESS_MASK_REQUEST)");
			msg = msg.concat("  -  Codigo: " + code + " ");
			break;

		case ICMP.ADDRESS_MASK_REPLY: 
			msg = msg.concat("(ADDRESS_MASK_REPLY)");
			msg = msg.concat("  -  Codigo: " + code + " ");
			msg = msg.concat("\nMascara: " + toByteValueString(subnetAddressMask));
			break;

		default: 
			return "No se pudo crear el mensaje. No se encontro el tipo de mensaje. Tipo " + type + " código " + code;
		}

		msg = msg.concat("\n Paquete: " + toByteValueString(data));
		return msg;
	}

	int checksum(int nr) {
		DataInputStream ds = new DataInputStream(new ByteArrayInputStream(data,
				0, nr));
		int sum = 0;
		boolean theEnd = false;
		while (!theEnd) {
			try {
				sum = sum + ds.readUnsignedShort();
			} catch (EOFException e) {
				theEnd = true;
			} catch (IOException e) {
				return -1;
			}
		}
		sum = (sum >> 16) + (sum & 0xffff);
		sum += (sum >> 16);
		return (~sum & 0xffff);
	}

	private boolean validChecksum(){
		byte[] auxData = new byte[data.length];
		System.arraycopy(data, 0, auxData, 0, data.length);
		DataInputStream ds = null;
		int sumAct;
		ds = new DataInputStream(new ByteArrayInputStream(auxData,
				2, 4));
		try {
			sumAct = (int)ds.readUnsignedShort();
		} catch (IOException e) {
			e.printStackTrace();
			return false;
		}
		auxData[2] = 0x00;
		auxData[3] = 0x00;
		ds = new DataInputStream(new ByteArrayInputStream(auxData,
				0, auxData.length));
		int sum = 0;
		boolean theEnd = false;
		while (!theEnd) {
			try {
				sum = sum + ds.readUnsignedShort();
			} catch (EOFException e) {
				theEnd = true;
			} catch (IOException e) {
				return false;
			}
		}
		sum = (sum >> 16) + (sum & 0xffff);
		sum += (sum >> 16);
		int res = (~sum & 0xffff);
		if (sumAct == res)
			return true;
		else
			return false;
	}
	
	public String toByteValueString(byte[] val){
		String ret = new String();
		for (int i = 0; i < val.length; i++)
			ret = ret.concat(String.format("%X", val[i]).toUpperCase());
		return ret;
	}
}
