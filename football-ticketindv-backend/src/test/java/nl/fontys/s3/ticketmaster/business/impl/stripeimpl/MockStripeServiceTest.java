package nl.fontys.s3.ticketmaster.business.impl.stripeimpl;

import nl.fontys.s3.ticketmaster.domain.stripe.CreatePaymentRequest;
import nl.fontys.s3.ticketmaster.domain.stripe.CreatePaymentResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.context.annotation.Profile;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class MockStripeServiceTest {
    private MockStripeService mockStripeService;
    private static final String EXPECTED_CLIENT_SECRET = "mock_client_secret_test_12345";
    private static final Long TEST_MATCH_ID = 1L;
    private static final Long TEST_SEAT_ID = 2L;
    private static final Long TEST_USER_ID = 3L;

    @BeforeEach
    void setUp() {
        mockStripeService = new MockStripeService();
    }

    @Test
    void createPaymentIntent_ShouldReturnMockClientSecret() {
        // Arrange
        CreatePaymentRequest request = CreatePaymentRequest.builder()
                .matchId(TEST_MATCH_ID)
                .seatIds(List.of(TEST_SEAT_ID))
                .userId(TEST_USER_ID)
                .build();

        // Act
        CreatePaymentResponse response = mockStripeService.createPaymentIntent(request);

        // Assert
        assertNotNull(response);
        assertEquals(EXPECTED_CLIENT_SECRET, response.getClientSecret());
    }

    @Test
    void createPaymentIntent_WithNullValues_StillReturnsClientSecret() {
        // Arrange
        CreatePaymentRequest request = CreatePaymentRequest.builder()
                .matchId(null)
                .seatIds(null)
                .userId(null)
                .build();

        // Act
        CreatePaymentResponse response = mockStripeService.createPaymentIntent(request);

        // Assert
        assertNotNull(response);
        assertEquals(EXPECTED_CLIENT_SECRET, response.getClientSecret());
    }

    @Test
    void handleWebhook_WithValidInput_ReturnsSuccessResponse() {
        // Arrange
        String payload = "test_payload";
        String sigHeader = "test_signature";

        // Act
        ResponseEntity<String> response = mockStripeService.handleWebhook(payload, sigHeader);

        // Assert
        assertNotNull(response);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals("MOCK: Webhook processed successfully", response.getBody());
    }

    @Test
    void handleWebhook_WithNullInput_StillReturnsSuccessResponse() {
        // Act
        ResponseEntity<String> response = mockStripeService.handleWebhook(null, null);

        // Assert
        assertNotNull(response);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals("MOCK: Webhook processed successfully", response.getBody());
    }

    @Test
    void verifyProfileAnnotation() {
        // Arrange
        Profile profileAnnotation = MockStripeService.class.getAnnotation(Profile.class);

        // Assert
        assertNotNull(profileAnnotation, "Class should have @Profile annotation");
        assertArrayEquals(new String[]{"test"}, profileAnnotation.value(),
                "Profile should be set to 'test'");
    }

    @Test
    void verifyServiceAnnotation() {
        // Assert
        assertNotNull(MockStripeService.class.getAnnotation(Service.class),
                "Class should have @Service annotation");
    }
}