import Link.Link;
import NetworkProtocols.IP.*;
import NetworkProtocols.IP.Address.IpAddress;
import NetworkProtocols.IP.Address.Mask;
import Exceptions.AddressException;
import Exceptions.NodeException;
import Interface.*;

public class Main {

	/**
	 * @param args
	 */

	Datagram d;

	public static void main(String[] args) {
		/*
		 * ESTA ES UNA SECUENCIA PARECIDA A LA QUE TENDRIA Q HABER
		 * PARA Q FUNCIONE, AHORA ME ACORDE Q HABIA Q TRATAR CADA NODO
		 * COMO UNA MAQUINA VIRTUAL APARTE... Y SINO PODEMOS HACERLO DESDE
		 * ESTA MISMA PERO HAY QUE USAR THREADS Y QUIZAS SE COMPLIQUE UN POCO
		 * 
		 */
		
		Link l1 = null;
		Link l1r = null;
		Link l2 = null;
		Link l2r = null;
		IpAddress addr1 = null;
		IpAddress addr2 = null;
		IpAddress addr3 = null;
		IpAddress addr4 = null;
		Interface ifz1 = null;
		Interface ifz2 = null;
		Interface ifz3 = null;
		Interface ifz4 = null;
		Mask mask = null;
		try {
			mask = new Mask(8);
		} catch (AddressException e1) {
			System.out.println("Error al crear la mascara");
			e1.printStackTrace();
		}
		try {
			l1 = new Link("192.168.159.4", "804", "192.168.159.3", "803");
		} catch (NodeException e) {
			System.out.println("Error al crear el link 1");
			e.printStackTrace();
		}

		try {
			l1r = new Link("192.168.159.3", "803", "192.168.159.4", "804");
		} catch (NodeException e) {
			System.out.println("Error al crear el link 1r");
			e.printStackTrace();
		}

		try {
			l2 = new Link("192.168.159.1", "801", "192.168.159.2", "802");
		} catch (NodeException e) {
			System.out.println("Error al crear el link 2");
			e.printStackTrace();
		}

		try {
			l2r = new Link("192.168.159.2", "802", "192.168.159.1", "801");
		} catch (NodeException e) {
			System.out.println("Error al crear el link 2r");
			e.printStackTrace();
		}
		
		try {
			addr1 = new IpAddress("192p168p159p1");
		} catch (AddressException e) {
			System.out.println("Error al crear la ip 1");
			e.printStackTrace();
		}
		try {
			addr2 = new IpAddress("192p168p159p2");
		} catch (AddressException e) {
			System.out.println("Error al crear la ip 2");
			e.printStackTrace();
		}
		try {
			addr3 = new IpAddress("192p168p159p3");
		} catch (AddressException e) {
			System.out.println("Error al crear la ip 3");
			e.printStackTrace();
		}
		try {
			addr4 = new IpAddress("192p168p159p4");
		} catch (AddressException e) {
			System.out.println("Error al crear la ip 4");
			e.printStackTrace();
		}

		try {
			ifz1 = new Interface("ifz1", addr1, mask, 280);
		} catch (NodeException e) {
			System.out.println("Error al crear la interface 1");
			e.printStackTrace();
		}
		try {
			ifz2 = new Interface("ifz2", addr2, mask, 280);
		} catch (NodeException e) {
			System.out.println("Error al crear la interface 2");
			e.printStackTrace();
		}
		try {
			ifz3 = new Interface("ifz3", addr3, mask, 280);
		} catch (NodeException e) {
			System.out.println("Error al crear la interface 3");
			e.printStackTrace();
		}
		try {
			ifz4 = new Interface("ifz4", addr4, mask, 280);
		} catch (NodeException e) {
			System.out.println("Error al crear la interface 4");
			e.printStackTrace();
		}
		
		l1.setInterface(ifz4);
		l1r.setInterface(ifz3);
		l2.setInterface(ifz1);
		l2r.setInterface(ifz2);
		
		
	}

}
