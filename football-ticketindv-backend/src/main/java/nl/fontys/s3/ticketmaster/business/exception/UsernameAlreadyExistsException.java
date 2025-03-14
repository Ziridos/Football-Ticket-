package nl.fontys.s3.ticketmaster.business.exception;

public class UsernameAlreadyExistsException extends RuntimeException {
    public UsernameAlreadyExistsException() {
        super("A user with this username already exists.");
    }
}
