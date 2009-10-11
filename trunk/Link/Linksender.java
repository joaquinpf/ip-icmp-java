package Link;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;

public class Linksender extends Thread {

	// Variables relativas al socket UDP de soporte de transmision
	static DatagramSocket socket; // Socket de envio
	static int remport = 0; // Port del socket de envio
	static InetAddress remaddr = null; // IP del socket de envio
	Utils.Queue TQueue; // Cola de transmision de frames
	static Link link;

	public Linksender(Link lnk, DatagramSocket sock, InetAddress dip, int dport) {
		socket = sock;
		this.link = lnk;
		remport = dport;
		remaddr = dip;
		// TQueue = new Utils.Queue();
		TQueue = null;
		this.start();
	}

	// Agrega un frame a la cola de frames a enviar
	// public void addFrame(byte[] frame) {
	// TQueue.pushBack(frame);
	// }

	public void setQueue(Utils.Queue T) {
		TQueue = T;
	}

	// EDnvia un frame a la direccion remota esto deberia ser private, es solo
	// prueba
	static void enviar(byte[] frame) {
		try {
			socket.send(new DatagramPacket(frame, frame.length, remaddr,
					remport));
		} catch (IOException e) {
			// throw new NodeException(NodeException.EXCEPCION_ENVIO);
			System.out.println("Excepcion al enviar");
		}
	}

	// En el ciclo, envia os frames
	public void run() {
		while (true) {
			if (TQueue != null) {
				while (TQueue.size() != 0) {
					byte[] frame = (byte[]) TQueue.peekFront();
					enviar(frame);
					TQueue.popFront();
				}
			}
		}
	}
}
