package nl.fontys.s3.ticketmaster.business.exception;

public class InvalidClubException extends RuntimeException {
    public InvalidClubException(String message) {
        super(message);
    }
}
