package NetworkProtocols.IP.RoutingTable;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import Exceptions.AddressException;
import Exceptions.NodeException;
import Interface.Interface;
import NetworkProtocols.IP.Address.IpAddress;
import NetworkProtocols.IP.Address.Mask;

public class RoutingTable {
	Map<Integer, RoutingEntry> routeT = null; // Tabla de ruteo, compuesta por
												// entradas
	Integer t = 1;

	public RoutingTable() {
		routeT = new HashMap<Integer, RoutingEntry>();
	}

	// Agrega una entrada, si ya existe (es decir existe dest y mask), la
	// elimina y la
	// reemplaza por la nueva
	public void addEntry(IpAddress dst, Mask m, boolean type, IpAddress nh,
			Interface ifc) {
		Integer rem = null;
		Set<Integer> keySet = routeT.keySet();
		for (Integer elementKey : keySet) {
			RoutingEntry i = routeT.get(elementKey);
			if (i.equals(dst, m)) {
				rem = elementKey;
				break;
			}
		}
		if (rem != null)
			routeT.remove(rem);
		routeT.put(t, new RoutingEntry(dst, m, type, nh, ifc));
		t++;
	}

	// Agrega una entrada por defecto, si ya existe la entrada por defecto, se
	// sobreescribe la interfaz
	// y el next hop
	public void addDefault(IpAddress nh, Interface ifc) throws NodeException {
		Integer rem = null;
		Set<Integer> keySet = routeT.keySet();
		for (Integer elementKey : keySet) {
			RoutingEntry i = routeT.get(elementKey);
			if (i.isdefault()) {
				rem = elementKey;
				break;
			}
		}
		if (rem != null)
			routeT.remove(rem);
		try {
			routeT.put(t, new RoutingEntry(new IpAddress(new Integer(0)),
					new Mask(new Integer(0)), false, nh, ifc));
			t++;
		} catch (AddressException e) {
		}
	}

	// Elimina una entrada, dada su dest y mask. Si la entrada no esta, ignora
	public void delRoute(IpAddress dst, Mask m) {
		Integer rem = null;
		Set<Integer> keySet = routeT.keySet();
		for (Integer elementKey : keySet) {
			RoutingEntry i = routeT.get(elementKey);
			if (i.getDestination().equals(dst) && i.getMask().equals(m)) {
				rem = elementKey;
				break;
			}
		}
		if (rem != null)
			routeT.remove(rem);
	}

	// Dada una direccion de destino, devuelve la entrada que contiene al next
	// hop, segun
	// metodo longest match prefix
	// Si no hay matching y hay default, devuelve la entrada default.
	// Si no consigue matching ni default, devuelve null, lo que indica red
	// desconocida
	public RoutingEntry getNextHop(IpAddress dest) {
		int match = -1; // Bits de matching de la entrada con mayor matching
		RoutingEntry matchingentry = null;
		Set<Integer> keySet = routeT.keySet();
		for (Integer elementKey : keySet) {
			RoutingEntry i = routeT.get(elementKey);
			int m = i.getmatching(dest); // Obtiene matching de la entrada
			if (m > match) { // mejora matching existente?
				match = m;
				matchingentry = i; // routeT.get(elementKey);
			}
		}
		return matchingentry;
	}

	public String toString() {
		Set<Integer> keySet = routeT.keySet();
		for (Integer elementKey : keySet) {
			RoutingEntry i = routeT.get(elementKey);
			System.out.println("RoutingEntry: " + i.toString());
		}
		return "eee";
	}
}
