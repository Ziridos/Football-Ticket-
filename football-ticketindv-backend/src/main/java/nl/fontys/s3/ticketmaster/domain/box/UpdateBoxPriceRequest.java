package nl.fontys.s3.ticketmaster.domain.box;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class UpdateBoxPriceRequest {
    @NotNull
    private Long boxId;
    @NotNull
    private Double newPrice;
}