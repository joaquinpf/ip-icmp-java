package Link;

import java.net.*;
import java.io.*;
import java.util.Hashtable;
import java.util.Map;

import Exceptions.*; 
import Interface.*;
import Link.*;
import Utils.*;      
import NetworkProtocols.*;
  import NetworkProtocols.IP.Address.*;
    import NetworkProtocols.IP.*;
    import NetworkProtocols.IP.RoutingTable.*;
  import NetworkProtocols.ICMP.*;
  import NetworkProtocols.ARP.*;
  
// acepta frames (via UDP) provenientes de la otra punta del link (esto cambiara para links
// de acceso multiple) y los entrega a la interfaz, quien se encargara de desencapsular y determinar
// a quien debe pasarlos (IP, ARP, etc)
public class Linkreceiver extends Thread  {
  static DatagramSocket socket = null;
  DatagramPacket dgpacket;
  byte buff[];
  Link lnk;
  // Esto podria cambiar si es un link de acceso multiple
  static int remport;
  static InetAddress remaddr;

  public Linkreceiver(Link l, DatagramSocket sk) throws NodeException {
    socket = sk;
    lnk = l;
    buff = new byte[20000];
    dgpacket = new DatagramPacket(buff, 20000);
    this.start();
  }

  public void run() {
    try {
      while (true) {
        socket.receive(dgpacket);
        byte[] bb = new byte[dgpacket.getLength()];
        System.arraycopy(buff, 0 , bb, 0, dgpacket.getLength());
        // aca entregar frame al Link, que lo entrega a la interfaz
            System.out.println("LINK recibe  "+ bb);

        lnk.receive(bb);
        //System.out.println("   R<-- (" );
      }
    }
    catch (SocketException se) {
      if (se.getMessage().matches("Socket closed"))
        System.out.println("Linkreceiver closed");
      else {
        //System.out.println("Exception in DCCPreceiver.");
        se.printStackTrace();
      }
    }
    catch (IOException ioex) {
      //System.out.println("Exception in DCCPreceiver.");
      ioex.printStackTrace();
      //aca mandar la exception
    }
    //System.out.println("DCCPreceiver thread finished.");
  }

  public void close() throws IOException {
    socket.close();
  }
}
