package nl.fontys.s3.ticketmaster.business.impl.stripeimpl;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.stripe.exception.SignatureVerificationException;
import com.stripe.model.PaymentIntent;
import com.stripe.net.Webhook;
import com.stripe.param.PaymentIntentCreateParams;
import nl.fontys.s3.ticketmaster.business.exception.InvalidMatchException;
import nl.fontys.s3.ticketmaster.business.interfaces.emailinterfaces.EmailServiceI;
import nl.fontys.s3.ticketmaster.business.interfaces.ticketinterfaces.CreateTicketUseCase;
import nl.fontys.s3.ticketmaster.business.interfaces.websockets.SelectedSeatsService;
import nl.fontys.s3.ticketmaster.domain.stripe.CreatePaymentRequest;
import nl.fontys.s3.ticketmaster.domain.stripe.CreatePaymentResponse;
import nl.fontys.s3.ticketmaster.domain.ticket.CreateTicketRequest;
import nl.fontys.s3.ticketmaster.domain.ticket.CreateTicketResponse;
import nl.fontys.s3.ticketmaster.persitence.MatchRepository;
import nl.fontys.s3.ticketmaster.persitence.UserRepository;
import nl.fontys.s3.ticketmaster.persitence.entity.ClubEntity;
import nl.fontys.s3.ticketmaster.persitence.entity.MatchEntity;
import nl.fontys.s3.ticketmaster.persitence.entity.SeatEntity;
import nl.fontys.s3.ticketmaster.persitence.entity.UserEntity;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockedStatic;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.util.ReflectionTestUtils;

import java.time.LocalDateTime;
import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class StripeServiceTest {
    @Mock
    private CreateTicketUseCase createTicketUseCase;
    @Mock
    private MatchRepository matchRepository;
    @Mock
    private UserRepository userRepository;
    @Mock
    private EmailServiceI emailService;
    @Mock
    private PaymentIntent mockPaymentIntent;

    @Mock
    private SelectedSeatsService selectedSeatsService;

    @InjectMocks
    private StripeService stripeService;


    private static final Long TEST_MATCH_ID = 1L;
    private static final Long TEST_SEAT_ID = 2L;
    private static final Long TEST_USER_ID = 3L;
    private static final String TEST_CLIENT_SECRET = "test_client_secret";
    private static final String TEST_WEBHOOK_SECRET = "whsec_test_secret";
    private static final double TEST_PRICE = 10.0;

    private MatchEntity testMatch;
    private UserEntity testUser;
    private SeatEntity testSeat;
    private CreateTicketResponse testTicketResponse;

    @BeforeEach
    void setUp() {
        ReflectionTestUtils.setField(stripeService, "endpointSecret", TEST_WEBHOOK_SECRET);

        // Setup test entities
        testSeat = SeatEntity.builder()
                .id(TEST_SEAT_ID)
                .price(TEST_PRICE)
                .build();

        ClubEntity homeClub = ClubEntity.builder().clubName("Home Club").build();
        ClubEntity awayClub = ClubEntity.builder().clubName("Away Club").build();

        testMatch = MatchEntity.builder()
                .id(TEST_MATCH_ID)
                .homeClub(homeClub)
                .awayClub(awayClub)
                .matchDateTime(LocalDateTime.now().plusDays(7))
                .build();

        // Initialize the available seats map
        Map<SeatEntity, Boolean> availableSeats = new HashMap<>();
        availableSeats.put(testSeat, true);
        testMatch.setAvailableSeats(availableSeats);

        // Initialize the match specific seat prices map
        Map<SeatEntity, Double> matchSpecificPrices = new HashMap<>();
        matchSpecificPrices.put(testSeat, TEST_PRICE);
        testMatch.setMatchSpecificSeatPrices(matchSpecificPrices);

        testUser = UserEntity.builder()
                .id(TEST_USER_ID)
                .name("Test User")
                .email("test@example.com")
                .build();

        testTicketResponse = CreateTicketResponse.builder()
                .id(1L)
                .userId(TEST_USER_ID)
                .matchId(TEST_MATCH_ID)
                .seatIds(List.of(TEST_SEAT_ID))
                .purchaseDateTime(LocalDateTime.now())
                .totalPrice(TEST_PRICE)
                .build();
    }

    @Test
    void createPaymentIntent_ValidRequest_Success()  {
        // Arrange
        CreatePaymentRequest request = CreatePaymentRequest.builder()
                .matchId(TEST_MATCH_ID)
                .seatIds(List.of(TEST_SEAT_ID))
                .userId(TEST_USER_ID)
                .build();

        when(matchRepository.findById(TEST_MATCH_ID)).thenReturn(Optional.of(testMatch));
        when(mockPaymentIntent.getClientSecret()).thenReturn(TEST_CLIENT_SECRET);

        try (MockedStatic<PaymentIntent> mockedStatic = mockStatic(PaymentIntent.class)) {
            // Explicitly specify the PaymentIntentCreateParams version
            mockedStatic.when(() -> PaymentIntent.create(any(PaymentIntentCreateParams.class)))
                    .thenReturn(mockPaymentIntent);

            // Act
            CreatePaymentResponse response = stripeService.createPaymentIntent(request);

            // Assert
            assertNotNull(response);
            assertEquals(TEST_CLIENT_SECRET, response.getClientSecret());
            verify(mockPaymentIntent).getClientSecret();
        }
    }

    @Test
    void createPaymentIntent_InvalidMatch_ThrowsException() {
        // Arrange
        CreatePaymentRequest request = CreatePaymentRequest.builder()
                .matchId(TEST_MATCH_ID)
                .seatIds(List.of(TEST_SEAT_ID))
                .userId(TEST_USER_ID)
                .build();

        when(matchRepository.findById(TEST_MATCH_ID)).thenReturn(Optional.empty());

        // Act & Assert
        assertThrows(InvalidMatchException.class,
                () -> stripeService.createPaymentIntent(request));
    }

    @Test
    void handleWebhook_ValidPaymentIntent_Success() {
        // Arrange
        String payload = createTestPayloadJson();
        String sigHeader = "test_signature";

        try (MockedStatic<Webhook> mockedWebhook = mockStatic(Webhook.class)) {
            mockedWebhook.when(() -> Webhook.constructEvent(eq(payload), eq(sigHeader), eq(TEST_WEBHOOK_SECRET)))
                    .thenReturn(createTestEvent());

            when(matchRepository.findById(TEST_MATCH_ID)).thenReturn(Optional.of(testMatch));
            when(userRepository.findById(TEST_USER_ID)).thenReturn(Optional.of(testUser));
            when(createTicketUseCase.createTicket(any(CreateTicketRequest.class))).thenReturn(testTicketResponse);

            // Act
            ResponseEntity<String> response = stripeService.handleWebhook(payload, sigHeader);

            // Assert
            assertEquals(HttpStatus.OK, response.getStatusCode());
            verify(createTicketUseCase).createTicket(any());
            verify(selectedSeatsService).removeSelectedSeat(TEST_MATCH_ID, TEST_SEAT_ID);
            verify(emailService).sendTicketConfirmation(
                    eq(testUser.getEmail()),
                    eq(testUser.getName()),
                    anyString(),
                    eq(List.of("Seat " + TEST_SEAT_ID)),
                    eq(TEST_PRICE),
                    eq(testTicketResponse.getId().toString())
            );

        }
    }

    @Test
    void handleWebhook_MissingSignature_ReturnsUnauthorized() {
        // Act
        ResponseEntity<String> response = stripeService.handleWebhook("payload", null);

        // Assert
        assertEquals(HttpStatus.UNAUTHORIZED, response.getStatusCode());
        assertTrue(response.getBody().contains("Stripe signature header is missing"));
    }

    @Test
    void handleWebhook_InvalidSignature_ReturnsUnauthorized() {
        // Arrange
        String payload = "test_payload";
        String sigHeader = "invalid_signature";

        try (MockedStatic<Webhook> mockedWebhook = mockStatic(Webhook.class)) {
            mockedWebhook.when(() -> Webhook.constructEvent(any(), any(), any()))
                    .thenThrow(new SignatureVerificationException("Invalid signature", sigHeader));

            // Act
            ResponseEntity<String> response = stripeService.handleWebhook(payload, sigHeader);

            // Assert
            assertEquals(HttpStatus.UNAUTHORIZED, response.getStatusCode());
            assertEquals("Invalid signature", response.getBody());
        }
    }

    private String createTestPayloadJson() {
        Map<String, Object> metadata = new HashMap<>();
        metadata.put("matchId", TEST_MATCH_ID.toString());
        metadata.put("seatIds", TEST_SEAT_ID.toString());
        metadata.put("userId", TEST_USER_ID.toString());
        metadata.put("totalPrice", String.valueOf(TEST_PRICE));

        Map<String, Object> object = new HashMap<>();
        object.put("metadata", metadata);
        object.put("amount", 1000); // Amount in cents

        Map<String, Object> data = new HashMap<>();
        data.put("object", object);

        Map<String, Object> event = new HashMap<>();
        event.put("type", "payment_intent.succeeded");
        event.put("data", data);

        try {
            return new ObjectMapper().writeValueAsString(event);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    private com.stripe.model.Event createTestEvent() {
        com.stripe.model.Event event = new com.stripe.model.Event();
        event.setType("payment_intent.succeeded");
        return event;
    }
}