package NetworkProtocols.IP.RoutingTable;

import Interface.Interface;
import NetworkProtocols.IP.Address.IpAddress;
import NetworkProtocols.IP.Address.Mask;

// Representa entradas en la tabla de ruteo. La ruta por defecto se representa con una
// direccion de destino de red 0 y una mascara 0
public class RoutingEntry {
	private IpAddress Destination; // Red de destino
	private Mask mask; // Mascara de red
	private boolean direct; // Ruteo directo o a través de un router (D=true)
	private IpAddress nextHop; // Dirección del próximo router
	private Interface interface1; // Interfaz (por ejemplo, eth0)

	RoutingEntry(IpAddress dst, Mask m, boolean type, IpAddress nh,
			Interface ifc) {
		Destination = dst;
		mask = m;
		direct = type;
		nextHop = nh;
		interface1 = ifc;
	}

	// Devuelve la cantidad de bits de matching de la entrada con la direccion
	// parametro
	// Si no hay matching, devuelve -1. Si no hay matching con ninguna entrada
	// pero existe
	// default route, devuelve matching 0 (notar que el chequeo da true para
	// default)
	public int getmatching(IpAddress dest) {
		int mat = -1;
		if ((dest.toInt() & mask.toInt()) == Destination.toInt())
			mat = mask.toPrefix();
		return mat;
	}

	// Devuelve true si es la defayult entry
	public boolean isdefault() {
		if ((Destination.toInt() == 0) && (mask.toInt() == 0))
			return true;
		else
			return false;
	}

	// Devuelve true si la entrada es igual
	public boolean equals(IpAddress dst, Mask m) {
		if (Destination.equals(dst) && mask.equals(m))
			return true;
		else
			return false;
	}

	// Convierte la entrada en String
	public String toString() {
		String di = "I";
		if (direct)
			di = "D";
		String st = "(" + Destination.toString() + " , " + mask.toString()
				+ " , " + di + " , " + nextHop.toString() + " , " + interface1
				+ ")";
		return (st);
	}

	public IpAddress getDestination() {
		return Destination;
	}

	public IpAddress getNextHop() {
		return nextHop;
	}

	public Mask getMask() {
		return mask;
	}

	public Interface getInterface() {
		return interface1;
	}

	public boolean getType() {
		return direct;
	}

}
