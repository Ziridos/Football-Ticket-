package nl.fontys.s3.ticketmaster.controller;

import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import nl.fontys.s3.ticketmaster.business.interfaces.stripeinterfaces.StripeServiceI;
import nl.fontys.s3.ticketmaster.domain.stripe.CreatePaymentRequest;
import nl.fontys.s3.ticketmaster.domain.stripe.CreatePaymentResponse;

@RestController
@RequestMapping("/api/payments")
@CrossOrigin(origins = "http://localhost:5173")
@AllArgsConstructor
public class PaymentController {
    private final StripeServiceI stripeService;

    @PostMapping("/create-payment-intent")
    public ResponseEntity<CreatePaymentResponse> createPaymentIntent(@RequestBody CreatePaymentRequest request) {
        return ResponseEntity.ok(stripeService.createPaymentIntent(request));
    }

    @PostMapping("/webhook")
    public ResponseEntity<String> handleStripeWebhook(@RequestBody String payload,
                                                      @RequestHeader("Stripe-Signature") String sigHeader) {
        return stripeService.handleWebhook(payload, sigHeader);
    }
}