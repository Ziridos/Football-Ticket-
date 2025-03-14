package nl.fontys.s3.ticketmaster.domain.websocket;

import lombok.Data;

@Data
public class SeatSelectionMessage {
    private Long matchId;
    private Long seatId;
    private String action; // "SELECT" or "DESELECT"
    private Long userId;
    private Long timestamp;
    private boolean expired;
}