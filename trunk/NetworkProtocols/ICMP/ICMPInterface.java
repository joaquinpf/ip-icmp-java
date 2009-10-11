package NetworkProtocols.ICMP;

public interface ICMPInterface {
	/**
	 * Tipos de mensajes ICMP.
	 */
	public static byte ECHO_REPLY = 0, DESTINATION_UNREACHABLE = 3,
			SOURCE_QUENCH = 4, REDIRECT = 5, ECHO_REQUEST = 8,
			ROUTER_ADVERT = 9, ROUTER_SOLICIT = 10, TIME_EXCEEDED = 11,
			PARAMETER_PROBLEM = 12, TIMESTAMP = 13, TIMESTAMP_REPLY = 14,
			INFORMATION_REQUEST = 15, INFORMATION_REPLY = 16;
	// Faltan 17 y 18
	/**
	 * Etiquetas asociadas a los tipos de mensajes ICMP.
	 */

	public static String[] typeLabels = { "Echo Reply", "", "",
			"Destination Unreachable", "Source Quench", "Redirect", "", "",
			"Echo Request", "Router Advertisment", "Router Solicitation",
			"Time Exceeded", "Parameter Problem", "Timestamp Request",
			"Timestamp Reply", "Information Request", "Information Reply" };

	/**
	 * Codigos asociados con Destino Inalcanzable
	 */

	public static byte NETWORK_UNREACHABLE = 0, HOST_UNREACHABLE = 1,
			PROTOCOL_UNREACHABLE = 2, PORT_UNREACHABLE = 3,
			FRAGMENTATION_NEEDED_DF_1 = 4, SOURCE_ROUTE_FAILED = 5;
	// VER QUE FALTAN DEL 6 AL 15
	/**
	 * Etiquetas asociadas con Destino Inalcanzable
	 */

	public static String[] unreachableLabels = { "Network Unreachable",
			"Host Unreachable", "Protocol Unreachable", "Port Unreachable",
			"Fragmentation necessary but DF bit set", "Source Route failed" };
	// VER QUE FALTAN DEL 6 AL 15
	/**
	 * Codigos asociados con Redireccion de paquetes
	 */

	public static byte REDIRECT_FOR_NETWORK = 0, REDIRECT_FOR_HOST = 1,
			REDIRECT_FOR_TOSAN = 2, // Type-of-Service and Network
			REDIRECT_FOR_TOSAH = 3; // Type-of-Service and Host

	/**
	 * Etiquetas asociadas con Redireccion de Paquetes
	 */

	public static String[] redirectLabels = { "Redirect for Network",
			"Redirect for Host", "Redirect for Type-of-Service and Network",
			"Redirect for Type-of-Service and Host" };

	/**
	 * Codigos asiciados con Tiempo Excedido
	 */

	public static byte TTL_COUNT_EXCEEDED_TRANSMISION = 0,
			TTL_COUNT_EXCEEDED_REASEMBLY = 1;

	/**
	 * Etiquetas asociadas con Tiempo Excedido
	 */

	public static String[] timeExceededLabels = {
			"TTL count exceeded during transit",
			"TTL count exceeded during reassembly" };
}