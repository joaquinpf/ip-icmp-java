package NetworkProtocols.IP;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import Exceptions.AddressException;
import NetworkProtocols.IP.Address.IpAddress;
import Utils.BinaryManipulator;

public class FragAssembler {

	/**
	 * Fragmenta el datagrama pasado como parametro segun el MTU especificado.
	 * @param datagram
	 * @param mtu
	 * @return
	 */
	public static List<Datagram> fragmentar(Datagram datagram, int mtu)
	{
		List<Datagram> data = new ArrayList<Datagram>(); 

		if(datagram.isFlags_fragm())
		{
			if ( ( datagram.getTotalLength() ) < mtu ) //  si el Maximo permitido es mayor al tamaÃ±o del paquete envio el mismo datagrama.
			{
				data.add(datagram);
				return data;
			}
			else // sino tengo que fragmentar. 
			{
				int longitudCabecera = datagram.getTotalLength() - datagram.getMessage().length ;
				int longitudFragmento = mtu - longitudCabecera;

				//la longitud total debe ser estrictamente multiplo de 8 para poder calcular correctamente el offset
				if(longitudFragmento % 8 != 0){
					longitudFragmento -= longitudFragmento % 8;
				}
				
				int totalDatagramas = 0;
				int fragOffsetGlobal = datagram.getFragOffset();
				int fragOffsetLocal = 0;
				if ( (datagram.getMessage().length % longitudFragmento) > 0) //Si el mod > 0, hay un fragmento incompleto al final
					totalDatagramas = datagram.getMessage().length / longitudFragmento + 1 ;
				else
					totalDatagramas = datagram.getMessage().length / longitudFragmento;

				//Agregar todos los fragmentos menos el ultimo 
				for (int i = 0; i < totalDatagramas - 1 ; i++) {	 
					Datagram fragmento = generarFramento(datagram, fragOffsetLocal, fragOffsetGlobal, longitudFragmento, longitudCabecera, false); 
					fragOffsetGlobal += longitudFragmento;
					fragOffsetLocal += longitudFragmento;
					data.add(fragmento);
				}	

				int datagramaFinal = (datagram.getMessage().length % longitudFragmento > 0) ? datagram.getMessage().length % longitudFragmento : longitudFragmento ;	
				Datagram fragmento = generarFramento(datagram, fragOffsetLocal, fragOffsetGlobal, datagramaFinal, longitudCabecera, datagram.isFlags_ultfrag()); 
				data.add(fragmento);
			}
			return data;
		}	
		else 
		{
			return null;
		}	
	}

	/**
	 * Genera un fragmento del datagrama @param base
	 * @param base
	 * @param fragOffsetLocal
	 * @param fragOffsetGlobal
	 * @param longFragmento
	 * @param longHeader
	 * @param esUltimo
	 * @return
	 */
	private static Datagram generarFramento(Datagram base, int fragOffsetLocal, int fragOffsetGlobal, int longFragmento, int longHeader, boolean esUltimo)
	{
		byte[] contenidoFragmento = new byte[longFragmento + longHeader];
		contenidoFragmento[0] = (byte) ( (base.getVersion() << 4) + base.getHeaderLength());

		byte isdelay = 0, isthroughput = 0, isreliability = 0, iscost = 0, isunused = 0;
		if(base.isDelay()){ isdelay = 16; }
		if(base.isThroughput()){ isthroughput = 8; }
		if(base.isReliability()){ isreliability = 4; }
		if(base.isCost()){ iscost = 2; }
		if(base.isUnused()){ isunused = 1; }
		contenidoFragmento[1] = (byte)((base.getPrecedence() << 5) + isdelay + isthroughput + isreliability + iscost + isunused);
		
		//total length
		contenidoFragmento[2] = (byte) (longFragmento + longHeader >> 8);      
		contenidoFragmento[3] = (byte) (longFragmento + longHeader & 0x00ff);

		//bytes del Id del datagrama
		contenidoFragmento[4] = (byte) (base.getDatagramId() >> 8);
		contenidoFragmento[5] = (byte) (base.getDatagramId() & 0x00ff);
		
		//bytes de flags y offset del subdatagrama
		byte flags_nousado = 0, flags_ultfrag = 0, flags_fragm = 0, fragOffset = 0;
		if(base.isFlags_nousado()) { flags_nousado=(byte)0x80; }
		if(esUltimo) { flags_ultfrag = 64; }
		flags_fragm=32;
		fragOffset = (byte) ((fragOffsetGlobal /8 >>8)& 0x001f);
		contenidoFragmento[6] = (byte)(flags_nousado + flags_ultfrag + flags_fragm + fragOffset); // 3 bits de flag y primeros 5 bits de offset del fragmento
		contenidoFragmento[7] = (byte) (fragOffsetGlobal / 8 & 0x00ff);
		contenidoFragmento[8] = (byte) (base.getTtl() & 0x00ff);
		contenidoFragmento[9] = (byte) (base.getProtocol() & 0x00ff);
		contenidoFragmento[10] = (byte) (base.getChecksum() >> 8);
		contenidoFragmento[11] = (byte) (base.getChecksum() & 0x00ff);
		byte[] xx = new byte[4];
		xx = base.getSourceAddress().toByte();
		contenidoFragmento[12] = xx[0];
		contenidoFragmento[13] = xx[1];
		contenidoFragmento[14] = xx[2];
		contenidoFragmento[15] = xx[3];
		xx = base.getDestAddress().toByte();
		contenidoFragmento[16] = xx[0];
		contenidoFragmento[17] = xx[1];
		contenidoFragmento[18] = xx[2];
		contenidoFragmento[19] = xx[3];

		if(base.getOptions() != null){
			System.arraycopy(base.getOptions(), 0, contenidoFragmento, 20, base.getOptions().length);
		}
		if(base.getPad() != null){
			System.arraycopy(base.getPad(), 0, contenidoFragmento, 20 + base.getOptions().length, base.getPad().length);
		}
		
		for (int j = 0 ; j < longFragmento; j++) {
			contenidoFragmento[longHeader + j] = base.getMessage()[fragOffsetLocal+j];
		}

		Datagram fragmento = new Datagram(contenidoFragmento);
		fragmento.genChecksum();
		return fragmento;
	}

	/**
	 * Reensambla una lista de fragmentos a su estado original.
	 * @param fragments
	 * @return
	 */
	public static Datagram reensamblar(List<Datagram> fragments)
	{
		Collections.sort(fragments, new DatagramComparator());
		int longMessage = getDataSize(fragments);
		Datagram base = fragments.get(0);
		byte[] contenidoFragmento = new byte[longMessage + base.getHeaderLength() * 4];
		
		contenidoFragmento[0] = (byte) ( (base.getVersion() << 4) + base.getHeaderLength());

		byte isdelay = 0, isthroughput = 0, isreliability = 0, iscost = 0, isunused = 0;
		if(base.isDelay()){ isdelay = 16; }
		if(base.isThroughput()){ isthroughput = 8; }
		if(base.isReliability()){ isreliability = 4; }
		if(base.isCost()){ iscost = 2; }
		if(base.isUnused()){ isunused = 1; }
		contenidoFragmento[1] = (byte)((base.getPrecedence() << 5) + isdelay + isthroughput + isreliability + iscost + isunused);
		
		//Total length
		contenidoFragmento[2] = (byte) (longMessage + 20 >>8);      
		contenidoFragmento[3] = (byte) (longMessage + 20 & 0x00ff);

		//bytes del Id del datagrama
		contenidoFragmento[4] = (byte) (base.getDatagramId()>>8);
		contenidoFragmento[5] = (byte) (base.getDatagramId() & 0x00ff);
		
		//bytes de flags y offset del subdatagrama
		byte flags_nousado = 0, flags_ultfrag = 0, flags_fragm = 0, fragOffset = 0, orden = 0;
		/*if(base.isFlags_nousado()) { flags_nousado=(byte)0x80; }
		if(esUltimo) { flags_ultfrag = 64; }
		flags_fragm=32;*/
		fragOffset = (byte) ((orden>>8)& 0x001f);
		contenidoFragmento[6] = (byte)(flags_nousado + flags_ultfrag + flags_fragm + fragOffset); // 3 bits de flag y primeros 5 bits de offset del fragmento
		contenidoFragmento[7] = (byte) (orden & 0x00ff);
		contenidoFragmento[8] = (byte) (base.getTtl() & 0x00ff);
		contenidoFragmento[9] = (byte) (base.getProtocol() & 0x00ff);
		contenidoFragmento[10] = (byte) (base.getChecksum() >> 8);
		contenidoFragmento[11] = (byte) (base.getChecksum() & 0x00ff);
		byte[] xx = new byte[4];
		xx = base.getSourceAddress().toByte();
		contenidoFragmento[12] = xx[0];
		contenidoFragmento[13] = xx[1];
		contenidoFragmento[14] = xx[2];
		contenidoFragmento[15] = xx[3];
		xx = base.getDestAddress().toByte();
		contenidoFragmento[16] = xx[0];
		contenidoFragmento[17] = xx[1];
		contenidoFragmento[18] = xx[2];
		contenidoFragmento[19] = xx[3];

		if(base.getOptions() != null){
			for(int i=20; i<base.getOptions().length; i++){
				contenidoFragmento[20+i] = base.getOptions()[i];
			}
		}
		
		if(base.getPad() != null){
			for(int i=20+base.getOptions().length; i<base.getPad().length; i++){
				contenidoFragmento[20 + base.getOptions().length + i] = base.getPad()[i];
			}
		}
		
		int totalcount = 0;
		for(Datagram d: fragments){
			int localcount = 0;
			while(localcount < d.getMessage().length){
				contenidoFragmento[(base.getHeaderLength() * 4) + totalcount] = d.getMessage()[localcount];				
				totalcount++;
				localcount++;
			}
		}
		

		Datagram fragmento = new Datagram(contenidoFragmento);
		fragmento.genChecksum();
		return fragmento;
	}

	/**
	 * Obtiene el tamaño compuesto de los fragmentos pasados por parametro
	 * @param fragments
	 * @return
	 */
	private static int getDataSize(List<Datagram> fragments) {
		int size = 0;
		for(Datagram d: fragments){
			size += d.getMessage().length;
		}
		return size;
	}
	
	/**
	 * Metodo de prueba
	 * @param args
	 */	
	public static void main(String[] args) {
		/*byte[] message1 = new String("Mensaje de prueba 1. 1 2 3 probando. Necesita ser lo suficientemente largo como para que no entre en el mtu y deba fragmentarlo, idealmente que el ultimo fragmento sea menor que el mtu para ver que sucede. Ya llegamos a la longitud requerida siendo que estamos utilizando MTU 68. End of transmision :D").getBytes();
		byte[] message2 = new String("Mensaje de prueba 2. 1 2 3 probando. Necesita ser lo suficientemente largo como para que no entre en el mtu y deba fragmentarlo, idealmente que el ultimo fragmento sea menor que el mtu para ver que sucede. Ya llegamos a la longitud requerida siendo que estamos utilizando MTU 68. End of transmision :D").getBytes();
		byte[] message3 = new String("Mensaje de prueba 3. 1 2 3 probando. Necesita ser lo suficientemente largo como para que no entre en el mtu y deba fragmentarlo, idealmente que el ultimo fragmento sea menor que el mtu para ver que sucede. Ya llegamos a la longitud requerida siendo que estamos utilizando MTU 68. End of transmision :D").getBytes();
		byte[] message4 = new String("Mensaje de prueba 4. 1 2 3 probando. Necesita ser lo suficientemente largo como para que no entre en el mtu y deba fragmentarlo, idealmente que el ultimo fragmento sea menor que el mtu para ver que sucede. Ya llegamos a la longitud requerida siendo que estamos utilizando MTU 68. End of transmision :D").getBytes();
		try {
			Datagram d1 = new Datagram(4, 5, 0, true, true, true, true, true, message1.length + 20, 1, true, true, true, 0, 128, 1, 11, new IpAddress("168P123P154P111"), new IpAddress("168P123P154P111"));
			Datagram d2 = new Datagram(4, 5, 0, true, true, true, true, true, message2.length + 20, 2, true, true, true, 0, 128, 1, 11, new IpAddress("168P123P154P111"), new IpAddress("168P123P154P111"));
			Datagram d3 = new Datagram(4, 5, 0, true, true, true, true, true, message3.length + 20, 3, true, true, true, 0, 128, 1, 11, new IpAddress("168P123P154P111"), new IpAddress("168P123P154P111"));
			Datagram d4 = new Datagram(4, 5, 0, true, true, true, true, true, message4.length + 20, 4, true, true, true, 0, 128, 1, 11, new IpAddress("168P123P154P111"), new IpAddress("168P123P154P111"));
			
			d1.setData(message1);
			d2.setData(message2);
			d3.setData(message3);
			d4.setData(message4);
			
			List<Datagram> frags = new ArrayList<Datagram>();
			frags.addAll(FragAssembler.fragmentar(d1, 150));
			frags.addAll(FragAssembler.fragmentar(d2, 120));
			frags.addAll(FragAssembler.fragmentar(d3, 133));
			frags.addAll(FragAssembler.fragmentar(d4, 75));

			Collections.shuffle(frags);
			
			for(int i=0; i<7 ; i++){
				Datagram dat = frags.remove(0);
				frags.addAll(FragAssembler.fragmentar(dat, 68));
			}
			
			DatagramPool pool = new DatagramPool();
			
			for(Datagram dat: frags){
				System.out.println("Cacho de mensaje ID " + dat.getDatagramId() + " : " + new String(dat.getMessage()));
				pool.addDatagram(dat);
			}
			
		} catch (AddressException e) {
			e.printStackTrace();
		}*/
		
		byte[] message1 = BinaryManipulator.readByteArray("c:\\para_elisa.mid");
		byte[] options = new byte[1];
		options[0] = 0;
		try {
			Datagram d1 = new Datagram(4, 6, 0, true, true, true, true, true, message1.length + 24, 1, true, true, true, 0, 128, 1, 11, new IpAddress("168P123P154P111"), new IpAddress("168P123P154P111"),options);
		
			d1.setData(message1);
			
			List<Datagram> frags = new ArrayList<Datagram>();
			frags.addAll(FragAssembler.fragmentar(d1, 700));

			Collections.shuffle(frags);
			
			
			
			DatagramPool pool = new DatagramPool();
			
			for(Datagram dat: frags){
				pool.addDatagram(dat);
			}
			
		} catch (AddressException e) {
			e.printStackTrace();
		}
	}

	
}
