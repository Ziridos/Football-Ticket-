package nl.fontys.s3.ticketmaster.business.impl.websocketsimpl;


import nl.fontys.s3.ticketmaster.domain.websocket.SeatSelectionMessage;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.messaging.simp.SimpMessagingTemplate;

import java.util.Map;
import java.util.Optional;


import static org.junit.jupiter.api.Assertions.*;



@ExtendWith(MockitoExtension.class)
class SelectedSeatsServiceImplTest {
    @Mock
    private SimpMessagingTemplate messagingTemplate;

    @InjectMocks
    private SelectedSeatsServiceImpl selectedSeatsService;

    @Captor
    private ArgumentCaptor<SeatSelectionMessage> messageCaptor;

    private static final Long TEST_MATCH_ID = 1L;
    private static final Long TEST_SEAT_ID = 2L;
    private static final Long TEST_USER_ID = 3L;



    @BeforeEach
    void setUp() {
        selectedSeatsService = new SelectedSeatsServiceImpl(messagingTemplate);
    }

    @Test
    void addSelectedSeat_ShouldAddSeatSuccessfully() {
        // Act
        selectedSeatsService.addSelectedSeat(TEST_MATCH_ID, TEST_SEAT_ID, TEST_USER_ID);

        // Assert
        Map<Long, Long> selectedSeats = selectedSeatsService.getSelectedSeatsForMatch(TEST_MATCH_ID);
        assertNotNull(selectedSeats);
        assertEquals(TEST_USER_ID, selectedSeats.get(TEST_SEAT_ID));
    }

    @Test
    void removeSelectedSeat_ShouldRemoveSeatSuccessfully() {
        // Arrange
        selectedSeatsService.addSelectedSeat(TEST_MATCH_ID, TEST_SEAT_ID, TEST_USER_ID);

        // Act
        selectedSeatsService.removeSelectedSeat(TEST_MATCH_ID, TEST_SEAT_ID);

        // Assert
        Map<Long, Long> selectedSeats = selectedSeatsService.getSelectedSeatsForMatch(TEST_MATCH_ID);
        assertTrue(selectedSeats.isEmpty());
    }

    @Test
    void getSelectedSeatsForMatch_WithNoSeats_ReturnsEmptyMap() {
        // Act
        Map<Long, Long> selectedSeats = selectedSeatsService.getSelectedSeatsForMatch(TEST_MATCH_ID);

        // Assert
        assertNotNull(selectedSeats);
        assertTrue(selectedSeats.isEmpty());
    }

    @Test
    void getSelectedSeatsForMatch_WithValidSeats_ReturnsCorrectMap() {
        // Arrange
        selectedSeatsService.addSelectedSeat(TEST_MATCH_ID, TEST_SEAT_ID, TEST_USER_ID);

        // Act
        Map<Long, Long> selectedSeats = selectedSeatsService.getSelectedSeatsForMatch(TEST_MATCH_ID);

        // Assert
        assertNotNull(selectedSeats);
        assertEquals(1, selectedSeats.size());
        assertEquals(TEST_USER_ID, selectedSeats.get(TEST_SEAT_ID));
    }

    @Test
    void getSelectionTimeRemaining_ForValidSelection_ReturnsTimeRemaining() {
        // Arrange
        selectedSeatsService.addSelectedSeat(TEST_MATCH_ID, TEST_SEAT_ID, TEST_USER_ID);

        // Act
        Optional<Long> timeRemaining = selectedSeatsService.getSelectionTimeRemaining(TEST_MATCH_ID, TEST_SEAT_ID);

        // Assert
        assertTrue(timeRemaining.isPresent());
        assertTrue(timeRemaining.get() > 0 && timeRemaining.get() <= 900000); // 15 minutes in milliseconds
    }

    @Test
    void getSelectionTimeRemaining_ForNonExistentSelection_ReturnsEmpty() {
        // Act
        Optional<Long> timeRemaining = selectedSeatsService.getSelectionTimeRemaining(TEST_MATCH_ID, TEST_SEAT_ID);

        // Assert
        assertTrue(timeRemaining.isEmpty());
    }



    @Test
    void multipleSeatsForMatch_ShouldHandleCorrectly() {
        // Arrange
        Long secondSeatId = 4L;
        Long secondUserId = 5L;

        // Act
        selectedSeatsService.addSelectedSeat(TEST_MATCH_ID, TEST_SEAT_ID, TEST_USER_ID);
        selectedSeatsService.addSelectedSeat(TEST_MATCH_ID, secondSeatId, secondUserId);

        // Assert
        Map<Long, Long> selectedSeats = selectedSeatsService.getSelectedSeatsForMatch(TEST_MATCH_ID);
        assertEquals(2, selectedSeats.size());
        assertEquals(TEST_USER_ID, selectedSeats.get(TEST_SEAT_ID));
        assertEquals(secondUserId, selectedSeats.get(secondSeatId));
    }
}