package nl.fontys.s3.ticketmaster.controller;

import lombok.RequiredArgsConstructor;
import nl.fontys.s3.ticketmaster.business.interfaces.websockets.SelectedSeatsService;
import nl.fontys.s3.ticketmaster.domain.websocket.SeatSelectionMessage;
import org.springframework.http.ResponseEntity;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.stereotype.Controller;

import java.util.Map;

@Controller
@RequiredArgsConstructor
public class WebSocketController {
    private final SelectedSeatsService selectedSeatsService;

    @MessageMapping("/seat-selection")
    @SendTo("/topic/seat-updates")
    public SeatSelectionMessage handleSeatSelection(SeatSelectionMessage message) {
        if ("SELECT".equals(message.getAction())) {
            selectedSeatsService.addSelectedSeat(
                    message.getMatchId(),
                    message.getSeatId(),
                    message.getUserId()
            );
        } else if ("DESELECT".equals(message.getAction())) {
            selectedSeatsService.removeSelectedSeat(
                    message.getMatchId(),
                    message.getSeatId()
            );
        }
        message.setTimestamp(System.currentTimeMillis());
        return message;
    }

    @GetMapping("/matches/{matchId}/selected-seats")
    public ResponseEntity<Map<Long, Long>> getSelectedSeats(@PathVariable Long matchId) {
        return ResponseEntity.ok(selectedSeatsService.getSelectedSeatsForMatch(matchId));
    }
}