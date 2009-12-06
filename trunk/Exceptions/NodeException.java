package Exceptions;

public class NodeException extends Exception {
	/**
	 * 
	 */
	private static final long serialVersionUID = -2355133663292625103L;
	public static final int SOCKETERROR = 5;
	public static final int ADDRESSERROR = 10;
	public static final int UNKNOWNHOST = 3;
	public static final int BADPORT = 4;
	public static final int PARAMETROS = 1;
	public static final int NETWORK_UNREACHABLE = 51;

	public static final int CONEXION_EXISTENTE = 7;
	public static final int CONEXION_INEXISTENTE = 8;
	public static final int CONEXION_ILEGAL = 20;
	public static final int CLOSED_BY_USER = 21;
	public static final int CONNECTION_RESET = 18;
	public static final int CONEXION_RECHAZADA = 11;

	public static final int CONNECTION_CLOSING = 52;

	public static final int DOBLETCP = 2;

	public static final int EXCEPCION_ENVIO = 6;
	public static final int ESPERANDO_CONEXION_REMOTA = 9;
	public static final int RESET_DE_CONEXION = 12;
	public static final int CONEXION_NO_OPERATIVA = 13;
	public static final int RECURSOS_INSUFICIENTES = 14;
	public static final int DATOS = 15;
	public static final int RECEIVE_ERROR = 16;
	public static final int SIN_DATOS = 17;
	public static final int RESETTING = 19;
	int tipo;
	String masinfo = "";

	public NodeException(int tip) {
		tipo = tip;
		show();
	}

	public NodeException(int tip, String mi) {
		super(mi);
		// tipo = tip;
		// masinfo = " (" + mi + ").";
		// show();
	}

	public void show() {
		switch (tipo) {
		case SOCKETERROR:
			System.out.println("ERROR en creacion de socket" + masinfo);
			break;
		case ADDRESSERROR:
			System.out.println("ERROR en direccion IP" + masinfo);
			break;
		case UNKNOWNHOST:
			System.out.println("ERROR, Host desconocido" + masinfo);
			break;
		case BADPORT:
			System.out.println("ERROR, Port erroneo" + masinfo);
			break;
		case PARAMETROS:
			System.out
					.println("ERROR EN NUMERO DE PARAMETROS. Usos:" + masinfo);
			System.out
					.println("  TCP host_local, port_local, host_remoto, port_remoto Tipo");
			break;
		case NETWORK_UNREACHABLE:
			System.out.println("Network unreachable" + masinfo);
			break;

		case CONEXION_EXISTENTE:
			System.out.println("ERROR: connection already exists" + masinfo);
			break;
		case CONEXION_INEXISTENTE:
			System.out.println("ERROR: connection does not exist" + masinfo);
			break;
		case CONEXION_ILEGAL:
			System.out.println("ERROR: connection illegal for this process"
					+ masinfo);
			break;

		case CLOSED_BY_USER:
			System.out.println("ERROR: closed by user" + masinfo);
			break;

		case CONNECTION_RESET:
			System.out.println("ERROR: connection reset" + masinfo);
			break;

		case CONEXION_RECHAZADA:
			System.out.println("ERROR: connection refused" + masinfo);
			break;

		case CONNECTION_CLOSING:
			System.out.println("ERROR connection closing" + masinfo);
			break;

		case DOBLETCP:
			System.out
					.println("ERROR, se permite una unica instancia de TCP por aplicacion"
							+ masinfo);
			break;

		case EXCEPCION_ENVIO:
			System.out.println("ERROR en envio de informacion" + masinfo);
			break;

		case ESPERANDO_CONEXION_REMOTA:
			System.out.println("ERROR esperando por conexion remota" + masinfo);
			break;

		case RESET_DE_CONEXION:
			System.out.println("ERROR conexion reseteada" + masinfo);
			break;

		case CONEXION_NO_OPERATIVA:
			System.out.println("ERROR conexion no operativa" + masinfo);
			break;

		case RECURSOS_INSUFICIENTES:
			System.out.println("ERROR recursos insuficientes" + masinfo);
			break;

		case DATOS:
			System.out.println("ERROR en datos" + masinfo);
			break;

		case RECEIVE_ERROR:
			System.out.println("ERROR en recepcion de datos" + masinfo);
			break;

		case SIN_DATOS:
			System.out.println("ERROR en recepcion de datos, sin datos"
					+ masinfo);
			break;

		case RESETTING:
			System.out.println("ERROR TCP se esta reseteando..." + masinfo);
			break;

		default:
			System.out.println("Excepcion TCP desconocida ????" + masinfo);
		}
	}

}
