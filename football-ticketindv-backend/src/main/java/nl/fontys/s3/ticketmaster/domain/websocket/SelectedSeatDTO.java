package nl.fontys.s3.ticketmaster.domain.websocket;

import lombok.Data;

@Data
public class SelectedSeatDTO {
    private Long matchId;
    private Long seatId;
    private Long userId;
    private Long timestamp;
}