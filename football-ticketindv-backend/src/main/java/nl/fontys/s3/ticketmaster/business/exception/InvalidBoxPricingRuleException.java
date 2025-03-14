package nl.fontys.s3.ticketmaster.business.exception;

public class InvalidBoxPricingRuleException extends RuntimeException {
    public InvalidBoxPricingRuleException(String message) {
        super(message);
    }
}