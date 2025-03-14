package nl.fontys.s3.ticketmaster.domain.boxpricingrule;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UpdateBoxPricingRuleRequest {
    @NotNull
    private Long id;
    @NotNull
    private double occupancyThreshold;
    @NotNull
    private double priceIncreasePercentage;
}
