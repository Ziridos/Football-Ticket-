package nl.fontys.s3.ticketmaster.domain.boxpricingrule;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class BoxPricingRule {
    private Long id;
    private Long stadiumId;
    private double occupancyThreshold;
    private double priceIncreasePercentage;
}
