package NetworkProtocols.ARP;

import Exceptions.NodeException;
import Interface.Interfaces;
import NetworkProtocols.NetworkProtocols;
import NetworkProtocols.ProtocolInterface;
import NetworkProtocols.eventoN3;

//

public class ARP implements ProtocolInterface {

	public ARP(Interfaces ifs, NetworkProtocols nt) throws NodeException {
	}

	// Metodo invocado por la interfaz para agregar un evento a la cola remota
	public void addRem(eventoN3 x) {
	}

	// Metodo invocado por el nivel superior para agregar un evento a la cola
	// local
	public void addLoc(eventoN3 x) {
	}
}
