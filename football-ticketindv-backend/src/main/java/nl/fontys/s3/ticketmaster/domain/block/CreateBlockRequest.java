package nl.fontys.s3.ticketmaster.domain.block;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Builder;
import lombok.Data;
import nl.fontys.s3.ticketmaster.domain.seat.CreateSeatRequest;

import java.util.List;

@Data
@Builder
public class CreateBlockRequest {
    @NotBlank
    private String blockName;

    private Long boxId;
    @NotNull
    private int xPosition;
    @NotNull
    private int yPosition;
    @NotNull
    private int width;
    @NotNull
    private int height;
    private List<CreateSeatRequest> seats;
}
