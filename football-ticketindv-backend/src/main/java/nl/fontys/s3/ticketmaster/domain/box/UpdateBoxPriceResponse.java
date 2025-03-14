package nl.fontys.s3.ticketmaster.domain.box;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class UpdateBoxPriceResponse {
    private Long boxId;
    private Double updatedPrice;
}