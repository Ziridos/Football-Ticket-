package nl.fontys.s3.ticketmaster.business.exception;

public class InvalidBoxException extends RuntimeException {
    public InvalidBoxException(String message) {
        super(message);
    }
}
