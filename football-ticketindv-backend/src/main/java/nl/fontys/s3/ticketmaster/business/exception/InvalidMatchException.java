package nl.fontys.s3.ticketmaster.business.exception;

public class InvalidMatchException extends RuntimeException {
    public InvalidMatchException(String message) {
        super(message);
    }
}
