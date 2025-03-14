package nl.fontys.s3.ticketmaster.configuration;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import com.stripe.Stripe;
import jakarta.annotation.PostConstruct;
import org.springframework.context.annotation.Profile;

@Configuration
@Profile("!test")
public class StripeConfig {
    @Value("${stripe.api.secret-key}")
    private String secretKey;

    @PostConstruct
    public void init() {
        initializeStripeKey(secretKey);
    }

    private static synchronized void initializeStripeKey(String apiKey) {
        Stripe.apiKey = apiKey;
    }
}