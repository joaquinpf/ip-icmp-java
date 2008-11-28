package NetworkProtocols.ICMP;

import java.io.*;

import Exceptions.MalformedPacketException;
import NetworkProtocols.*;
import NetworkProtocols.IP.Datagram;
import NetworkProtocols.IP.Address.IpAddress;


class ICMPMessage implements ICMPInterface
{
	public int HEADER_LENGTH = 8;
	int protocol = 0x1;	// Setea el tipo de protocolo al tipo de ICMP
	byte type;			// Tipo
	byte code;			// Codigo
	int checksum;		// Checksum del header mas los datos
	int idMessage;		// Id del mansaje (utilizado para el Echo, Timestamp o Information)
	int sequenceNumber;	// Numero de secuencia (utilizado para el Echo, Timestamp o Information)
	int gatewayAddress;	// Direccion internet del Gateway (usada para Redirect Message)
	byte pointer;		// Puntero utilizado para Parameters Problem Message
	byte[] data;		// Datos del paquete ICMP

	//Datos especificos del mensaje ICMP
	byte[] ipHeader;					//20 bytes
	byte[] postHeader;					//4 bytes
	byte[] postHeader3;					//3 bytes
	byte[] puntero;						//1 byte
	byte[] bitsDatosDatagramOriginal;	//8 bytes
	byte[] identificador;				//2 bytes
	byte[] nroSecuencia;				//2 bytes
	byte[] marcaTiempoOrigen;			//4 bytes
	byte[] marcaTiempoRecepcion;		//4 bytes
	byte[] marcaTiempoTransmision;		//4 bytes
	byte[] direccionIpGateway;			//4 bytes
	byte[] datosEcho;					//20 bytes
	

	public ICMPMessage()
	{
		data = null;
	}

	public ICMPMessage(byte[] raw) throws MalformedPacketException
	{
		data = raw;
		parse();
	}

	public ICMPMessage(eventoN3 p) throws MalformedPacketException
	{
		Datagram datagram = new Datagram((byte[])p.getInfo());
		data = datagram.getData();
		parse();
	}

	private void parse() throws MalformedPacketException
	{
		DataInputStream ds = new DataInputStream(new ByteArrayInputStream(data, 0, 8));
		try 
		{ 
			type = (byte)ds.readUnsignedByte();
			code = (byte)ds.readUnsignedByte();
			checksum = ds.readUnsignedShort();
			if ((type == ECHO_REQUEST) || (type == ECHO_REPLY))
			{
				idMessage = ds.readUnsignedShort();
				sequenceNumber = ds.readUnsignedShort();
			}
		}
		catch (EOFException e) {}
		catch (IOException e) {}
		if (checksum(data.length) != 0)
			throw new MalformedPacketException();
	}

	public byte[] toByteArray(){
		return data.clone();
	}
	
	public IpAddress getSourceAdrr(){
		Datagram datagram = new Datagram(data);
		return datagram.getSourceAddr();
	}
	
	public void update()
	{
		data[0] = type;
		data[1] = code;
		data[2] = 0;
		data[3] = 0;
		if ((type == ECHO_REQUEST) || ((type == ECHO_REPLY)))
		{ 
			data[4] = (byte)(idMessage >> 8);
			data[5] = (byte)(idMessage & 0xff);
			data[6] = (byte)(sequenceNumber >> 8);
			data[7] = (byte)(sequenceNumber & 0xff);
		}
		checksum = checksum(data.length);
		data[2] = (byte)(checksum >> 8);
		data[3] = (byte)(checksum & 0xff);
	}

	public ICMPMessage reply()
	{
		if (type == ECHO_REQUEST)
		{
			ICMPMessage rep = new ICMPMessage();
			rep.data = new byte[data.length];
			System.arraycopy(data, 0, rep.data, 0, data.length);
			rep.type = ECHO_REPLY;
			rep.code = code;
			rep.idMessage = idMessage;
			rep.sequenceNumber = sequenceNumber;
			rep.update();
			return rep;
		}
		return null;
	}

	public String toString()
	{
		return type+"/"+code+"/"+idMessage+"/"+sequenceNumber;
	}

	int checksum(int nr)
	{
		DataInputStream ds = new DataInputStream(new ByteArrayInputStream(data, 0, nr));
		int sum = 0;
		boolean theEnd = false;
		while (!theEnd)
		{ 
			try { sum = sum + ds.readUnsignedShort();}
			catch (EOFException e) {theEnd = true;}
			catch (IOException e) {return -1;}
		}
		sum = (sum >> 16)+ (sum & 0xffff);
		sum += (sum >> 16); 
		return (~sum & 0xffff);
	}

}
