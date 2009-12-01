package NetworkProtocols.IP;

import java.util.ArrayList;
import java.util.List;

import Exceptions.AddressException;
import Exceptions.DatagramException;
import NetworkProtocols.IP.Address.IpAddress;

public class Datagram {

	/**
	 * id utilizado para los datagrams q se crean con Datagram(int)
	 */
	private static int datId = 1;
	/**
	 * Version de IP (4) (4 bits)
	 */
	private int version;         
	
	/**
	 * Longitud del header en multiplos de 4 bytes (4 bits)
	 */
	private int ihl;              
	// Tipo de servicio  (8 bits)
	
	/**
	 * Precedencia, 3 bits
	 */
	private int precedence; 
	
	/**
	 * demora (1 bit)
	 */
	private boolean delay;        
	
	/**
	 * throughput (1 bit)
	 */
	private boolean throughput; 
	
	/**
	 * confiabilidad (1 bit)
	 */
	private boolean reliability; 
	
	/**
	 * costo (1 bit)
	 */
	private boolean cost;       
	
	/**
	 * no usado (1 bit)
	 */
	private boolean unused;      
	
	/**
	 * Longitud total del datagram en bytes (16 bits)
	 */
	private int totalLength;     
	
	/**
	 * Identificacion unica del datagram  (16 bits)
	 */
	private int datagramId;     
	// Flags (3 bits)
	
	/**
	 * No usado (1 bit) primero no usado
	 */
	private boolean flags_nousado;   
	
	/**
	 * (1 bit) segundo true si es ultimo fragmento
	 */
	private boolean flags_ultfrag;   
	
	/**
	 * (1 bit) tercero true si es posible fragmentar el datagram
	 */
	private boolean flags_fragm;     
	
	/**
	 * Offset de este fragmento en el datagram completo (13 bits)
	 */
	private int fragOffset;      
	
	/**
	 * Tiempo de vida  (8 bits)
	 */
	private int ttl;             
	
	/**
	 * Protocolo encapsulado en IP  (8 bits)
	 */
	private int protocol;        

	/**
	 * Checksum del datagram  (16 bits)
	 * Es el checksum de la cabecera. Se calcula como el complemento a uno 
	 * de la suma de los complementos a uno de todas las palabras de 16 bits 
	 * de la cabecera. Con el fin de este cálculo, el campo checksum se supone cero. 
	 * Si el checksum de la cabecera no se corresponde con los contenidos, el 
	 * datagrama se desecha, ya que al menos un bit de la cabecera está corrupto, 
	 * y el datagrama podría haber llegado al destino equivocado.
	 */
	private int checksum;   
	
	/**
	 * Direccion origen  (32 bits)
	 */
	private IpAddress sourceAddress;   
	
	/**
	 * Direccion destino  (32 bits)
	 */
	private IpAddress destAddress;     
	
	/**
	 * Opciones (hasta bits)
	 */
	private byte[] options;         
	
	/**
	 * Relleno  (completa a multiplo de bits)
	 */
	private byte[] pad;             
	
	/**
	 * payload del datagram (hasta 65535 bytes menos header)
	 */
	private byte[] payload;          
	
	/**
	 * Bytes del mensaje a ser enviado
	 */
	private byte[] message;

	/**
	 * Dada la representacion interna del frame IP, devuelve su formato en bits para la transmision
	 * @return
	 */
	public byte[] toByte()  {
		byte[] b = new byte[(ihl * 4) + message.length];
		b[0] = (byte) ( (version << 4) + ihl);
		byte x1,x2,x3,x4,x5;
		x1=x2=x3=x4=x5=0;
		if(delay) x1=16;
		if(throughput) x2=8;
		if(reliability) x3=4;
		if(cost) x4=2;
		if(unused)x5=1;
		b[1] = (byte)((precedence<<5)+x1+x2+x3+x4+x5);
		b[2] = (byte) (totalLength>>8);
		b[3] = (byte) (totalLength & 0x00ff);
		b[4] = (byte) (datagramId>>8);
		b[5] = (byte) (datagramId & 0x00ff);
		x1=x2=x3=0;
		if(flags_nousado) x1=(byte)0x80;
		if(flags_ultfrag) x2=64;
		if(flags_fragm) x3=32;
		x4 = (byte) ((fragOffset>>8)& 0x001f);
		b[6] = (byte)(x1+x2+x3+x4);
		b[7] = (byte) (fragOffset & 0x00ff);
		b[8] = (byte) (ttl & 0x00ff);
		b[9] = (byte) (protocol & 0x00ff);
		b[10] = (byte) (checksum>>8);
		b[11] = (byte) (checksum & 0x00ff);
		byte[] xx = new byte[4];
		xx = sourceAddress.toByte();
		b[12] = xx[0];
		b[13] = xx[1];
		b[14] = xx[2];
		b[15] = xx[3];
		xx = destAddress.toByte();
		b[16] = xx[0];
		b[17] = xx[1];
		b[18] = xx[2];
		b[19] = xx[3];
		if(options != null){
			System.arraycopy(options, 0, b, 20, options.length);
		}
		if(pad != null){
			System.arraycopy(pad, 0, b, 20 + options.length, pad.length);
		}
		System.arraycopy(message, 0, b, (ihl * 4), message.length);
		return b;
	}

	/**
	 * Devuelva la representaci�n interna del datagram IP, dado un arreglo de bytes
	 * @param byt
	 */
	public Datagram(byte[] byt) {
		version=(int)(byt[0] & 0xf0);
		version = version >> 4;
		ihl = (int)(byt[0] & 0x0F);
		//    int b = (int) byt[0];
		precedence = byt[1] % 0x000000e0;  // Precedencia, 3 bits
		precedence = (precedence >> 5) & 0x07;
		delay = false;                     // demora (1 bit)
		byte x = (byte) (byt[1] & (byte) 0x10);
		if(x != 0) delay = true;
		throughput = false;                     // throughput (1 bit)
		x = (byte) (byt[1] & (byte) 0x08);
		if(x != 0) throughput = true;
		reliability = false;                     // reliability (1 bit)
		x = (byte) (byt[1] & (byte) 0x04);
		if(x != 0) reliability = true;
		cost = false;                          // cost (1 bit)
		x = (byte) (byt[1] & (byte) 0x02);
		if(x != 0) cost = true;
		unused = false;                     // no usado (1 bit)
		x = (byte) (byt[1] & (byte) 0x01);
		if(x != 0) unused = true;
		System.out.println("BYTES 2 3 4 5 "+byt[2]+"  "+byt[3]+"  "+byt[4]+"  "+byt[5]);
		totalLength = (int) (byt[2]<<8) + (int) byt[3];
		datagramId = (int) ((byt[4]<<8)&0x00ffff) + (int) (byt[5]&0x00ff);
		datId = datagramId + 1;
		flags_nousado = false;
		x = (byte) (byt[6] & 0x80);
		if(x != 0) flags_nousado = true;
		flags_ultfrag = false;
		x = (byte) (byt[6] & 0x40);
		if(x != 0) flags_ultfrag = true;
		flags_fragm = false;
		x = (byte) (byt[6] & 0x20);
		if(x != 0) flags_fragm = true;
		x = (byte) (byt[6] & 0x001f);
		fragOffset = (int) ((x<<8)&0x00ff00) + (int) (byt[7]& 0x00ff);
		ttl = (int) (byt[8]&0x00ff);
		protocol = (int) (byt[9]&0x00ff);
		checksum = (int) ((byt[10]<<8)&0x00ffff) + (int) (byt[11]&0x00ff);
		sourceAddress = new IpAddress(byt[12], byt[13], byt[14], byt[15]);
		destAddress =  new IpAddress(byt[16], byt[17], byt[18], byt[19]);
		
		//Options, optional. If ihl * 4 = 20, there are none
		List<Byte> localOptions = new ArrayList<Byte>();
		int paddingStart = -1;
		for(int i=20; i < (ihl * 4); i++){
			if(byt[i] != 0) {
				localOptions.add(byt[i]);
			} else {
				localOptions.add(byt[i]);
				paddingStart = i + 1; //padding starts in the next byte
				break;
			}
		}
		
		if(localOptions.size() > 0){
			options = new byte[localOptions.size()];
			for(int i=0; i < localOptions.size(); i++){
				options[i] = localOptions.get(i);
			}
		} else {
			options = null;
		}
		
		//Padding, must be used when options ended in a non 4 byte boundary, specified with the option "0000000"
		if(paddingStart != -1){
			pad = new byte[4 - localOptions.size() % 4];
			System.arraycopy(byt, 20 + localOptions.size(), pad, 0, pad.length);
		} else {
			pad = null;
		}
		
		// FALTA la parte de payloas
		message = new byte[byt.length - (ihl * 4)];
		System.arraycopy(byt, (ihl * 4), message, 0, byt.length - (ihl * 4));
		
		updateTotalLength();
	}

	/**
	 * Crea un datagram a partir del valor de sus campos. Produce una inetrrupcion si los valores de
	 * los campos estan fuera de rango (no chequea validez de los campos)
	 * @param vers
	 * @param hedlen
	 * @param prec
	 * @param del
	 * @param thr
	 * @param rel
	 * @param cos
	 * @param notus
	 * @param ttol
	 * @param did
	 * @param fnu
	 * @param fuf
	 * @param frg
	 * @param off
	 * @param tl
	 * @param prt
	 * @param chk
	 * @param sou
	 * @param des
	 * @param options
	 */
	public Datagram(int vers, int hedlen, int prec, boolean del, boolean thr, boolean rel, boolean cos,
			boolean notus, int ttol, int did, boolean fnu, boolean fuf, boolean frg, int off,
			int tl, int prt, int chk, IpAddress sou, IpAddress des, byte[] options) {
		try {
			if((vers < 0) || (vers > 15)) throw new DatagramException("Campo VERSION fuera de rango");
			version = vers;
			if((hedlen < 0) || (hedlen > 15)) throw new DatagramException("Campo HEADERLENGTH fuera de rango");
			ihl = hedlen;
			if((prec < 0) || (prec > 7)) throw new DatagramException("Campo PRECEDENCE fuera de rango");
			precedence = prec;
			delay = del;
			throughput = thr;
			reliability = rel;
			cost = cos;
			unused = notus;
			if((ttol < 0) || (ttol > 65535)) throw new DatagramException("Campo TOTALLENGTH fuera de rango");
			totalLength = ttol;
			if((did < 0) || (did > 65535)) throw new DatagramException("Campo DATAGRAMID fuera de rango");
			datagramId = did;
			datId = datagramId + 1;
			flags_nousado = fnu;
			flags_ultfrag = fuf;
			flags_fragm = frg;
			if((off < 0) || (off > 8191)) throw new DatagramException("Campo FRAGMENTOFFSET fuera de rango");
			fragOffset = off;
			if((tl < 0) || (tl > 255)) throw new DatagramException("Campo TTL fuera de rango");
			ttl = tl;
			if((prt < 0) || (prt > 255)) throw new DatagramException("Campo PROTOCOL fuera de rango");
			protocol = prt;
			if((chk < 0) || (chk > 65535)) throw new DatagramException("Campo CHECKSUM fuera de rango");
			checksum = chk;
			sourceAddress = sou;   // IpAddress, ya chequeada
			destAddress = des;     // IpAddress, ya chequeada
			if(options != null) {
				this.options = options;

				//Padding if options does not end in a 4 byte boundary
				if(options.length % 4 != 0) {
					pad = new byte[4 - (options.length % 4)];
					for(int i=0; i<pad.length ;i++){
						pad[i] = 7;
					}
				}
			}
			message = new byte[200];
		} catch (DatagramException e) {
			e.printStackTrace();
		}
	}

	public Datagram (int lengthData){
//			if((vers < 0) || (vers > 15)) throw new DatagramException("Campo VERSION fuera de rango");
		version = 4;
//			if((hedlen < 0) || (hedlen > 15)) throw new DatagramException("Campo HEADERLENGTH fuera de rango");
		ihl = 5;
//			if((prec < 0) || (prec > 7)) throw new DatagramException("Campo PRECEDENCE fuera de rango");
		precedence = 0;
		delay = false;
		throughput = false;
		reliability = false;
		cost = false;
		unused = false;
//			if((ttol < 0) || (ttol > 65535)) throw new DatagramException("Campo TOTALLENGTH fuera de rango");
		totalLength = 20 + lengthData;
//			if((did < 0) || (did > 65535)) throw new DatagramException("Campo DATAGRAMID fuera de rango");
		datagramId = datId;
		datId = datagramId + 1;
		flags_nousado = false;
		flags_ultfrag = true;
		flags_fragm = true;
//			if((off < 0) || (off > 8191)) throw new DatagramException("Campo FRAGMENTOFFSET fuera de rango");
		fragOffset = 0;
//			if((tl < 0) || (tl > 255)) throw new DatagramException("Campo TTL fuera de rango");
		ttl = 64;
//			if((prt < 0) || (prt > 255)) throw new DatagramException("Campo PROTOCOL fuera de rango");
		protocol = 1;
//			if((chk < 0) || (chk > 65535)) throw new DatagramException("Campo CHECKSUM fuera de rango");
		message = new byte[lengthData];
		try {
			sourceAddress = new IpAddress("192P168P1P1");
		} catch (AddressException e) {
			System.out.println("Error al crear la direccion de origen del datagram");
			e.printStackTrace();
		}
		try {
			destAddress = new IpAddress("192P168P1P1");
		} catch (AddressException e) {
			System.out.println("Error al crear la direccion de destino del datagram");
			e.printStackTrace();
		}
		genChecksum();
	}
	
	/**
	 * Retorna los bytes del header del datagrama
	 * @return
	 */
	public byte[] getHeaderBytes()  {
		int options_len = 0, pad_len = 0;
		if (options != null)
			options_len = options.length;
		if (pad != null)
			pad_len = pad.length;
		
		byte[] b = new byte[(ihl*4) + options_len + pad_len];
		b[0] = (byte) ( (version << 4) + ihl);
		byte x1,x2,x3,x4,x5;
		x1=x2=x3=x4=x5=0;
		if(delay) x1=16;
		if(throughput) x2=8;
		if(reliability) x3=4;
		if(cost) x4=2;
		if(unused)x5=1;
		b[1] = (byte)((precedence<<5)+x1+x2+x3+x4+x5);
		b[2] = (byte) (totalLength>>8);
		b[3] = (byte) (totalLength & 0x00ff);
		b[4] = (byte) (datagramId>>8);
		b[5] = (byte) (datagramId & 0x00ff);
		x1=x2=x3=0;
		if(flags_nousado) x1=(byte)0x80;
		if(flags_ultfrag) x2=64;
		if(flags_fragm) x3=32;
		x4 = (byte) ((fragOffset>>8)& 0x001f);
		b[6] = (byte)(x1+x2+x3+x4);
		b[7] = (byte) (fragOffset & 0x00ff);
		b[8] = (byte) (ttl & 0x00ff);
		b[9] = (byte) (protocol & 0x00ff);
		b[10] = (byte) (checksum>>8);
		b[11] = (byte) (checksum & 0x00ff);
		byte[] xx = new byte[4];
		xx = sourceAddress.toByte();
		b[12] = xx[0];
		b[13] = xx[1];
		b[14] = xx[2];
		b[15] = xx[3];
		xx = destAddress.toByte();
		b[16] = xx[0];
		b[17] = xx[1];
		b[18] = xx[2];
		b[19] = xx[3];
		
		if(options != null){
			System.arraycopy(options, 0, b, 20, options.length);
		}
		if(pad != null){
			System.arraycopy(pad, 0, b, 20 + options.length, pad.length);
		}
		
		return b;
	}

	/**
	 * Verifica que el checksum del header sea correcto. Si lo es devuelve true
	 * @return
	 */
	public boolean verifyChecksum() {
		int lg = this.ihl * 4;  // Longitud del header IP
		byte[] b = new byte[lg];
		b = this.toByte();      // Transforma el header en un arreglo de bytes
		int ck = 0;
		for(int i=0; i<lg;i=i+2) 
			ck+=(((b[i] & 0xff) << 8) | (b[i+1] & 0xff));
		//System.out.println("ck "+ck +"   and "+ (int) (ck&0x00ffff));
		if( (ck & 0x00ffff) == 0x00ffff) 
			return true;
		else 
			return false;
	}

	/**
	 * Genera el checksum del header. Ademas de cambiarlo en el datagram, devuelve su resultado
	 * @return
	 */
	public int genChecksum() {
		int lg = this.ihl * 4;  // Longitud del header IP
		byte[] b = new byte[lg];
		b = this.toByte();      // Transforma el header en un arreglo de bytes
		b[10] = b[11] = 0;      // pone checksum en cero
		int ck = 0;
		for(int i=0; i<lg;i=i+2) ck+=(((b[i] & 0xff) << 8) | (b[i+1] & 0xff));
		//System.out.println("checksum "+ck );
		ck = (ck^0x00ffff)&0x00ffff;
		this.checksum = ck;                 // cambia el checksum en el datagram
		//System.out.println("checksum "+ck );
		return ck;
	}

	/**
	 * Prueba de checksum, lo genera a partir de un grupo de bytes dado
	 * @param b
	 * @return
	 */
	public int pruebaChecksum(byte[] b) {
		int lg = b.length;
		int ck = 0;
		for(int i=0; i<lg;i=i+2) ck+=(((b[i] & 0xff) << 8) | (b[i+1] & 0xff));
		//System.out.println("checksum SUMA "+ck );
		ck = (ck^0x00ffff)&0x00ffff;
		//System.out.println("checksum RESULTADO "+ck );
		return ck;
	}

	/**
	 * Genera un checksum erroneo del header. Ademas de cambiarlo en el datagram, devuelve su resultado
	 * Se utiliza para pruebas
	 */
	public int genBadChecksum() {
		int lg = this.ihl * 4;  // Longitud del header IP
		byte[] b = new byte[lg];
		b = this.toByte();      // Transforma el header en un arreglo de bytes
		b[10] = b[11] = 0;      // pone checksum en cero
		int ck = 0;
		for(int i=0; i<lg;i=i+2) ck+=(((b[i] & 0xff) << 8) | (b[i+1] & 0xff));
		ck = (ck^0x00ffff)&0x00f5af;        // cambia los bits del checksum
		this.checksum = ck;                 // cambia el checksum en el datagram
		System.out.println("checksum erroneo:"+ck );
		return ck;
	}

	/**
	 * Datagram formateado
	 */
	public String toString() {

		String dtrcu= "";
		if(delay) dtrcu=dtrcu+"D"; else dtrcu=dtrcu+"-";
		if(throughput) dtrcu=dtrcu+"T"; else dtrcu=dtrcu+"-";
		if(reliability) dtrcu=dtrcu+"R"; else dtrcu=dtrcu+"-";
		if(cost) dtrcu=dtrcu+"C"; else dtrcu=dtrcu+"-";
		if(unused) dtrcu=dtrcu+"U"; else dtrcu=dtrcu+"-";
		String fff="";
		if(flags_nousado) fff=fff+"T,"; else fff=fff+"F,";
		if(flags_ultfrag) fff=fff+"LF,"; else fff=fff+"-,";
		if(flags_fragm) fff=fff+"DNTF"; else fff=fff+"-";
		String fip = "DG IP: V:"+version+" HL:"+ihl+" TOS-P:"+precedence+"-"+dtrcu+" TLN:"+totalLength+
		" DGId:"+datagramId+" Flg:"+fff+" OFF:"+fragOffset+" TTL:"+ttl+" PROT:"+protocol+
		" chks:"+checksum+" DST:"+destAddress.toString()+" SRC:"+sourceAddress.toString();
		return fip;
	} 

	/**
	 * Decrementa en 1 el time to live y regenera el checksum
	 */
	public void decrementTtl() {
		if (ttl > 0)
			ttl--;
		this.genChecksum();
	}
	
	
	//**********************************
	//GETTERS Y SETTERS
	//**********************************
	
	public int getVersion() {
		return version;
	}

	public int getHeaderLength() {
		return ihl;
	}

	public void setsourceAddress(IpAddress src){
		this.sourceAddress = src;
		this.genChecksum();
	}

	public void setDestAddress(IpAddress dest){
		this.destAddress = dest;
		this.genChecksum();
	}

	public byte[] getData(){
		return message.clone();
	}

	public void setData(byte[] data){
		this.message = data.clone();
		this.genChecksum();
	}
	
	public void updateTotalLength(){
		totalLength = getHeaderLength() * 4 + message.length;
	}
	
	public void setData(byte[] data, boolean updateTotalLength){
		this.message = data.clone();
		if (updateTotalLength == true)
			updateTotalLength();
		this.genChecksum();
	}

	public int getProtocol(){
		return protocol;
	}
	
	public void setProtocol(int p){
		this.protocol = p;
	}

	public byte[] getOptions(){
		return options;
	}

	public byte[] getPad() {
		return pad;
	}

	public byte[] getPayload() {
		return payload;
	}

	public byte[] gatDataBytes(int bytes) {
		byte[] ret = new byte[bytes];
		System.arraycopy(message, 0, ret, 0, bytes);
		return ret;
	}

	public int getTtl() {
		return ttl;
	}

	public void setTtl(int value) {
		ttl = value;
		this.genChecksum();
	}
	
	public int getPrecedence() {
		return precedence;
	}

	public boolean isDelay() {
		return delay;
	}

	public boolean isThroughput() {
		return throughput;
	}

	public boolean isReliability() {
		return reliability;
	}

	public boolean isCost() {
		return cost;
	}

	public boolean isUnused() {
		return unused;
	}

	public int getTotalLength() {
		return totalLength;
	}

	public int getDatagramId() {
		return datagramId;
	}

	public boolean isFlags_nousado() {
		return flags_nousado;
	}

	public boolean isFlags_ultfrag() {
		return flags_ultfrag;
	}

	public boolean isFlags_fragm() {
		return flags_fragm;
	}

	public int getFragOffset() {
		return fragOffset;
	}

	public int getChecksum() {
		return checksum;
	}

	public IpAddress getSourceAddress() {
		return sourceAddress;
	}

	public IpAddress getDestAddress() {
		return destAddress;
	}

	public byte[] getMessage() {
		return message;
	}
	
	public void setUltimoFragmento(boolean status){
		flags_ultfrag = status;
	}

	public void setFragmentar(boolean status){
		flags_fragm = status;
	}

}


