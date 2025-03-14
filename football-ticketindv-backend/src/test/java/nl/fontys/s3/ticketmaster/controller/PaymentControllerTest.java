package nl.fontys.s3.ticketmaster.controller;

import nl.fontys.s3.ticketmaster.business.interfaces.stripeinterfaces.StripeServiceI;
import nl.fontys.s3.ticketmaster.domain.stripe.CreatePaymentRequest;
import nl.fontys.s3.ticketmaster.domain.stripe.CreatePaymentResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.ResponseEntity;

import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class PaymentControllerTest {
    @Mock
    private StripeServiceI stripeService;

    @InjectMocks
    private PaymentController paymentController;

    private CreatePaymentRequest testRequest;
    private CreatePaymentResponse testResponse;
    private static final Long TEST_MATCH_ID = 1L;
    private static final Long TEST_USER_ID = 2L;
    private static final List<Long> TEST_SEAT_IDS = Arrays.asList(1L, 2L, 3L);
    private static final String TEST_CLIENT_SECRET = "pi_test_secret_key";

    @BeforeEach
    void setUp() {
        testRequest = CreatePaymentRequest.builder()
                .matchId(TEST_MATCH_ID)
                .userId(TEST_USER_ID)
                .seatIds(TEST_SEAT_IDS)
                .build();

        testResponse = new CreatePaymentResponse(TEST_CLIENT_SECRET);
    }

    @Test
    void createPaymentIntent_ValidRequest_ReturnsClientSecret() {
        // Arrange
        when(stripeService.createPaymentIntent(any(CreatePaymentRequest.class)))
                .thenReturn(testResponse);

        // Act
        ResponseEntity<CreatePaymentResponse> response = paymentController.createPaymentIntent(testRequest);

        // Assert
        assertTrue(response.getStatusCode().is2xxSuccessful());
        assertNotNull(response.getBody());
        assertEquals(TEST_CLIENT_SECRET, response.getBody().getClientSecret());
        verify(stripeService).createPaymentIntent(testRequest);
    }

    @Test
    void handleStripeWebhook_ValidWebhook_ReturnsSuccess() {
        // Arrange
        String testPayload = "{\"type\":\"payment_intent.succeeded\"}";
        String testSignature = "test_signature";
        ResponseEntity<String> expectedResponse = ResponseEntity.ok("Webhook processed successfully");

        when(stripeService.handleWebhook(testPayload, testSignature))
                .thenReturn(expectedResponse);

        // Act
        ResponseEntity<String> response = paymentController.handleStripeWebhook(testPayload, testSignature);

        // Assert
        assertTrue(response.getStatusCode().is2xxSuccessful());
        assertEquals(expectedResponse.getBody(), response.getBody());
        verify(stripeService).handleWebhook(testPayload, testSignature);
    }

    @Test
    void createPaymentIntent_WithEmptySeatList_StillProcessesRequest() {
        // Arrange
        CreatePaymentRequest emptySeatsRequest = CreatePaymentRequest.builder()
                .matchId(TEST_MATCH_ID)
                .userId(TEST_USER_ID)
                .seatIds(List.of())
                .build();

        when(stripeService.createPaymentIntent(any(CreatePaymentRequest.class)))
                .thenReturn(testResponse);

        // Act
        ResponseEntity<CreatePaymentResponse> response = paymentController.createPaymentIntent(emptySeatsRequest);

        // Assert
        assertTrue(response.getStatusCode().is2xxSuccessful());
        assertNotNull(response.getBody());
        assertEquals(TEST_CLIENT_SECRET, response.getBody().getClientSecret());
        verify(stripeService).createPaymentIntent(emptySeatsRequest);
    }

    @Test
    void handleStripeWebhook_EmptyPayload_StillProcessesRequest() {
        // Arrange
        String emptyPayload = "";
        String testSignature = "test_signature";
        ResponseEntity<String> expectedResponse = ResponseEntity.ok("Webhook processed");

        when(stripeService.handleWebhook(emptyPayload, testSignature))
                .thenReturn(expectedResponse);

        // Act
        ResponseEntity<String> response = paymentController.handleStripeWebhook(emptyPayload, testSignature);

        // Assert
        assertTrue(response.getStatusCode().is2xxSuccessful());
        assertEquals(expectedResponse.getBody(), response.getBody());
        verify(stripeService).handleWebhook(emptyPayload, testSignature);
    }

    @Test
    void handleStripeWebhook_MissingSignature_StillProcessesRequest() {
        // Arrange
        String testPayload = "{\"type\":\"payment_intent.succeeded\"}";
        String emptySignature = "";
        ResponseEntity<String> expectedResponse = ResponseEntity.ok("Webhook processed");

        when(stripeService.handleWebhook(testPayload, emptySignature))
                .thenReturn(expectedResponse);

        // Act
        ResponseEntity<String> response = paymentController.handleStripeWebhook(testPayload, emptySignature);

        // Assert
        assertTrue(response.getStatusCode().is2xxSuccessful());
        assertEquals(expectedResponse.getBody(), response.getBody());
        verify(stripeService).handleWebhook(testPayload, emptySignature);
    }
}