package nl.fontys.s3.ticketmaster.business.impl.matchimpl;

import nl.fontys.s3.ticketmaster.business.interfaces.matchinterfaces.MatchService;
import nl.fontys.s3.ticketmaster.business.interfaces.seatinterfaces.SeatConverter;
import nl.fontys.s3.ticketmaster.business.interfaces.seatinterfaces.SeatService;
import nl.fontys.s3.ticketmaster.domain.seat.SeatAvailabilityResponse;
import nl.fontys.s3.ticketmaster.persitence.entity.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class GetSeatAvailabilityUseCaseImplTest {

    @Mock
    private MatchService matchService;

    @Mock
    private SeatService seatService;

    @Mock
    private SeatConverter seatConverter;

    @InjectMocks
    private GetSeatAvailabilityUseCaseImpl getSeatAvailabilityUseCase;

    private MatchEntity matchEntity;
    private StadiumEntity stadium;
    private ClubEntity homeClub;
    private List<SeatEntity> seats;
    private Map<SeatEntity, Boolean> availableSeats;
    private List<SeatAvailabilityResponse> expectedResponses;

    @BeforeEach
    void setUp() {
        // Create stadium
        stadium = StadiumEntity.builder()
                .id(1L)
                .stadiumName("Test Stadium")
                .build();

        // Create home club
        homeClub = ClubEntity.builder()
                .id(1L)
                .clubName("Home Club")
                .stadium(stadium)
                .build();

        // Create seats
        seats = Arrays.asList(
                SeatEntity.builder().id(1L).seatNumber("A1").build(),
                SeatEntity.builder().id(2L).seatNumber("A2").build()
        );

        // Create available seats map
        availableSeats = new HashMap<>();
        availableSeats.put(seats.get(0), true);
        availableSeats.put(seats.get(1), false);

        // Create match entity
        matchEntity = MatchEntity.builder()
                .id(1L)
                .homeClub(homeClub)
                .availableSeats(availableSeats)
                .build();

        // Create expected responses
        expectedResponses = Arrays.asList(
                SeatAvailabilityResponse.builder()
                        .seatId(1L)
                        .seatNumber("A1")
                        .isAvailable(true)
                        .build(),
                SeatAvailabilityResponse.builder()
                        .seatId(2L)
                        .seatNumber("A2")
                        .isAvailable(false)
                        .build()
        );
    }

    @Test
    void getSeatAvailability_ValidMatch_ReturnsAvailabilityList() {
        // Arrange
        when(matchService.getMatchById(1L)).thenReturn(Optional.of(matchEntity));
        when(seatService.getSeatsByStadium(1L)).thenReturn(seats);

        Map<Long, Boolean> expectedAvailabilityMap = new HashMap<>();
        expectedAvailabilityMap.put(1L, true);
        expectedAvailabilityMap.put(2L, false);

        when(seatConverter.convertToSeatAvailabilityResponses(eq(seats), any(Map.class)))
                .thenReturn(expectedResponses);

        // Act
        List<SeatAvailabilityResponse> result = getSeatAvailabilityUseCase.getSeatAvailability(1L);

        // Assert
        assertNotNull(result);
        assertEquals(expectedResponses, result);

        // Verify
        verify(matchService).getMatchById(1L);
        verify(seatService).getSeatsByStadium(1L);
        verify(seatConverter).convertToSeatAvailabilityResponses(eq(seats), any(Map.class));
    }

    @Test
    void getSeatAvailability_MatchNotFound_ThrowsException() {
        // Arrange
        when(matchService.getMatchById(1L)).thenReturn(Optional.empty());

        // Act & Assert
        RuntimeException exception = assertThrows(RuntimeException.class,
                () -> getSeatAvailabilityUseCase.getSeatAvailability(1L));
        assertEquals("Match not found with id: 1", exception.getMessage());

        // Verify that subsequent methods were not called
        verify(seatService, never()).getSeatsByStadium(any());
        verify(seatConverter, never()).convertToSeatAvailabilityResponses(any(), any());
    }

    @Test
    void getSeatAvailability_NoSeatsInStadium_ReturnsEmptyList() {
        // Arrange
        when(matchService.getMatchById(1L)).thenReturn(Optional.of(matchEntity));
        when(seatService.getSeatsByStadium(1L)).thenReturn(Collections.emptyList());
        when(seatConverter.convertToSeatAvailabilityResponses(eq(Collections.emptyList()), any(Map.class)))
                .thenReturn(Collections.emptyList());

        // Act
        List<SeatAvailabilityResponse> result = getSeatAvailabilityUseCase.getSeatAvailability(1L);

        // Assert
        assertNotNull(result);
        assertTrue(result.isEmpty());

        // Verify
        verify(matchService).getMatchById(1L);
        verify(seatService).getSeatsByStadium(1L);
        verify(seatConverter).convertToSeatAvailabilityResponses(eq(Collections.emptyList()), any(Map.class));
    }

    @Test
    void getSeatAvailability_SeatServiceThrowsException_PropagatesException() {
        // Arrange
        when(matchService.getMatchById(1L)).thenReturn(Optional.of(matchEntity));
        when(seatService.getSeatsByStadium(1L))
                .thenThrow(new RuntimeException("Failed to retrieve seats"));

        // Act & Assert
        RuntimeException exception = assertThrows(RuntimeException.class,
                () -> getSeatAvailabilityUseCase.getSeatAvailability(1L));
        assertEquals("Failed to retrieve seats", exception.getMessage());

        // Verify
        verify(matchService).getMatchById(1L);
        verify(seatConverter, never()).convertToSeatAvailabilityResponses(any(), any());
    }

    @Test
    void getSeatAvailability_ConverterThrowsException_PropagatesException() {
        // Arrange
        when(matchService.getMatchById(1L)).thenReturn(Optional.of(matchEntity));
        when(seatService.getSeatsByStadium(1L)).thenReturn(seats);
        when(seatConverter.convertToSeatAvailabilityResponses(any(), any()))
                .thenThrow(new RuntimeException("Conversion failed"));

        // Act & Assert
        RuntimeException exception = assertThrows(RuntimeException.class,
                () -> getSeatAvailabilityUseCase.getSeatAvailability(1L));
        assertEquals("Conversion failed", exception.getMessage());

        // Verify
        verify(matchService).getMatchById(1L);
        verify(seatService).getSeatsByStadium(1L);
    }
}