package nl.fontys.s3.ticketmaster.business.impl.matchimpl;

import nl.fontys.s3.ticketmaster.business.interfaces.seatinterfaces.SeatService;
import nl.fontys.s3.ticketmaster.persitence.MatchRepository;
import nl.fontys.s3.ticketmaster.persitence.entity.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class MatchUpdateServiceImplTest {
    @Mock
    private MatchRepository matchRepository;

    @Mock
    private SeatService seatService;

    @InjectMocks
    private MatchUpdateServiceImpl matchUpdateService;

    @Captor
    private ArgumentCaptor<MatchEntity> matchCaptor;

    private MatchEntity match;
    private StadiumEntity stadium;
    private ClubEntity homeClub;
    private List<SeatEntity> stadiumSeats;
    private Map<SeatEntity, Boolean> availableSeats;

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
        SeatEntity seat1 = SeatEntity.builder().id(1L).seatNumber("A1").build();
        SeatEntity seat2 = SeatEntity.builder().id(2L).seatNumber("A2").build();
        SeatEntity seat3 = SeatEntity.builder().id(3L).seatNumber("A3").build();

        stadiumSeats = Arrays.asList(seat1, seat2, seat3);

        // Create available seats map
        availableSeats = new HashMap<>();
        stadiumSeats.forEach(seat -> availableSeats.put(seat, true));

        // Create match
        match = MatchEntity.builder()
                .id(1L)
                .homeClub(homeClub)
                .matchDateTime(LocalDateTime.now())
                .availableSeats(new HashMap<>(availableSeats))
                .build();
    }

    @Test
    void updateMatchSeats_ValidMatchAndSeats_UpdatesAvailability() {
        // Arrange
        List<Long> seatIds = Arrays.asList(1L, 2L);
        when(matchRepository.findById(1L)).thenReturn(Optional.of(match));
        when(seatService.getSeatsByStadium(1L)).thenReturn(stadiumSeats);

        // Act
        matchUpdateService.updateMatchSeats(1L, seatIds);

        // Assert
        verify(matchRepository).findById(1L);
        verify(seatService).getSeatsByStadium(1L);
        verify(matchRepository).save(matchCaptor.capture());

        MatchEntity savedMatch = matchCaptor.getValue();
        Map<SeatEntity, Boolean> updatedSeats = savedMatch.getAvailableSeats();

        // Verify selected seats are marked as unavailable
        stadiumSeats.forEach(seat -> {
            if (seatIds.contains(seat.getId())) {
                assertFalse(updatedSeats.get(seat));
            } else {
                assertTrue(updatedSeats.get(seat));
            }
        });
    }

    @Test
    void updateMatchSeats_MatchNotFound_ThrowsException() {
        // Arrange
        List<Long> seatIds = Arrays.asList(1L, 2L);
        when(matchRepository.findById(1L)).thenReturn(Optional.empty());

        // Act & Assert
        RuntimeException exception = assertThrows(RuntimeException.class,
                () -> matchUpdateService.updateMatchSeats(1L, seatIds));
        assertEquals("Match not found with id: 1", exception.getMessage());

        verify(matchRepository).findById(1L);
        verify(seatService, never()).getSeatsByStadium(any());
        verify(matchRepository, never()).save(any());
    }

    @Test
    void updateMatchSeats_NoSeatsToUpdate_SavesUnchangedMatch() {
        // Arrange
        List<Long> seatIds = Arrays.asList(999L); // Non-existent seat IDs
        when(matchRepository.findById(1L)).thenReturn(Optional.of(match));
        when(seatService.getSeatsByStadium(1L)).thenReturn(stadiumSeats);

        // Act
        matchUpdateService.updateMatchSeats(1L, seatIds);

        // Assert
        verify(matchRepository).save(matchCaptor.capture());
        MatchEntity savedMatch = matchCaptor.getValue();
        savedMatch.getAvailableSeats().values().forEach(available -> assertTrue(available));
    }

    @Test
    void updateMatchSeatsForTicketUpdate_ValidUpdate_UpdatesAvailability() {
        // Arrange
        SeatEntity oldSeat1 = stadiumSeats.get(0);
        SeatEntity oldSeat2 = stadiumSeats.get(1);
        SeatEntity newSeat1 = stadiumSeats.get(2);

        TicketEntity originalTicket = TicketEntity.builder()
                .id(1L)
                .match(match)
                .seats(Arrays.asList(oldSeat1, oldSeat2))
                .build();

        TicketEntity updatedTicket = TicketEntity.builder()
                .id(1L)
                .match(match)
                .seats(Collections.singletonList(newSeat1))
                .build();

        // Act
        matchUpdateService.updateMatchSeatsForTicketUpdate(originalTicket, updatedTicket);

        // Assert
        verify(matchRepository).save(matchCaptor.capture());
        MatchEntity savedMatch = matchCaptor.getValue();
        Map<SeatEntity, Boolean> updatedSeats = savedMatch.getAvailableSeats();

        // Original seats should be available again
        assertTrue(updatedSeats.get(oldSeat1));
        assertTrue(updatedSeats.get(oldSeat2));
        // New seat should be unavailable
        assertFalse(updatedSeats.get(newSeat1));
    }

    @Test
    void updateMatchSeatsForTicketUpdate_SameSeats_NoChange() {
        // Arrange
        List<SeatEntity> seats = Arrays.asList(stadiumSeats.get(0), stadiumSeats.get(1));

        TicketEntity originalTicket = TicketEntity.builder()
                .id(1L)
                .match(match)
                .seats(seats)
                .build();

        TicketEntity updatedTicket = TicketEntity.builder()
                .id(1L)
                .match(match)
                .seats(new ArrayList<>(seats))
                .build();

        // Act
        matchUpdateService.updateMatchSeatsForTicketUpdate(originalTicket, updatedTicket);

        // Assert
        verify(matchRepository).save(matchCaptor.capture());
        MatchEntity savedMatch = matchCaptor.getValue();
        Map<SeatEntity, Boolean> updatedSeats = savedMatch.getAvailableSeats();

        // Seats should be marked as unavailable
        seats.forEach(seat -> assertFalse(updatedSeats.get(seat)));
    }
}