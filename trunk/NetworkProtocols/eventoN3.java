package NetworkProtocols;

/*
Esta clase define los eventos que reciben los procesos de nivel 3. Estos procesos son IP, ICMP,
IGMP y otros.
Los eventos recibidos pueden ser originados en procesos de nivel inferior, de nivel 3 o de nivel
superior.
Los procesos de nivel inferior son las interfaces, que serian los drivers de las placas de red.
Los de nivel 3, son los restantes procesos de nivel 3 (IP, ICMP, etc.)
Los procesos de nivel superior pueden ser protocolos de transporte (UDP o TCP), cuando solicitan
envio de datos (seria un send a IP, por ejemplo), podrian ser procesos del nivel aplicacion que
se comunican directamente con el proceso de nivel 3 (raw sockets), u otros mecanismos del SO.
Cada proceso de nivel 3 tiene dos colas, una de eventos producidos por el nivel superior o por 
procersos del nivel 3, y otra por procesos de nivel inferior (interfaces). Estas colas podran 
ser cambiadas por una unica mas adelante.
Los eventos que se reciben a traves de dichas colas estan compuestos de un indicador unico de evento
y un objeto que contiene la informacion del evento
*/

public class eventoN3  {
  // Tipos de eventos a los procesos de nivel 3
  public static final int INFO_RECEIVED = 1;  // Una interfaz anuncia que recibio info para el protocolo
                                              // de nivel 3 (IP, ICMP, ARP, IGMP)
  public static final int SEND = 2;           // Un proceso de nivel mayor o igual a 3 (aplicacion, 
                                              // transporte-TCP. UDP-, o nivel 3 -IP, etc-, solicita envio
  public static final byte LINK_DOWN = 3;     // Interfaz anuncia la caida del link (NO USADO POR AHORA)
  public static final byte LINK_UP = 4;       // Interfaz anuncia link operativo (NO USADO POR AHORA)

  private int control;
  private Object info;

  public eventoN3(int ctl, Object cnt) {
    control = ctl;
    info = cnt;
  }
  
  public int getControl() {return control;}
  
  public Object getInfo() {return info;}

}
