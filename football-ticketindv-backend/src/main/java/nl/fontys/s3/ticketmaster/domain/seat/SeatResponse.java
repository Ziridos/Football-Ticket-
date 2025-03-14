package nl.fontys.s3.ticketmaster.domain.seat;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class SeatResponse {
    private Long seatId;
    private String seatNumber;
    private int xPosition;
    private int yPosition;
    private Double price;
}
