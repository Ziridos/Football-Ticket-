package nl.fontys.s3.ticketmaster.domain.seat;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import nl.fontys.s3.ticketmaster.domain.block.BlockDTO;
import nl.fontys.s3.ticketmaster.domain.box.BoxDTO;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class SeatDTO {
    private Long seatId;
    private String seatNumber;
    private BlockDTO block;
    private BoxDTO box;
    private Double price = 0.0;
}
