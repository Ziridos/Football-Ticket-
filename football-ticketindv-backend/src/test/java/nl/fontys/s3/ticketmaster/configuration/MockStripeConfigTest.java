package nl.fontys.s3.ticketmaster.configuration;

import com.stripe.Stripe;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeEach;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;

import static org.junit.jupiter.api.Assertions.*;

class MockStripeConfigTest {

    private static final String EXPECTED_MOCK_KEY = "sk_test_mock12345";

    @BeforeEach
    void resetStripeApiKey() {
        // Reset Stripe.apiKey before each test to ensure clean state
        Stripe.apiKey = null;
    }

    @Test
    void constructor_ShouldSetMockStripeApiKey() {
        // Act
        new MockStripeConfig();

        // Assert
        assertEquals(EXPECTED_MOCK_KEY, Stripe.apiKey,
                "Stripe.apiKey should be set to the mock key");
    }

    @Test
    void verifyProfileAnnotation() {
        // Arrange
        Profile profileAnnotation = MockStripeConfig.class.getAnnotation(Profile.class);

        // Assert
        assertNotNull(profileAnnotation, "Class should have @Profile annotation");
        assertArrayEquals(new String[]{"test"}, profileAnnotation.value(),
                "Profile should be set to 'test'");
    }

    @Test
    void verifyConfigurationAnnotation() {
        // Arrange
        Configuration configAnnotation = MockStripeConfig.class.getAnnotation(Configuration.class);

        // Assert
        assertNotNull(configAnnotation, "Class should have @Configuration annotation");
    }
}