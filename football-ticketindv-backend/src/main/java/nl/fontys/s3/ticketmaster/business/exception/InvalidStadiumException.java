package nl.fontys.s3.ticketmaster.business.exception;

public class InvalidStadiumException extends RuntimeException {
    public InvalidStadiumException(String message) {
        super(message);
    }
}
