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

	public ICMP(Interfaces ifs, NetworkProtocols nt) throws NodeException {
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

	// //////////////////////////////////////////////////////////////

	public ICMP(IP ip) {
		this.ip = ip;
	}

	public boolean handle(eventoN3 p) { // Es invocado cuando IP recibe un
										// datagram que corresponde a ICMP
		ICMPMessage icmpp = null;
		try {
			icmpp = new ICMPMessage(p);
		} catch (MalformedPacketException e) {
			return false; // El paquete recibido estaba mal formado o no era un
							// paquete ICMP
		}
		System.out.println("ICMP  " + icmpp.toString());
		switch (icmpp.type) {
		case ICMPMessage.ECHO_REQUEST: {
			System.out
					.println("Se encontro un paquete ICMP ECHO REQUEST proveniente de "
							+ icmpp.getSourceAdrr());
			System.out.println("Paquete ICMP: " + icmpp.toString());
			ICMPMessage rp = icmpp.reply();
			System.out.println("REP " + rp.toString());
			try {
				Datagram datagram = new Datagram((byte[]) p.getInfo());
				datagram.setsourceAddress(datagram.getDestAddress());
				datagram.setDestAddress(icmpp.getSourceAdrr());
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
							+ icmpp.getSourceAdrr());
			System.out.println("Paquete ICMP: " + icmpp.toString());
		}
			;
			break;

		case ICMPMessage.DESTINATION_UNREACHABLE: {
			System.out
					.println("Se encontro un paquete ICMP DESTINATION UNREACHABLE proveniente de "
							+ icmpp.getSourceAdrr());
			System.out.println("Paquete ICMP: " + icmpp.toString());

		}
			;
			break;

		case ICMPMessage.SOURCE_QUENCH: {
			System.out
					.println("Se encontro un paquete ICMP SOURCE QUENCH proveniente de "
							+ icmpp.getSourceAdrr());
			System.out.println("Paquete ICMP: " + icmpp.toString());

		}
			;
			break;

		case ICMPMessage.REDIRECT: {
			System.out
					.println("Se encontro un paquete ICMP REDIRECT proveniente de "
							+ icmpp.getSourceAdrr());
			System.out.println("Paquete ICMP: " + icmpp.toString());

		}
			;
			break;

		case ICMPMessage.ROUTER_ADVERT: {
			System.out
					.println("Se encontro un paquete ICMP ROUTER ADVERT proveniente de "
							+ icmpp.getSourceAdrr());
			System.out.println("Paquete ICMP: " + icmpp.toString());

		}
			;
			break;

		case ICMPMessage.ROUTER_SOLICIT: {
			System.out
					.println("Se encontro un paquete ICMP ROUTER SOLICIT proveniente de "
							+ icmpp.getSourceAdrr());
			System.out.println("Paquete ICMP: " + icmpp.toString());

		}
			;
			break;

		case ICMPMessage.TIME_EXCEEDED: {
			System.out
					.println("Se encontro un paquete ICMP TIME EXCEEDED proveniente de "
							+ icmpp.getSourceAdrr());
			System.out.println("Paquete ICMP: " + icmpp.toString());

		}
			;
			break;

		case ICMPMessage.PARAMETER_PROBLEM: {
			System.out
					.println("Se encontro un paquete ICMP PARAMETER PROBLEM proveniente de "
							+ icmpp.getSourceAdrr());
			System.out.println("Paquete ICMP: " + icmpp.toString());

		}
			;
			break;

		case ICMPMessage.TIMESTAMP: {
			System.out
					.println("Se encontro un paquete ICMP TIMESTAMP proveniente de "
							+ icmpp.getSourceAdrr());
			System.out.println("Paquete ICMP: " + icmpp.toString());

		}
			;
			break;

		case ICMPMessage.TIMESTAMP_REPLY: {
			System.out
					.println("Se encontro un paquete ICMP TIMESTAMP REPLY proveniente de "
							+ icmpp.getSourceAdrr());
			System.out.println("Paquete ICMP: " + icmpp.toString());

		}
			;
			break;

		case ICMPMessage.INFORMATION_REQUEST: {
			System.out
					.println("Se encontro un paquete ICMP INFORMATION REQUEST proveniente de "
							+ icmpp.getSourceAdrr());
			System.out.println("Paquete ICMP: " + icmpp.toString());

		}
			;
			break;

		case ICMPMessage.INFORMATION_REPLY: {
			System.out
					.println("Se encontro un paquete ICMP INFORMATION REPLY proveniente de "
							+ icmpp.getSourceAdrr());
			System.out.println("Paquete ICMP: " + icmpp.toString());

		}
			;
			break;

		default: {
			System.out.print("El codigo del paquete ICMP es invalido. Tipo: "
					+ icmpp.type);
			try {
				System.out.print(" tipo no implementado. Etiqueta: "
						+ ICMPInterface.typeLabels[icmpp.type] + "/n");
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
		case ICMP.ECHO_REPLY: {
			ping(dest);
		}
			;
			break;

		case ICMP.DESTINATION_UNREACHABLE: {

		}
			;
			break;

		case ICMP.SOURCE_QUENCH: {

		}
			;
			break;

		case ICMP.REDIRECT: {

		}
			;
			break;

		case ICMP.ECHO_REQUEST: {

		}
			;
			break;

		case ICMP.ROUTER_ADVERT: {

		}
			;
			break;

		case ICMP.ROUTER_SOLICIT: {

		}
			;
			break;

		case ICMP.TIME_EXCEEDED: {

		}
			;
			break;

		case ICMP.PARAMETER_PROBLEM: {

		}
			;
			break;

		case ICMP.TIMESTAMP: {

		}
			;
			break;

		case ICMP.TIMESTAMP_REPLY: {

		}
			;
			break;

		case ICMP.INFORMATION_REQUEST: {

		}
			;
			break;

		case ICMP.INFORMATION_REPLY: {

		}
			;
			break;

		default: {
			System.out
					.println("El envio de mensaje ICMP fallo! No se encontro el tipo de mensaje "
							+ type);
		}
		}

	}

	public void ping(IpAddress dest) {
		ICMPMessage icmpp = new ICMPMessage();
		icmpp.data = new byte[60];
		icmpp.type = (byte) 8;
		icmpp.code = 0;
		icmpp.idMessage = 1000;
		icmpp.sequenceNumber = 1;
		icmpp.update();
		try {
			// CREAR EL DATAGRAM A MANDAR CON SUS RESPECTIVOS CAMPOS.
			Datagram datagram = new Datagram(new byte[220]); // DATAGRAM que va
																// a tirar error
																// porq esta
																// vacio!!!

			datagram.setDestAddress(dest);
			datagram.setData(icmpp.toByteArray());
			datagram.genChecksum();
			eventoN3 evReq = new eventoN3(eventoN3.SEND, datagram);
			ip.addLoc(evReq);
		} catch (Exception e) {
			System.out
					.println("Error en la generacion y envio de ECHO REPLY. Posible ruta no encontrada.");
		}
	}

	// Procesa un requerimiento de envio u otro del nivel superior. Recibe en un
	// byte la info de
	// control de l ainterfa, y un objeto conteniendo la idu
	public void receive_loc(eventoN3 pack) {
		System.out.println("Encontro requerimiento en cola local");
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
			break;
		}
	}

	// Procesa un datagram remoto o alguan indicacion del nivel inferior. Esto
	// esta en buffRem
	// El objeto contiene un datagram, y podria contener ademas otras cosas que
	// seran definidas
	// mas adelante (por ejemplo, primitivas indication con las cuales la
	// interfaz le informe que
	// esta caida, etc
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
			break;
		}
	}
}