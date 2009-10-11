package NetworkProtocols.IP;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Pool de fragmentos de un datagrama especifico
 */
public class FragmentPool {

	private List<Datagram> fragments = new ArrayList<Datagram>();
	
	/**
	 * Agrega un fragmento al pool
	 * @param incoming
	 */
	public void addFragment(Datagram incoming) {
		fragments.add(incoming);		
	}
	
	/**
	 * Verifica si el datagrama de este pool puede ser formado por los fragmentos con
	 * los que se cuenta actualmente. Si ese es el caso el metodo devuelve true.
	 * @return
	 */
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
	
	/**
	 * Devuelve el datagrama ensamblado, o null si no se cuentan con los fragmentos suficientes
	 * @return
	 */
	public Datagram getReassembled(){
		return isComplete() ? FragAssembler.reensamblar(fragments) : null;
	}
}
