package nl.fontys.s3.ticketmaster.business.interfaces.stripeinterfaces;

import nl.fontys.s3.ticketmaster.domain.stripe.CreatePaymentRequest;
import nl.fontys.s3.ticketmaster.domain.stripe.CreatePaymentResponse;
import org.springframework.http.ResponseEntity;

public interface StripeServiceI {
    CreatePaymentResponse createPaymentIntent(CreatePaymentRequest request);
    ResponseEntity<String> handleWebhook(String payload, String sigHeader);
}