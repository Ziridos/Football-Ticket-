package nl.fontys.s3.ticketmaster.business.exception;

public class EmailAlreadyExistsException extends RuntimeException {
    public EmailAlreadyExistsException() {
        super("A user with this email already exists.");
    }
}
