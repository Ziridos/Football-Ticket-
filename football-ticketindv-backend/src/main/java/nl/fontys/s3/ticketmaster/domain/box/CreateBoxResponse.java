package nl.fontys.s3.ticketmaster.domain.box;

import lombok.Builder;
import lombok.Data;
import nl.fontys.s3.ticketmaster.domain.block.GetBlockResponse;

import java.util.List;

@Data
@Builder
public class CreateBoxResponse {
    private Long boxId;
    private String boxName;
    private int xPosition;
    private int yPosition;
    private int width;
    private int height;
    private List<GetBlockResponse> blocks;
}

