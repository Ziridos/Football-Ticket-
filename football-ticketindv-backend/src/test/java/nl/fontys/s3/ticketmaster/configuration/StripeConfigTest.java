package nl.fontys.s3.ticketmaster.configuration;

import com.stripe.Stripe;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.util.ReflectionTestUtils;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(SpringExtension.class)
@TestPropertySource(properties = {
        "stripe.api.secret-key=test_secret_key_123"
})
class StripeConfigTest {

    private StripeConfig stripeConfig;
    private static final String TEST_SECRET_KEY = "test_secret_key_123";

    @BeforeEach
    void setUp() {
        stripeConfig = new StripeConfig();
        ReflectionTestUtils.setField(stripeConfig, "secretKey", TEST_SECRET_KEY);
    }

    @Test
    void init_ShouldSetStripeApiKey() {
        // Act
        stripeConfig.init();

        // Assert
        assertEquals(TEST_SECRET_KEY, Stripe.apiKey,
                "Stripe.apiKey should be set to the configured secret key");
    }

    @Test
    void verifyProfileAnnotation() {
        // Arrange
        Profile profileAnnotation = StripeConfig.class.getAnnotation(Profile.class);

        // Assert
        assertNotNull(profileAnnotation, "Class should have @Profile annotation");
        assertArrayEquals(new String[]{"!test"}, profileAnnotation.value(),
                "Profile should be set to '!test'");
    }

    @Test
    void verifyConfigurationAnnotation() {
        // Arrange
        Configuration configAnnotation = StripeConfig.class.getAnnotation(Configuration.class);

        // Assert
        assertNotNull(configAnnotation, "Class should have @Configuration annotation");
    }

    @BeforeEach
    void resetStripeApiKey() {
        // Reset Stripe.apiKey after each test to ensure clean state
        Stripe.apiKey = null;
    }
}