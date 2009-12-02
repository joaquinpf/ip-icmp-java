package Interface;

import Exceptions.NodeException;
import NetworkProtocols.NetworkProtocols;
import NetworkProtocols.ProtocolInterface;
import NetworkProtocols.eventoN3;
import NetworkProtocols.IP.Address.IpAddress;
import NetworkProtocols.IP.Address.Mask;
import Utils.Queue;

// Ver Linux: \linux-2.6.11.6\include\linux\if.h  por contenido a definir
public class Interface {

	// Map<int, Object> protocols = null; // Tabla con los protocolos

	// FLAGS, ver si es necesario o si agregar mas
	boolean UP; // True si está operando
	boolean BROADCAST; // True si soporta broadcasts
	boolean PTOP; // True si es punto a punto

	String name; // Nombre de la interfaz
	String hwAddr; // Direccion de hardware (por ejemplo Ethernet, en este caso
					// genérico)
	// es un string con dir_ip:port o algo similar, es en realidad la dir UDP
	IpAddress ipAddr; // Direccion IP
	Mask netMask; // Mascara de red
	Integer MTU; // MTU de la interfaz

	Queue sendQueue; // Cola de frames a enviar, accedida po Link

	// Crea la interfaz
	public Interface(String nm, IpAddress addr, Mask msk, Integer mtu /* ,Link k */)
			throws NodeException {
		name = nm;
		ipAddr = addr;
		netMask = msk;
		MTU = mtu;
		sendQueue = new Utils.Queue();
		System.out.println("Creada Interfaz:" + name + " IP: "
				+ ipAddr.toString() + " Mask: " + netMask.toString()
				+ " MTU:  " + MTU);
	}

	// Recepcion de un frame desde el link. Aqui se desencapsula (por ejemplo se
	// saca el encabezamienyo ethernet
	// y en funcon del tipo de frame se invoca a arp, ip, etc. La
	// desencapsulacion en realidad
	// no se hace porque ese nivel no interesa.
	// Se pasa a IP (por ejemplo) el datagram mas info acerca de pro que
	// interfaz (esta) llego
	public void receive(byte[] b) {
		byte[] rec;
		int proto = (int) (b[0] << 8);
		proto = proto + (int) (b[1]);
		int y = b.length - 2;
		rec = new byte[y];
		for (int i = 0; i < y; i++)
			rec[i] = b[i + 2];
		// Invoca al protocolo correspondiente. No seria necesario codigo para
		// cada protocolo, siempre
		// que un protocolo como el ARP respetara la interfaz
		switch (proto) {
		case NetworkProtocols.PROTO_IP:
			System.out.println("Protocolo: " + proto);
			eventoN3 n = new eventoN3(eventoN3.INFO_RECEIVED, (Object) rec);
			((ProtocolInterface) (NetworkProtocols.getProtocol(new Integer(
					proto)))).addRem(n);
			// ProtocolInterface p =
			// (ProtocolInterface)(Interfaces.getProtocol(new Integer(proto)));
			// p.addRem(rec);
			// IP.addRem(rec);
			break;
		case NetworkProtocols.PROTO_ARP:
			System.out.println("Protocolo: " + proto);
			break;
		case NetworkProtocols.PROTO_ICMP:
			System.out.println("Protocolo: " + proto);
			eventoN3 n2 = new eventoN3(eventoN3.INFO_RECEIVED, (Object) rec);
			((ProtocolInterface) (NetworkProtocols.getProtocol(new Integer(
					proto)))).addRem(n2);
			break;
		default:
			System.out.println("Protocolo de N3 no reconocido");
		}

	}

	// Agrega un segmento a la cola de segmentos a ser enviados por el Link
	// El frame recibido tiene ya la encapsulacion a nivel link, realizada por
	// la interfaz
	// En nuestro caso, solo interesa el campo que indica qué protocolo es el
	// que se encapsula en
	// el frame del link layer
	public void send(int protocol, byte[] frame) {
		byte[] nf = new byte[frame.length + 2];
		int l;
		l = frame.length;
		for (int i = 2; i < l + 2; i++)
			nf[i] = frame[i - 2];
		nf[0] = (byte) (protocol >> 8 & 0x000000ff);
		nf[1] = (byte) (protocol & 0x000000ff);
		sendQueue.pushBack(nf);
	}

	// Devuelve la cola de frames a enviar
	public Queue getsendQueue() {
		return sendQueue;
	}

	// Representacion visible de la interfaz
	public String toString() {
		String rt = name + " IP:" + ipAddr.toString() + " Mask:"
				+ netMask.toString() + " MTU:" + MTU;
		return rt;
	}

	public String getName() {
		return name;
	}

	public IpAddress getIPAddress() {
		return ipAddr;
	}

	public Integer getMTU() {
		return MTU;
	}

	public void setMTU(Integer mTU) {
		MTU = mTU;
	}
	
	public Mask getMask(){
		return netMask;
	}
}
