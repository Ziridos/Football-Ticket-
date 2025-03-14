package nl.fontys.s3.ticketmaster.business.impl.boxpricingrule;

import lombok.AllArgsConstructor;
import nl.fontys.s3.ticketmaster.business.exception.InvalidBoxPricingRuleException;
import nl.fontys.s3.ticketmaster.business.interfaces.boxpricingrule.BoxPricingRuleValidator;
import nl.fontys.s3.ticketmaster.business.interfaces.stadiuminterfaces.StadiumService;
import nl.fontys.s3.ticketmaster.domain.boxpricingrule.CreateBoxPricingRuleRequest;
import nl.fontys.s3.ticketmaster.domain.boxpricingrule.UpdateBoxPricingRuleRequest;
import nl.fontys.s3.ticketmaster.persitence.BoxPricingRuleRepository;
import org.springframework.stereotype.Component;

@Component
@AllArgsConstructor
public class BoxPricingRuleValidatorImpl implements BoxPricingRuleValidator {
    private final BoxPricingRuleRepository boxPricingRuleRepository;
    private final StadiumService stadiumService;

    @Override
    public void validateCreateBoxPricingRuleRequest(CreateBoxPricingRuleRequest request) {
        if (!stadiumService.existsById(request.getStadiumId())) {
            throw new InvalidBoxPricingRuleException("Stadium with the given ID does not exist.");
        }
        validateOccupancyThreshold(request.getOccupancyThreshold());
        validatePriceIncreasePercentage(request.getPriceIncreasePercentage());
    }

    @Override
    public void validateUpdateBoxPricingRuleRequest(UpdateBoxPricingRuleRequest request, Long ruleId) {
        validateBoxPricingRuleExists(ruleId);
        validateOccupancyThreshold(request.getOccupancyThreshold());
        validatePriceIncreasePercentage(request.getPriceIncreasePercentage());
    }

    @Override
    public void validateBoxPricingRuleExists(Long ruleId) {
        if (!boxPricingRuleRepository.existsById(ruleId)) {
            throw new InvalidBoxPricingRuleException("Box pricing rule with the given ID does not exist.");
        }
    }

    private void validateOccupancyThreshold(double occupancyThreshold) {
        if (occupancyThreshold < 0 || occupancyThreshold > 100) {
            throw new InvalidBoxPricingRuleException("Occupancy threshold must be between 0 and 100.");
        }
    }

    private void validatePriceIncreasePercentage(double priceIncreasePercentage) {
        if (priceIncreasePercentage < 0) {
            throw new InvalidBoxPricingRuleException("Price increase percentage must be non-negative.");
        }
    }
}