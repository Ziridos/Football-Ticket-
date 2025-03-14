package nl.fontys.s3.ticketmaster.controller;

import nl.fontys.s3.ticketmaster.business.interfaces.websockets.SelectedSeatsService;
import nl.fontys.s3.ticketmaster.domain.websocket.SeatSelectionMessage;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.ResponseEntity;

import java.util.HashMap;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class WebSocketControllerTest {
    @Mock
    private SelectedSeatsService selectedSeatsService;

    @InjectMocks
    private WebSocketController webSocketController;

    private SeatSelectionMessage testMessage;
    private static final Long TEST_MATCH_ID = 1L;
    private static final Long TEST_SEAT_ID = 2L;
    private static final Long TEST_USER_ID = 3L;

    @BeforeEach
    void setUp() {
        testMessage = new SeatSelectionMessage();
        testMessage.setMatchId(TEST_MATCH_ID);
        testMessage.setSeatId(TEST_SEAT_ID);
        testMessage.setUserId(TEST_USER_ID);
    }

    @Test
    void handleSeatSelection_Select_ProcessesCorrectly() {
        // Arrange
        testMessage.setAction("SELECT");
        long beforeTimestamp = System.currentTimeMillis();

        // Act
        SeatSelectionMessage response = webSocketController.handleSeatSelection(testMessage);

        // Assert
        verify(selectedSeatsService).addSelectedSeat(TEST_MATCH_ID, TEST_SEAT_ID, TEST_USER_ID);
        assertNotNull(response);
        assertEquals(TEST_MATCH_ID, response.getMatchId());
        assertEquals(TEST_SEAT_ID, response.getSeatId());
        assertEquals(TEST_USER_ID, response.getUserId());
        assertEquals("SELECT", response.getAction());
        assertTrue(response.getTimestamp() >= beforeTimestamp);
    }

    @Test
    void handleSeatSelection_Deselect_ProcessesCorrectly() {
        // Arrange
        testMessage.setAction("DESELECT");
        long beforeTimestamp = System.currentTimeMillis();

        // Act
        SeatSelectionMessage response = webSocketController.handleSeatSelection(testMessage);

        // Assert
        verify(selectedSeatsService).removeSelectedSeat(TEST_MATCH_ID, TEST_SEAT_ID);
        assertNotNull(response);
        assertEquals(TEST_MATCH_ID, response.getMatchId());
        assertEquals(TEST_SEAT_ID, response.getSeatId());
        assertEquals(TEST_USER_ID, response.getUserId());
        assertEquals("DESELECT", response.getAction());
        assertTrue(response.getTimestamp() >= beforeTimestamp);
    }

    @Test
    void handleSeatSelection_InvalidAction_DoesNotCallService() {
        // Arrange
        testMessage.setAction("INVALID_ACTION");

        // Act
        SeatSelectionMessage response = webSocketController.handleSeatSelection(testMessage);

        // Assert
        verifyNoInteractions(selectedSeatsService);
        assertNotNull(response);
        assertTrue(response.getTimestamp() > 0);
    }

    @Test
    void getSelectedSeats_ReturnsCorrectMap() {
        // Arrange
        Map<Long, Long> expectedSeats = new HashMap<>();
        expectedSeats.put(TEST_SEAT_ID, TEST_USER_ID);
        when(selectedSeatsService.getSelectedSeatsForMatch(TEST_MATCH_ID)).thenReturn(expectedSeats);

        // Act
        ResponseEntity<Map<Long, Long>> response = webSocketController.getSelectedSeats(TEST_MATCH_ID);

        // Assert
        assertTrue(response.getStatusCode().is2xxSuccessful());
        assertNotNull(response.getBody());
        assertEquals(expectedSeats, response.getBody());
        verify(selectedSeatsService).getSelectedSeatsForMatch(TEST_MATCH_ID);
    }

    @Test
    void getSelectedSeats_EmptyMap_ReturnsEmptyResponse() {
        // Arrange
        Map<Long, Long> emptyMap = new HashMap<>();
        when(selectedSeatsService.getSelectedSeatsForMatch(TEST_MATCH_ID)).thenReturn(emptyMap);

        // Act
        ResponseEntity<Map<Long, Long>> response = webSocketController.getSelectedSeats(TEST_MATCH_ID);

        // Assert
        assertTrue(response.getStatusCode().is2xxSuccessful());
        assertNotNull(response.getBody());
        assertTrue(response.getBody().isEmpty());
        verify(selectedSeatsService).getSelectedSeatsForMatch(TEST_MATCH_ID);
    }
}