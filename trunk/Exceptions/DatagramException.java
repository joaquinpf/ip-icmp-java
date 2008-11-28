package Exceptions;

// Excepcion por datagram mal formado (valores no adecuados para los campos)
public class DatagramException extends Exception  {
  private static final long serialVersionUID = 1L;
  private static final String MESSAGE = " Datagram Exception: ";

  public DatagramException(String msg) {
    super(MESSAGE+msg);
  }
}
