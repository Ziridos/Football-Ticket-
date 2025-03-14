package nl.fontys.s3.ticketmaster.business.exception;

public class InvalidSeatException extends RuntimeException {
  public InvalidSeatException(String message) {
    super(message);
  }
}