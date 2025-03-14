package nl.fontys.s3.ticketmaster.business.exception;

public class SameClubException extends RuntimeException {
    public SameClubException(String message) {
        super(message);
    }
}
