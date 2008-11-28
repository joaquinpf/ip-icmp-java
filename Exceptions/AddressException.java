package Exceptions;

// Excepcion por direcciones
public class AddressException extends Exception  {
  private static final long serialVersionUID = 1L;
  private static final String MESSAGE = " Address Exception: ";

  public AddressException(String msg) {
    super(MESSAGE+msg);
  }
}
