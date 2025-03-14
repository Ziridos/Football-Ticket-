package nl.fontys.s3.ticketmaster.domain.box;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import nl.fontys.s3.ticketmaster.persitence.entity.BlockEntity;

import java.util.List;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Box {
    private Long boxId;
    private String boxName;
    private int xPosition;
    private int yPosition;
    private int width;
    private int height;
    private Long stadiumId;
    private List<BlockEntity> blocks;
    @Builder.Default
    private Double price = 0.0;
}
