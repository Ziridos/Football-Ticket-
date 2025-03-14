package nl.fontys.s3.ticketmaster.domain.seat;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class SeatAvailabilityResponse {
    private Long seatId;
    private String seatNumber;
    private int xPosition;
    private int yPosition;
    private Boolean isAvailable;
    private Long blockId;
    private Double price;
}