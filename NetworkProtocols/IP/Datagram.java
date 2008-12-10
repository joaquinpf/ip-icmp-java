package NetworkProtocols.IP;

import java.util.Vector;

import Exceptions.*; 
import NetworkProtocols.IP.Address.*;

public class Datagram {

  private int version;         // Version de IP (4) (4 bits)
  private int ihl;             // Longitud del header en multiplos de 4 bytes (4 bits)
  // Tipo de servicio  (8 bits)
  private int precedence;  // Precedencia, 3 bits
  private boolean delay;       // demora (1 bit)
  private boolean throughput;  // throughput (1 bit)
  private boolean reliability; // confiabilidad (1 bit)
  private boolean cost;        // costo (1 bit)
  private boolean unused;      // no usado (1 bit)
  public int totalLength;     // Longitud total del datagram en bytes (16 bits)
  private int datagramId;      // Identificacion unica del datagram  (16 bits)
  // Flags (3 bits)
  private boolean flags_nousado;   // No usado (1 bit) primero no usado
  private boolean flags_ultfrag;   // (1 bit) segundo true si es ultimo fragmento
  private boolean flags_fragm;     // (1 bit) tercero true si es posible fragmentar el datagram
  private int fragOffset;      // Offset de este fragmento en el datagram completo (13 bits)
  private int ttl;             // Tiempo de vida  (8 bits)
  private int protocol;        // Protocolo encapsulado en IP  (8 bits)
  private int checksum;        // Checksum del datagram  (16 bits)
  private IpAddress sourceAddress;   // Direccion origen  (32 bits)
  private IpAddress destAddress;     // Direccion destino  (32 bits)
  private byte[] options;         // Opciones (hasta bits)
  private byte[] pad;             // Relleno  (completa a multiplo de bits)
  private byte[] payload;         // payload del datagram (hasta 65535 bytes menos header)
  private byte[] message;		//Bytes del mensaje a ser enviado --200 bytes (longitud para prueba)
  								//OJO!!! no hay nada implementado para tratar estos bytes
  
  int MTU   ;   // debe ser como minimo 68 bytes (total del header). Longitud en bits. 
  // Dada la representacion interna del frame IP, devuelve su formato en bits para la transmision
  public byte[] toByte()  {
    byte[] b = new byte[220];
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
    System.arraycopy(message, 0, b, 20, 200);
    return b;
  }

  // Devuelva la representaci�n interna del datagram IP, dado un arreglo de bytes
  public Datagram(byte[] byt) {
    version=(int)(byt[0]&0xf0);
    version = version >> 4;
    ihl = (int)(byt[0]&0x0f);
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
    // FALTA la part e de opciones, pad y payloas
    message = new byte[200];
    System.arraycopy(byt, 20, message, 0, byt.length - 20);
  }

  // Crea un datagram a partir del valor de sus campos. Produce una inetrrupcion si los valores de
  // los campos estan fuera de rango (no chequea validez de los campos)
  public Datagram(int vers, int hedlen, int prec, boolean del, boolean thr, boolean rel, boolean cos,
                  boolean notus, int ttol, int did, boolean fnu, boolean fuf, boolean frg, int off,
                  int tl, int prt, int chk, IpAddress sou, IpAddress des) {
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
      message = new byte[200];
    } catch (DatagramException e) {
      e.printStackTrace();
    }
  }

  public byte[] getHeaderBytes()  {
	    byte[] b = new byte[100];
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
	    return b;
	  }
  
  // Verifica que el checksum del header sea correcto. Si lo es devuelve true
  public boolean verifyChecksum() {
    int lg = this.ihl * 4;  // Longitud del header IP
    byte[] b = new byte[lg];
    b = this.toByte();      // Transforma el header en un arreglo de bytes
    int ck = 0;
    for(int i=0; i<lg;i=i+2) 
    	ck+=(((b[i] & 0xff) << 8) | (b[i+1] & 0xff));
    System.out.println("ck "+ck +"   and "+ (int) (ck&0x00ffff));
    if( (ck & 0x00ffff) == 0x00ffff) 
    	return true;
    else 
    	return false;
  }

  // Genera el checksum del header. Ademas de cambiarlo en el datagram, devuelve su resultado
  public int genChecksum() {
    int lg = this.ihl * 4;  // Longitud del header IP
    byte[] b = new byte[lg];
    b = this.toByte();      // Transforma el header en un arreglo de bytes
    b[10] = b[11] = 0;      // pone checksum en cero
    int ck = 0;
    for(int i=0; i<lg;i=i+2) ck+=(((b[i] & 0xff) << 8) | (b[i+1] & 0xff));
    System.out.println("checksum "+ck );
    ck = (ck^0x00ffff)&0x00ffff;
    this.checksum = ck;                 // cambia el checksum en el datagram
    System.out.println("checksum "+ck );
    return ck;
  }

  // Prueba de checksum, lo genera a partir de un grupo de bytes dado
  public int pruebaChecksum(byte[] b) {
    int lg = b.length;
    int ck = 0;
    for(int i=0; i<lg;i=i+2) ck+=(((b[i] & 0xff) << 8) | (b[i+1] & 0xff));
    System.out.println("checksum SUMA "+ck );
    ck = (ck^0x00ffff)&0x00ffff;
    System.out.println("checksum RESULTADO "+ck );
    return ck;
  }



  // Genera un checksum erroneo del header. Ademas de cambiarlo en el datagram, devuelve su resultado
  // Se utiliza para pruebas
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

  public int getVersion() {return version;}

  public int getHeaderLength() {return ihl;}

  public void setsourceAddress(IpAddress src){
	  this.sourceAddress = src;
	  this.genChecksum();
  }

  public void setDestAddress(IpAddress dest){
	  this.destAddress = dest;
	  this.genChecksum();
  }

  public IpAddress getDestAddr() {return destAddress;}
  
  public IpAddress getSourceAddr() {return sourceAddress;}

  public byte[] getData(){
	  return message.clone();
  }
  
  public void setData(byte[] data){
	  this.message = data.clone();
	  this.genChecksum();
  }
  
  public int getProtocol(){
	  return protocol;
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
  
  public void decrementTtl() {
	  if (ttl > 0)
		  ttl--;
	  this.genChecksum();
  }
  
  // Datagram formateado
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
                 " chks:"+checksum+" DST:"+sourceAddress.toString()+" SRC:"+destAddress.toString();
    System.out.println(fip);
    return fip;
  }                  	
}

/*
 * Es el checksum de la cabecera. Se calcula como el complemento a uno 
 * de la suma de los complementos a uno de todas las palabras de 16 bits 
 * de la cabecera. Con el fin de este cálculo, el campo checksum se supone cero. 
 * Si el checksum de la cabecera no se corresponde con los contenidos, el 
 * datagrama se desecha, ya que al menos un bit de la cabecera está corrupto, 
 * y el datagrama podría haber llegado al destino equivocado.
 */


public Vector<Datagram> fragmentar()
{
	Vector<Datagram> v = new Vector<Datagram>(); 
	
	if(flags_fragm)
	{
		if ( ( this.totalLength ) < MTU ) //  si el Maximo permitido es mayor al tamaño del paquete envio el mismo datagrama.
			{
				v.addElement(this);
				return v;
			}
		else // sino tengo que fragmentar. 
		{
			int longitudCabecera = totalLenght - message.length ;
			int longitudFragmento = MTU - longitudCabecera;
			
			int totalDatagramas = 0;
			int orden = 0; 
			if ( (message.length % longitudFragmento) > 0)
			
				totalDatagramas = message.length / longitudFragmento + 1 ;
			
			else
				totalDatagramas = message.length / longitudFragmento;
			
			for (int i = 0; i < totalDatagramas ; i++)
				{
					if (i == totalDatagramas - 1)
					{
						int datagramaFinal = message.length % longitudFragmento;	
						Datagram fragmento = generarFramento( i, orden,datagramaFinal ,true); 
						orden += longitudFragmento;
						v.addElement(this);
					}
					else
					{
						Datagram fragmento = generarFramento( i, orden, longitudFragmento,false); 
						orden += longitudFragmento;
						v.addElement(this);
					}
				
				}	
		}
		return v;
	}	
	else 
	{
		return null;
	}	
}


public Datagram generarFramento(int i,int orden,int longitudFragmento,boolean esUltimo)
{
	byte[MTU] contenidoFragmento;
	contenidoFragmento[0] = (byte) ( (version << 4) + ihl);
	contenidoFragmento[1] = (byte)((precedence<<5)+delay+throughput+reliability+cost+unused);
	//bytes del Id del datagrama
	contenidoFragmento[2] = (byte) (i>>8);      
	contenidoFragmento[3] = (byte) (i & 0x00ff);
	contenidoFragmento[4] = (byte) (i>>8);
	contenidoFragmento[5] = (byte) (i & 0x00ff);
	//bytes de flags y offset del subdatagrama
	byte x1=x2=x3=0;
    if(flags_nousado) x1=(byte)0x80;
    if(esUltimo) x2 = (byte) x2=64;
    x3=32;
    x4 = (byte) ((orden>>8)& 0x001f);
    contenidoFragmento[6] = (byte)(x1+x2+x3+x4); // 3 bits de flag y primeros 5 bits de offset del fragmento
    contenidoFragmento[7] = (byte) (orden & 0x00ff);
   
    contenidoFragmento[8] = (byte) (ttl & 0x00ff);
    contenidoFragmento[9] = (byte) (protocol & 0x00ff);
    contenidoFragmento[10] = (byte) (checksum>>8);
    contenidoFragmento[11] = (byte) (checksum & 0x00ff);
    byte[] xx = new byte[4];
    xx = sourceAddress.toByte();
    contenidoFragmento[12] = xx[0];
    contenidoFragmento[13] = xx[1];
    contenidoFragmento[14] = xx[2];
    contenidoFragmento[15] = xx[3];
    xx = destAddress.toByte();
    contenidoFragmento[16] = xx[0];
    contenidoFragmento[17] = xx[1];
    contenidoFragmento[18] = xx[2];
    contenidoFragmento[19] = xx[3];
	
    for (int j = 0 ; j < longitudFragmento; j++)
    {
    	contenidoFragmento[20+j] = message[orden+j];
    }
        
    Datagram fragmento = new Datagram(contenidoFragmento);
    fragmento.genChecksum();
    return fragmento;
}

public void reemsamblar(Vector v)
{
}


