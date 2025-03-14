package nl.fontys.s3.ticketmaster.business.impl.ticketimpl;

import nl.fontys.s3.ticketmaster.business.exception.InvalidSeatException;
import nl.fontys.s3.ticketmaster.business.interfaces.matchinterfaces.ApplyPricingRulesUseCase;
import nl.fontys.s3.ticketmaster.business.interfaces.matchinterfaces.MatchUpdateService;
import nl.fontys.s3.ticketmaster.business.interfaces.ticketinterfaces.TicketCreationService;
import nl.fontys.s3.ticketmaster.business.interfaces.ticketinterfaces.TicketMapper;
import nl.fontys.s3.ticketmaster.business.interfaces.ticketinterfaces.TicketValidationService;
import nl.fontys.s3.ticketmaster.domain.ticket.CreateTicketRequest;
import nl.fontys.s3.ticketmaster.domain.ticket.CreateTicketResponse;
import nl.fontys.s3.ticketmaster.persitence.MatchRepository;
import nl.fontys.s3.ticketmaster.persitence.entity.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class CreateTicketUseCaseImplTest {
    @Mock
    private TicketValidationService ticketValidationService;
    @Mock
    private TicketCreationService ticketCreationService;
    @Mock
    private MatchUpdateService matchUpdateService;
    @Mock
    private ApplyPricingRulesUseCase applyPricingRulesUseCase;
    @Mock
    private TicketMapper ticketMapper;
    @Mock
    private MatchRepository matchRepository;

    @InjectMocks
    private CreateTicketUseCaseImpl createTicketUseCase;

    private CreateTicketRequest request;
    private MatchEntity matchEntity;
    private TicketEntity ticketEntity;
    private CreateTicketResponse expectedResponse;
    private List<Long> seatIds;
    private Map<SeatEntity, Boolean> availableSeats;

    @BeforeEach
    void setUp() {
        // Initialize test data
        seatIds = Arrays.asList(1L, 2L);
        request = CreateTicketRequest.builder()
                .matchId(1L)
                .userId(1L)
                .seatIds(seatIds)
                .build();

        // Create available seats map
        availableSeats = new HashMap<>();
        SeatEntity seat1 = createSeatEntity(1L);
        SeatEntity seat2 = createSeatEntity(2L);
        availableSeats.put(seat1, true);
        availableSeats.put(seat2, true);

        // Create match entity
        matchEntity = createMatchEntity(1L, availableSeats);

        // Create ticket entity
        ticketEntity = createTicketEntity();

        // Create expected response
        expectedResponse = CreateTicketResponse.builder()
                .id(1L)
                .build();
    }

    @Test
    void createTicket_ValidRequest_ReturnsTicketResponse() {
        // Arrange
        when(matchRepository.findById(request.getMatchId())).thenReturn(Optional.of(matchEntity));
        when(ticketCreationService.createTicket(request)).thenReturn(ticketEntity);
        when(ticketMapper.toCreateTicketResponse(ticketEntity)).thenReturn(expectedResponse);

        // Act
        CreateTicketResponse response = createTicketUseCase.createTicket(request);

        // Assert
        assertNotNull(response);
        assertEquals(expectedResponse.getId(), response.getId());
        verify(ticketValidationService).validateTicketRequest(request);
        verify(matchUpdateService).updateMatchSeats(request.getMatchId(), request.getSeatIds());
        verify(applyPricingRulesUseCase).applyPricingRules(request.getMatchId());
    }

    @Test
    void createTicket_InvalidSeatRequest_ThrowsInvalidSeatException() {
        // Arrange
        doThrow(new InvalidSeatException("Invalid seat")).when(ticketValidationService).validateTicketRequest(request);

        // Act & Assert
        assertThrows(InvalidSeatException.class, () -> createTicketUseCase.createTicket(request));
        verify(ticketCreationService, never()).createTicket(any());
        verify(matchUpdateService, never()).updateMatchSeats(any(), any());
    }

    @Test
    void createTicket_MatchNotFound_ThrowsRuntimeException() {
        // Arrange
        when(matchRepository.findById(request.getMatchId())).thenReturn(Optional.empty());

        // Act & Assert
        RuntimeException exception = assertThrows(RuntimeException.class,
                () -> createTicketUseCase.createTicket(request));
        assertEquals("Match not found", exception.getMessage());
        verify(ticketCreationService, never()).createTicket(any());
    }

    @Test
    void createTicket_SeatNotAvailable_ThrowsInvalidSeatException() {
        // Arrange
        availableSeats.put(createSeatEntity(1L), false); // Make seat unavailable
        matchEntity = createMatchEntity(1L, availableSeats);
        when(matchRepository.findById(request.getMatchId())).thenReturn(Optional.of(matchEntity));

        // Act & Assert
        InvalidSeatException exception = assertThrows(InvalidSeatException.class,
                () -> createTicketUseCase.createTicket(request));
        assertTrue(exception.getMessage().contains("not available"));
        verify(ticketCreationService, never()).createTicket(any());
    }

    @Test
    void createTicket_MatchUpdateFails_ThrowsRuntimeException() {
        // Arrange
        when(matchRepository.findById(request.getMatchId())).thenReturn(Optional.of(matchEntity));
        when(ticketCreationService.createTicket(request)).thenReturn(ticketEntity);
        doThrow(new RuntimeException("Update failed")).when(matchUpdateService)
                .updateMatchSeats(request.getMatchId(), request.getSeatIds());

        // Act & Assert
        RuntimeException exception = assertThrows(RuntimeException.class,
                () -> createTicketUseCase.createTicket(request));
        assertEquals("Failed to update match seats", exception.getMessage());
    }

    @Test
    void createTicket_PricingRuleApplicationFails_CompletesPurchase() {
        // Arrange
        when(matchRepository.findById(request.getMatchId())).thenReturn(Optional.of(matchEntity));
        when(ticketCreationService.createTicket(request)).thenReturn(ticketEntity);
        when(ticketMapper.toCreateTicketResponse(ticketEntity)).thenReturn(expectedResponse);
        doThrow(new RuntimeException("Pricing rules failed")).when(applyPricingRulesUseCase)
                .applyPricingRules(request.getMatchId());

        // Act
        CreateTicketResponse response = createTicketUseCase.createTicket(request);

        // Assert
        assertNotNull(response);
        assertEquals(expectedResponse.getId(), response.getId());
        // Verify that the ticket was still created despite pricing rules failure
        verify(ticketCreationService).createTicket(request);
        verify(matchUpdateService).updateMatchSeats(request.getMatchId(), request.getSeatIds());
    }

    // Helper methods to create test entities
    private SeatEntity createSeatEntity(Long id) {
        return SeatEntity.builder()
                .id(id)
                .seatNumber("A" + id)
                .price(50.0)
                .build();
    }

    private MatchEntity createMatchEntity(Long id, Map<SeatEntity, Boolean> availableSeats) {
        return MatchEntity.builder()
                .id(id)
                .matchDateTime(LocalDateTime.now().plusDays(7))
                .availableSeats(availableSeats)
                .build();
    }

    private TicketEntity createTicketEntity() {
        return TicketEntity.builder()
                .id(1L)
                .purchaseDateTime(LocalDateTime.now())
                .totalPrice(100.0)
                .build();
    }
}