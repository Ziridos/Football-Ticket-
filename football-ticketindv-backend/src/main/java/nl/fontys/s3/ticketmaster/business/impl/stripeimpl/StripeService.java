package nl.fontys.s3.ticketmaster.business.impl.stripeimpl;

import com.stripe.exception.SignatureVerificationException;
import com.stripe.exception.StripeException;
import com.stripe.model.Event;
import com.stripe.model.PaymentIntent;
import com.stripe.net.Webhook;
import com.stripe.param.PaymentIntentCreateParams;
import lombok.extern.slf4j.Slf4j;
import nl.fontys.s3.ticketmaster.business.exception.InvalidMatchException;
import nl.fontys.s3.ticketmaster.business.exception.PaymentProcessingException;
import nl.fontys.s3.ticketmaster.business.interfaces.emailinterfaces.EmailServiceI;
import nl.fontys.s3.ticketmaster.business.interfaces.stripeinterfaces.StripeServiceI;
import nl.fontys.s3.ticketmaster.business.interfaces.ticketinterfaces.CreateTicketUseCase;
import nl.fontys.s3.ticketmaster.business.interfaces.websockets.SelectedSeatsService;
import nl.fontys.s3.ticketmaster.domain.ticket.CreateTicketRequest;
import nl.fontys.s3.ticketmaster.domain.stripe.CreatePaymentRequest;
import nl.fontys.s3.ticketmaster.domain.stripe.CreatePaymentResponse;
import nl.fontys.s3.ticketmaster.domain.ticket.CreateTicketResponse;
import nl.fontys.s3.ticketmaster.persitence.MatchRepository;
import nl.fontys.s3.ticketmaster.persitence.UserRepository;
import nl.fontys.s3.ticketmaster.persitence.entity.MatchEntity;
import nl.fontys.s3.ticketmaster.persitence.entity.SeatEntity;
import nl.fontys.s3.ticketmaster.persitence.entity.UserEntity;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Profile;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.core.type.TypeReference;

import java.time.format.DateTimeFormatter;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Slf4j
@Service
@Profile("!test")
public class StripeService implements StripeServiceI {
    @Value("${stripe.webhook.secret}")
    private String endpointSecret;

    private static final String METADATA_TOTAL_PRICE = "totalPrice";

    private final CreateTicketUseCase createTicketUseCase;
    private final MatchRepository matchRepository;
    private final UserRepository userRepository;
    private final EmailServiceI emailService;
    private final SelectedSeatsService selectedSeatsService;

    public StripeService(CreateTicketUseCase createTicketUseCase, MatchRepository matchRepository, UserRepository userRepository, EmailServiceI emailService, SelectedSeatsService selectedSeatsService) {
        this.createTicketUseCase = createTicketUseCase;
        this.matchRepository = matchRepository;
        this.userRepository = userRepository;
        this.emailService = emailService;
        this.selectedSeatsService = selectedSeatsService;
    }

    @Override
    public CreatePaymentResponse createPaymentIntent(CreatePaymentRequest request) {
        try {
            log.info("Creating payment intent for match: {}, seats: {}, user: {}",
                    request.getMatchId(), request.getSeatIds(), request.getUserId());

            Long amount = calculateOrderAmount(request);
            log.debug("Calculated order amount: {} cents", amount);

            PaymentIntentCreateParams params = PaymentIntentCreateParams.builder()
                    .setAmount(amount)
                    .setCurrency("eur")
                    .setAutomaticPaymentMethods(
                            PaymentIntentCreateParams.AutomaticPaymentMethods
                                    .builder()
                                    .setEnabled(true)
                                    .build()
                    )
                    .putMetadata("matchId", request.getMatchId().toString())
                    .putMetadata("seatIds", String.join(",", request.getSeatIds().stream()
                            .map(String::valueOf)
                            .toList()))
                    .putMetadata("userId", request.getUserId().toString())
                    .putMetadata(METADATA_TOTAL_PRICE, String.valueOf(amount / 100.0))
                    .build();

            PaymentIntent paymentIntent = PaymentIntent.create(params);
            log.info("Successfully created payment intent: {}", paymentIntent.getId());

            return new CreatePaymentResponse(paymentIntent.getClientSecret());
        } catch (StripeException e) {
            log.error("Error creating payment intent: {}", e.getMessage(), e);
            throw new PaymentProcessingException("Could not process payment: " + e.getMessage());
        }
    }

    @Override
    public ResponseEntity<String> handleWebhook(String payload, String sigHeader) {
        if (sigHeader == null) {
            log.error("Stripe signature header is missing");
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Stripe signature header is missing");
        }

        log.info("Received webhook with signature: {}", sigHeader);
        log.debug("Webhook payload: {}", payload);

        try {
            log.debug("Using webhook secret starting with: {}",
                    endpointSecret != null ? endpointSecret.substring(0, Math.min(10, endpointSecret.length())) + "..." : "null");

            Event event = Webhook.constructEvent(payload, sigHeader, endpointSecret);
            log.info("Successfully verified Stripe signature for event type: {}", event.getType());

            switch (event.getType()) {
                case "payment_intent.succeeded":
                    return processPaymentIntentSucceeded(payload);

                case "charge.succeeded":
                    log.info("Charge succeeded event received, skipping ticket creation as it's handled by payment_intent.succeeded");
                    break;

                case "payment_intent.created":
                    log.info("Payment intent created event received");
                    break;

                default:
                    log.info("Unhandled event type: {}", event.getType());
            }

            return ResponseEntity.ok().body("Webhook processed successfully");

        } catch (SignatureVerificationException e) {
            log.error("Invalid Stripe signature. Error: {}", e.getMessage(), e);
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Invalid signature");
        } catch (Exception e) {
            log.error("Error processing webhook: {}", e.getMessage(), e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Error processing webhook: " + e.getMessage());
        }
    }

    private ResponseEntity<String> processPaymentIntentSucceeded(String payload) {
        try {
            JsonNode jsonNode = new ObjectMapper().readTree(payload);
            JsonNode dataNode = jsonNode.get("data").get("object");

            Map<String, String> metadata = new ObjectMapper().convertValue(
                    dataNode.get("metadata"),
                    new TypeReference<Map<String, String>>() {}
            );
            long amount = dataNode.get("amount").asLong();

            log.info("Processing payment with parsed data. Amount: {}", amount);
            handleSuccessfulPayment(metadata, amount);

            return ResponseEntity.ok().body("Payment intent succeeded processed successfully");
        } catch (Exception e) {
            log.error("Error processing payment event: {}", e.getMessage(), e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Error processing payment event: " + e.getMessage());
        }
    }



    private void handleSuccessfulPayment(Map<String, String> metadata, Long amountInCents) {
        try {
            log.info("Processing payment metadata: {}", metadata);

            if (metadata == null) {
                log.error("Metadata is null in payment");
                throw new PaymentProcessingException("Payment metadata is missing");
            }

            String matchIdStr = metadata.get("matchId");
            String seatIdsStr = metadata.get("seatIds");
            String userIdStr = metadata.get("userId");

            if (matchIdStr == null || seatIdsStr == null || userIdStr == null) {
                log.error("Missing required metadata. matchId: {}, seatIds: {}, userId: {}",
                        matchIdStr, seatIdsStr, userIdStr);
                throw new PaymentProcessingException("Required payment metadata is missing");
            }

            Long matchId = Long.parseLong(matchIdStr);
            List<Long> seatIds = Arrays.stream(seatIdsStr.split(","))
                    .map(String::trim)
                    .filter(s -> !s.isEmpty())
                    .map(Long::parseLong)
                    .toList();
            Long userId = Long.parseLong(userIdStr);

            if (seatIds.isEmpty()) {
                log.error("No seat IDs found in metadata");
                throw new PaymentProcessingException("No seat IDs provided in payment metadata");
            }

            double totalPrice = metadata.containsKey(METADATA_TOTAL_PRICE)
                    ? Double.parseDouble(metadata.get(METADATA_TOTAL_PRICE))
                    : amountInCents / 100.0;

            log.info("Creating ticket for match: {}, seats: {}, user: {}, price: €{}",
                    matchId, seatIds, userId, totalPrice);

            CreateTicketRequest ticketRequest = CreateTicketRequest.builder()
                    .matchId(matchId)
                    .seatIds(seatIds)
                    .userId(userId)
                    .totalPrice(totalPrice)
                    .build();

            CreateTicketResponse createdTicket = createTicketUseCase.createTicket(ticketRequest);
            log.info("Successfully created ticket for match: {}, seats: {}, user: {}, price: €{}",
                    matchId, seatIds, userId, totalPrice);

            for (Long seatId : seatIds) {
                selectedSeatsService.removeSelectedSeat(matchId, seatId);
                log.debug("Deselected seat {} for match {} after successful purchase", seatId, matchId);
            }

            Optional<UserEntity> userOptional = userRepository.findById(userId);
            Optional<MatchEntity> matchOptional = matchRepository.findById(matchId);

            if (userOptional.isPresent() && matchOptional.isPresent()) {
                sendTicketConfirmationEmail(userOptional.get(), matchOptional.get(), seatIds, totalPrice, createdTicket);
            } else {
                log.warn("Could not send confirmation email - user or match not found. UserId: {}, MatchId: {}",
                        userId, matchId);
            }

        } catch (Exception e) {
            log.error("Failed to create ticket after successful payment. Error: {}", e.getMessage(), e);
            throw new PaymentProcessingException("Failed to create ticket: " + e.getMessage());
        }
    }

    private void sendTicketConfirmationEmail(UserEntity user, MatchEntity match, List<Long> seatIds, double totalPrice, CreateTicketResponse createdTicket) {
        try {
            String matchDetails = String.format("%s vs %s - %s",
                    match.getHomeClub().getClubName(),
                    match.getAwayClub().getClubName(),
                    match.getMatchDateTime().format(DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm")));

            List<String> seatNumbers = seatIds.stream()
                    .map(id -> "Seat " + id)
                    .toList();

            emailService.sendTicketConfirmation(
                    user.getEmail(),
                    user.getName(),
                    matchDetails,
                    seatNumbers,
                    totalPrice,
                    createdTicket.getId().toString()
            );

            log.info("Sent confirmation email for ticket {} to {}", createdTicket.getId(), user.getEmail());
        } catch (Exception e) {
            log.error("Failed to send confirmation email: {}", e.getMessage(), e);
        }
    }



    private Long calculateOrderAmount(CreatePaymentRequest request) {
        Optional<MatchEntity> matchOptional = matchRepository.findById(request.getMatchId());
        if (matchOptional.isEmpty()) {
            log.error("Match not found with ID: {}", request.getMatchId());
            throw new InvalidMatchException("Match not found");
        }

        MatchEntity match = matchOptional.get();
        Map<SeatEntity, Double> seatPrices = match.getMatchSpecificSeatPrices();

        double totalPrice = match.getAvailableSeats().entrySet().stream()
                .filter(entry -> request.getSeatIds().contains(entry.getKey().getId()))
                .mapToDouble(entry -> {
                    SeatEntity seat = entry.getKey();
                    double price = seatPrices.getOrDefault(seat, seat.getPrice());
                    log.debug("Seat {} price: €{}", seat.getId(), price);
                    return price;
                })
                .sum();

        log.debug("Calculated total price: €{}", totalPrice);

        return Math.round(totalPrice * 100);
    }
}