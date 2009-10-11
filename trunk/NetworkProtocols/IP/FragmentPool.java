package NetworkProtocols.IP;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class FragmentPool {

	private List<Datagram> fragments = new ArrayList<Datagram>();
	
	public void addFragment(Datagram incoming) {
		fragments.add(incoming);		
	}
	
	public boolean isComplete(){
		Collections.sort(fragments, new DatagramComparator());
		if(fragments.size() == 0 || fragments.get(0).getFragOffset() != 0 || fragments.get(fragments.size() - 1).isFlags_ultfrag() == false) { 
			return false; 
		}
		
		Datagram last = fragments.get(0);

		for(int i=1; i < fragments.size(); i++){
			Datagram next = fragments.get(i);
			
			if(last.getFragOffset() * 8 + last.getMessage().length != next.getFragOffset() * 8){
				return false;
			}
			
			last = next;
		}
		
		return true;
	}
	
	public Datagram getReassembled(){
		return isComplete() ? FragAssembler.reensamblar(fragments) : null;
	}
}
