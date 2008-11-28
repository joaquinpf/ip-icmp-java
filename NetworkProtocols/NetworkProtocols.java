package NetworkProtocols;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.net.*;

import Exceptions.*; 
import Interface.*;
import Link.*;

import Utils.*;
import NetworkProtocols.*;
  import NetworkProtocols.IP.*;
    import NetworkProtocols.IP.Address.*;
    import NetworkProtocols.IP.RoutingTable.*;
  import NetworkProtocols.ICMP.*;
  import NetworkProtocols.ARP.*;
public class NetworkProtocols {

  // Tipos de protocolo soportados por el data link
  public static final int PROTO_IP = 1;
  public static final int PROTO_ARP = 2;
  public static final int PROTO_ICMP = 3;

  public static Map<Integer, Object> protocols = null;  // Tabla con los protocolos de N3 registrados


  public NetworkProtocols(){
    protocols = new HashMap<Integer, Object>();
  }

  // Agraga un protocolo de nivel 3 o que opera en ese nivel (IP, ICMP, ARP). Deberia chequearse
  public static void addProtocol(Integer num, Object prot) {
    protocols.put(num, prot);
  }

  // Dado un numero de protocolo, devuelve su referencia
  public static Object getProtocol(Integer num) {return (Object) protocols.get(num);}

/*
  public String toString() {
    Set<String> keySet = ifaces.keySet();
    for (String elementKey : keySet) {
      Interface i = ifaces.get(elementKey);
      System.out.println("Iface: "+ i.toString());
    }
    return "eee";
  }
*/
}



