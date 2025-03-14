package nl.fontys.s3.ticketmaster.business.exception;

public class InvalidTicketException extends RuntimeException {
    public InvalidTicketException(String message) {
        super(message);
    }
}
