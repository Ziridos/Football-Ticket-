package nl.fontys.s3.ticketmaster.business.impl.stripeimpl;

import lombok.extern.slf4j.Slf4j;
import nl.fontys.s3.ticketmaster.business.interfaces.stripeinterfaces.StripeServiceI;
import nl.fontys.s3.ticketmaster.domain.stripe.CreatePaymentRequest;
import nl.fontys.s3.ticketmaster.domain.stripe.CreatePaymentResponse;
import org.springframework.context.annotation.Profile;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

@Service
@Profile("test")
@Slf4j
public class MockStripeService implements StripeServiceI {
    private static final String MOCK_CLIENT_SECRET = "mock_client_secret_test_12345";

    @Override
    public CreatePaymentResponse createPaymentIntent(CreatePaymentRequest request) {
        log.info("MOCK: Creating payment intent for match: {}, seats: {}, user: {}",
                request.getMatchId(), request.getSeatIds(), request.getUserId());


        return new CreatePaymentResponse(MOCK_CLIENT_SECRET);
    }

    @Override
    public ResponseEntity<String> handleWebhook(String payload, String sigHeader) {
        log.info("MOCK: Received webhook with signature: {}", sigHeader);
        log.info("MOCK: Webhook payload: {}", payload);


        return ResponseEntity.ok("MOCK: Webhook processed successfully");
    }
}