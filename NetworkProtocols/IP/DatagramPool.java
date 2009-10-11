package NetworkProtocols.IP;

import java.util.HashMap;
import java.util.Map;

import Utils.BinaryManipulator;

public class DatagramPool {
	
	private Map<String,FragmentPool> datagrams = new HashMap<String, FragmentPool>();
	
	public synchronized void addDatagram(Datagram incoming){
		String md5;
		try {
			md5 = MD5.getMD5(incoming.getSourceAddress().toString() + incoming.getDestAddress().toString() + incoming.getProtocol() + incoming.getDatagramId());
		} catch (Exception e) {
			System.out.println("Could not generate MD5, datagram will be rejected");
			e.printStackTrace();
			return;
		}
		
		if(datagrams.containsKey(md5) == false){ datagrams.put(md5, new FragmentPool()); }
		
		FragmentPool current = datagrams.get(md5);
		
		current.addFragment(incoming);
		
		if(current.isComplete()){
			Datagram reassembled = current.getReassembled();
			//PASARLO AL NIVEL SUPERIOR Y REMOVERLO DEL POOL
			BinaryManipulator.writeByteArray("c:\\CondorMethods-ass.png", reassembled.getMessage());
			//System.out.println(reassembled);
			//System.out.println(new String(reassembled.getMessage()));
			datagrams.remove(md5);
		}
		
	}
}
