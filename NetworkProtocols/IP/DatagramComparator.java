package NetworkProtocols.IP;

import java.util.Comparator;

public class DatagramComparator implements Comparator<Datagram> {

	/**
	 * Compara datagramas segun su fragment offset
	 */
	@Override
	public int compare(Datagram arg0, Datagram arg1) {
		
		if(arg0.getFragOffset() > arg1.getFragOffset()){
			return 1;
		} else if(arg0.getFragOffset() < arg1.getFragOffset()){
			return -1;
		} else {
			return 0;
		}
	}

}
