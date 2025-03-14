package nl.fontys.s3.ticketmaster.business.exception;

public class LogoUploadException extends RuntimeException {
    public LogoUploadException(String message, Throwable cause) {
        super(message, cause);
    }
}