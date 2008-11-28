package Exceptions;

// Excepcion por direcciones
public class RoutingException extends Exception  {
  private static final long serialVersionUID = 1L;
  private static final String MESSAGE = " Routing Exception: ";

  public RoutingException(String msg) {
    super(MESSAGE+msg);
  }
}
