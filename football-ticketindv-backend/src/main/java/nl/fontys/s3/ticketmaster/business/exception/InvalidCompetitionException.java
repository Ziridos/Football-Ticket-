package nl.fontys.s3.ticketmaster.business.exception;

public class InvalidCompetitionException extends RuntimeException {
    public InvalidCompetitionException(String message) {
        super(message);
    }
}
