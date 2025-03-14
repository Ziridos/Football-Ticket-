package nl.fontys.s3.ticketmaster.configuration;

import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import com.stripe.Stripe;

@Configuration
@Profile("test")
public class MockStripeConfig {
    private static final String MOCK_API_KEY = "sk_test_mock12345";


    MockStripeConfig() {
        initializeMockStripeKey();
    }

    private static synchronized void initializeMockStripeKey() {
        Stripe.apiKey = MOCK_API_KEY;
    }


    static String getMockApiKey() {
        return MOCK_API_KEY;
    }
}