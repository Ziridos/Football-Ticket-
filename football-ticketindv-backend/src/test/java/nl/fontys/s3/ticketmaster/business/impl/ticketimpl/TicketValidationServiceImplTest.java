package nl.fontys.s3.ticketmaster.business.impl.ticketimpl;

import nl.fontys.s3.ticketmaster.business.exception.InvalidSeatException;
import nl.fontys.s3.ticketmaster.business.exception.InvalidTicketException;
import nl.fontys.s3.ticketmaster.business.interfaces.matchinterfaces.MatchService;
import nl.fontys.s3.ticketmaster.business.interfaces.matchinterfaces.MatchValidator;
import nl.fontys.s3.ticketmaster.business.interfaces.userinterfaces.UserValidator;
import nl.fontys.s3.ticketmaster.domain.ticket.CreateTicketRequest;
import nl.fontys.s3.ticketmaster.domain.ticket.UpdateTicketRequest;
import nl.fontys.s3.ticketmaster.persitence.TicketRepository;
import nl.fontys.s3.ticketmaster.persitence.entity.MatchEntity;
import nl.fontys.s3.ticketmaster.persitence.entity.SeatEntity;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class TicketValidationServiceImplTest {
    @Mock
    private UserValidator userValidator;
    @Mock
    private MatchValidator matchValidator;
    @Mock
    private TicketRepository ticketRepository;
    @Mock
    private MatchService matchService;

    @InjectMocks
    private TicketValidationServiceImpl ticketValidationService;

    private CreateTicketRequest createTicketRequest;
    private UpdateTicketRequest updateTicketRequest;
    private MatchEntity match;
    private Map<SeatEntity, Boolean> availableSeats;

    @BeforeEach
    void setUp() {
        // Create test seats
        SeatEntity seat1 = SeatEntity.builder().id(1L).seatNumber("A1").build();
        SeatEntity seat2 = SeatEntity.builder().id(2L).seatNumber("A2").build();
        SeatEntity seat3 = SeatEntity.builder().id(3L).seatNumber("A3").build();

        // Setup available seats map
        availableSeats = new HashMap<>();
        availableSeats.put(seat1, true);  // Available
        availableSeats.put(seat2, true);  // Available
        availableSeats.put(seat3, false); // Not available

        // Create match with available seats
        match = MatchEntity.builder()
                .id(1L)
                .availableSeats(availableSeats)
                .build();

        // Create test requests
        createTicketRequest = CreateTicketRequest.builder()
                .userId(1L)
                .matchId(1L)
                .seatIds(Arrays.asList(1L, 2L))
                .build();

        updateTicketRequest = UpdateTicketRequest.builder()
                .userId(1L)
                .matchId(1L)
                .seatIds(Arrays.asList(1L, 2L))
                .build();
    }

    @Test
    void validateTicketRequest_ValidRequest_NoExceptionThrown() {
        // Arrange
        doNothing().when(userValidator).validateUserExists(createTicketRequest.getUserId());
        doNothing().when(matchValidator).validateMatchExists(createTicketRequest.getMatchId());
        when(matchService.getMatchById(createTicketRequest.getMatchId())).thenReturn(Optional.of(match));

        // Act & Assert
        assertDoesNotThrow(() -> ticketValidationService.validateTicketRequest(createTicketRequest));

        // Verify
        verify(userValidator).validateUserExists(createTicketRequest.getUserId());
        verify(matchValidator).validateMatchExists(createTicketRequest.getMatchId());
        verify(matchService).getMatchById(createTicketRequest.getMatchId());
    }

    @Test
    void validateTicketRequest_InvalidUser_ThrowsException() {
        // Arrange
        doThrow(new RuntimeException("User not found"))
                .when(userValidator).validateUserExists(createTicketRequest.getUserId());

        // Act & Assert
        Exception exception = assertThrows(RuntimeException.class,
                () -> ticketValidationService.validateTicketRequest(createTicketRequest));
        assertEquals("User not found", exception.getMessage());

        // Verify that match validation was not attempted
        verify(matchValidator, never()).validateMatchExists(any());
    }

    @Test
    void validateTicketRequest_InvalidMatch_ThrowsException() {
        // Arrange
        doNothing().when(userValidator).validateUserExists(createTicketRequest.getUserId());
        doThrow(new RuntimeException("Match not found"))
                .when(matchValidator).validateMatchExists(createTicketRequest.getMatchId());

        // Act & Assert
        Exception exception = assertThrows(RuntimeException.class,
                () -> ticketValidationService.validateTicketRequest(createTicketRequest));
        assertEquals("Match not found", exception.getMessage());
    }

    @Test
    void validateTicketRequest_UnavailableSeats_ThrowsInvalidSeatException() {
        // Arrange
        CreateTicketRequest requestWithUnavailableSeat = CreateTicketRequest.builder()
                .userId(1L)
                .matchId(1L)
                .seatIds(Arrays.asList(1L, 3L)) // Seat 3 is unavailable
                .build();

        doNothing().when(userValidator).validateUserExists(requestWithUnavailableSeat.getUserId());
        doNothing().when(matchValidator).validateMatchExists(requestWithUnavailableSeat.getMatchId());
        when(matchService.getMatchById(requestWithUnavailableSeat.getMatchId())).thenReturn(Optional.of(match));

        // Act & Assert
        InvalidSeatException exception = assertThrows(InvalidSeatException.class,
                () -> ticketValidationService.validateTicketRequest(requestWithUnavailableSeat));
        assertTrue(exception.getMessage().contains("not available"));
    }

    @Test
    void validateTicketExists_ExistingTicket_NoExceptionThrown() {
        // Arrange
        when(ticketRepository.existsById(1L)).thenReturn(true);

        // Act & Assert
        assertDoesNotThrow(() -> ticketValidationService.validateTicketExists(1L));
    }

    @Test
    void validateTicketExists_NonExistingTicket_ThrowsInvalidTicketException() {
        // Arrange
        when(ticketRepository.existsById(1L)).thenReturn(false);

        // Act & Assert
        InvalidTicketException exception = assertThrows(InvalidTicketException.class,
                () -> ticketValidationService.validateTicketExists(1L));
        assertEquals("Ticket with the given ID does not exist.", exception.getMessage());
    }

    @Test
    void validateTicketUpdateRequest_ValidRequest_NoExceptionThrown() {
        // Arrange
        doNothing().when(userValidator).validateUserExists(updateTicketRequest.getUserId());
        doNothing().when(matchValidator).validateMatchExists(updateTicketRequest.getMatchId());
        when(matchService.getMatchById(updateTicketRequest.getMatchId())).thenReturn(Optional.of(match));

        // Act & Assert
        assertDoesNotThrow(() -> ticketValidationService.validateTicketUpdateRequest(updateTicketRequest));

        // Verify
        verify(userValidator).validateUserExists(updateTicketRequest.getUserId());
        verify(matchValidator).validateMatchExists(updateTicketRequest.getMatchId());
        verify(matchService).getMatchById(updateTicketRequest.getMatchId());
    }

    @Test
    void validateTicketUpdateRequest_NullSeatAvailability_ThrowsInvalidSeatException() {
        // Arrange
        SeatEntity seatWithNullAvailability = SeatEntity.builder().id(4L).seatNumber("A4").build();
        availableSeats.put(seatWithNullAvailability, null);

        UpdateTicketRequest requestWithNullAvailability = UpdateTicketRequest.builder()
                .userId(1L)
                .matchId(1L)
                .seatIds(Arrays.asList(4L))
                .build();

        doNothing().when(userValidator).validateUserExists(requestWithNullAvailability.getUserId());
        doNothing().when(matchValidator).validateMatchExists(requestWithNullAvailability.getMatchId());
        when(matchService.getMatchById(requestWithNullAvailability.getMatchId())).thenReturn(Optional.of(match));

        // Act & Assert
        InvalidSeatException exception = assertThrows(InvalidSeatException.class,
                () -> ticketValidationService.validateTicketUpdateRequest(requestWithNullAvailability));
        assertTrue(exception.getMessage().contains("not available"));
    }

    @Test
    void validateTicketUpdateRequest_NonexistentSeat_ThrowsInvalidSeatException() {
        // Arrange
        UpdateTicketRequest requestWithNonexistentSeat = UpdateTicketRequest.builder()
                .userId(1L)
                .matchId(1L)
                .seatIds(Arrays.asList(999L)) // Non-existent seat ID
                .build();

        doNothing().when(userValidator).validateUserExists(requestWithNonexistentSeat.getUserId());
        doNothing().when(matchValidator).validateMatchExists(requestWithNonexistentSeat.getMatchId());
        when(matchService.getMatchById(requestWithNonexistentSeat.getMatchId())).thenReturn(Optional.of(match));

        // Act & Assert
        InvalidSeatException exception = assertThrows(InvalidSeatException.class,
                () -> ticketValidationService.validateTicketUpdateRequest(requestWithNonexistentSeat));
        assertTrue(exception.getMessage().contains("not available"));
    }
}