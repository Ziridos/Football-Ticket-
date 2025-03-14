package nl.fontys.s3.ticketmaster.domain.seat;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Seat {
    private Long seatId;
    private String seatNumber;
    private int xPosition;
    private int yPosition;
    private Long blockId;
    @Builder.Default
    private Double price = 0.0;
}
