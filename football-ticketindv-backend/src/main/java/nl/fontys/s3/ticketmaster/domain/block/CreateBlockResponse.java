package nl.fontys.s3.ticketmaster.domain.block;

import lombok.Builder;
import lombok.Data;
import nl.fontys.s3.ticketmaster.domain.seat.SeatResponse;
import java.util.List;

@Data
@Builder
public class CreateBlockResponse {
    private Long blockId;
    private String blockName;
    private int xPosition;
    private int yPosition;
    private int width;
    private int height;
    private List<SeatResponse> seats;
}
