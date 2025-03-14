package nl.fontys.s3.ticketmaster.domain.seat;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class CreateSeatRequest {
    @NotBlank
    private String seatNumber;
    @NotNull
    private int xPosition;
    @NotNull
    private int yPosition;
}
