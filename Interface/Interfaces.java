package Interface;

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
  
public class Interfaces {

  public Map<String, Interface> ifaces = null;  // Tabla con las interfaces

  public Interfaces(){
    ifaces=new HashMap<String, Interface>();
  }

  // Agrega una interfaz a la lista. Se deberia chequear validez
  public Interface addInterface(String name, IpAddress addr, Mask msk, Integer mtu ) throws NodeException  {
    Interface ifc1 = new Interface(name, addr, msk, mtu);
    ifaces.put(name, ifc1);
    return ifc1;
  }


  public String toString() {
    Set<String> keySet = ifaces.keySet();
    for (String elementKey : keySet) {
      Interface i = ifaces.get(elementKey);
      System.out.println("Iface: "+ i.toString());
    }
    return "eee";
  }

}



