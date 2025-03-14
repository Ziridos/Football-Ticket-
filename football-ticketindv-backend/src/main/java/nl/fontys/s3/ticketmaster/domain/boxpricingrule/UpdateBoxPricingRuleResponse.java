package nl.fontys.s3.ticketmaster.domain.boxpricingrule;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UpdateBoxPricingRuleResponse {
    private Long id;
    private Long stadiumId;
    private double occupancyThreshold;
    private double priceIncreasePercentage;
}
