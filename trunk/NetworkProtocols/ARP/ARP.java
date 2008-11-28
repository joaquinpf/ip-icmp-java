package NetworkProtocols.ARP;

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

//

public class ARP implements ProtocolInterface {

  public ARP(Interfaces ifs, NetworkProtocols nt) throws NodeException {
  }

  // Metodo invocado por la interfaz para agregar un evento a la cola remota
  public void addRem(eventoN3 x) { }

  // Metodo invocado por el nivel superior para agregar un evento a la cola local
  public void addLoc(eventoN3 x) { }
}



