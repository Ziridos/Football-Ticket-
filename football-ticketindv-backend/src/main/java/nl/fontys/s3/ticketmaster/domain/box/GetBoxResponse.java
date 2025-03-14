package nl.fontys.s3.ticketmaster.domain.box;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;


@Data
@Builder
@AllArgsConstructor
public class GetBoxResponse {
    private Long boxId;
    private String boxName;
    private int xPosition;
    private int yPosition;
    private int width;
    private int height;
    private Long stadiumId;
    @Builder.Default
    private Double price = 0.0;
}
