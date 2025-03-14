package nl.fontys.s3.ticketmaster.business.exception;

public class InvalidBlockException extends RuntimeException {
    public InvalidBlockException(String message) {
        super(message);
    }
}
