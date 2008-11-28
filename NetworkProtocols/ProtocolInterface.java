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
/*

*/

public interface ProtocolInterface  {
  public void addRem(eventoN3 x); // Agrega un evento en la cola de eventos remotos
  public void addLoc(eventoN3 x); // Agrega un evento en la cola de eventos locales

}
