package NetworkProtocols.ICMP;

import java.io.*;

import Exceptions.MalformedPacketException;
import NetworkProtocols.*;
import NetworkProtocols.IP.Datagram;
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
//	byte[] postHeader; // 4 bytes     son todos ceros
//	byte[] postHeader3; // 3 bytes    son todos ceros
	byte puntero; // 1 byte
	byte[] bitsDatosDatagramOriginal; // 8 bytes
	byte[] identificador; // 2 bytes
	byte[] nroSecuencia; // 2 bytes
	byte[] marcaTiempoOrigen; // 4 bytes
	byte[] marcaTiempoRecepcion; // 4 bytes
	byte[] marcaTiempoTransmision; // 4 bytes
	byte[] direccionIpGateway; // 4 bytes
	byte[] datosEcho; // 20 bytes

	public ICMPMessage() {
		data = null;
	}

	public ICMPMessage(byte[] raw) throws MalformedPacketException {
		data = raw;
		parse();
	}

	public ICMPMessage(eventoN3 p) throws MalformedPacketException {
		Datagram datagram = new Datagram((byte[]) p.getInfo());
		data = datagram.getData();
		parse();
	}

	private void parse() throws MalformedPacketException {
		DataInputStream ds = new DataInputStream(new ByteArrayInputStream(data,
				0, 8));
		try {
			type = (byte) ds.readUnsignedByte();
			code = (byte) ds.readUnsignedByte();
			checksum = ds.readUnsignedShort();
			if ((type == ECHO_REQUEST) || (type == ECHO_REPLY)) {
				idMessage = ds.readUnsignedShort();
				sequenceNumber = ds.readUnsignedShort();
			}
		} catch (EOFException e) {
		} catch (IOException e) {
		}
		if (checksum(data.length) != 0)
			throw new MalformedPacketException();
	}

	public byte[] toByteArray() {
		this.update();
		return data.clone();
	}

	public IpAddress getSourceAdrr() {
		Datagram datagram = new Datagram(data);
		return datagram.getSourceAddress();
	}

	//Crea el mensaje ICMP a enviar. Lo 
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
				if (type == ECHO_REQUEST) //Completamos los datos del mensaje
					System.arraycopy(datosEcho, 0, data, 8, 20);
				break;
			case ECHO_REPLY:
				data[4] = (byte) (idMessage >> 8);
				data[5] = (byte) (idMessage & 0xff);
				data[6] = (byte) (sequenceNumber >> 8);
				data[7] = (byte) (sequenceNumber & 0xff);
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
				//Mirar porq falta el detalle del mensaje
			}
				;
				break;
	
			case ICMP.ROUTER_SOLICIT: {
				//Mirar porq falta el detalle del mensaje
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
	
			default: {
				System.out
						.println("No se pudo crear el mensaje. No se encontro el tipo de mensaje. Tipo " + type + " código " + code);
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
				rep.marcaTiempoRecepcion = new byte[4];
				Long m = System.currentTimeMillis();
				for(int i= 0; i < 4; i++){  
					rep.marcaTiempoRecepcion[3 - i] = (byte)(m >>> (i * 8));  
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
				rep.type = TIMESTAMP_REPLY;
				rep.code = code;
				rep.identificador = new byte[2];
				rep.nroSecuencia = new byte[2];
				rep.identificador[0] = identificador[0];
				rep.identificador[1] = identificador[1];
				rep.nroSecuencia[0] = nroSecuencia[0];
				rep.nroSecuencia[1] = nroSecuencia[1];
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
			msg = msg.concat("ECHO_REQUEST");
			break;
		case ECHO_REPLY:
			msg = msg.concat("ECHO_REPLY");
			break;
		case ICMP.DESTINATION_UNREACHABLE: 
			msg = msg.concat("DESTINATION_UNREACHABLE");
			break;

		case ICMP.SOURCE_QUENCH: 
			msg = msg.concat("SOURCE_QUENCH");
			break;

		case ICMP.REDIRECT: 
			msg = msg.concat("REDIRECT");
			break;

		case ICMP.ROUTER_ADVERT: 
			msg = msg.concat("ROUTER_ADVERT");
			break;

		case ICMP.ROUTER_SOLICIT: 
			msg = msg.concat("ROUTER_SOLICIT");
			break;

		case ICMP.TIME_EXCEEDED: 
			msg = msg.concat("TIME_EXCEEDED");
			break;

		case ICMP.PARAMETER_PROBLEM: 
			msg = msg.concat("PARAMETER_PROBLEM");
			break;

		case ICMP.TIMESTAMP: 
			msg = msg.concat("TIMESTAMP_REQUEST");
			break;

		case ICMP.TIMESTAMP_REPLY: 
			msg = msg.concat("TIMESTAMP_REPLY");
			break;

		case ICMP.INFORMATION_REQUEST: 
			msg = msg.concat("INFORMATION_REQUEST");
			break;

		case ICMP.INFORMATION_REPLY: 
			msg = msg.concat("INFORMATION_REPLY");
			break;

		default: 
			return "No se pudo crear el mensaje. No se encontro el tipo de mensaje. Tipo " + type + " código " + code;
		}
		msg = msg.concat("  -  Codigo: " + code + " \n Paquete: " + toByteValueString(data));
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

	public byte[] getMessage(){
		return toByteArray();
	}
	
	public String toByteValueString(byte[] val){
		String ret = new String();
		for (int i = 0; i < val.length; i++)
			ret = ret.concat(String.format("%x", val[i]).toUpperCase());
		return ret;
	}
}
