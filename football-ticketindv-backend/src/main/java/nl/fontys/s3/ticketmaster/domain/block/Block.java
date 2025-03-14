package nl.fontys.s3.ticketmaster.domain.block;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import nl.fontys.s3.ticketmaster.persitence.entity.SeatEntity;

import java.util.List;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Block {
    private Long blockId;
    private String blockName;
    private int xPosition;
    private int yPosition;
    private int width;
    private int height;
    private Long boxId;
    private List<SeatEntity> seats;
}
