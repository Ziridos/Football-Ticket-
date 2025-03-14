package nl.fontys.s3.ticketmaster.business.impl.boxpricingrule;

import lombok.AllArgsConstructor;
import nl.fontys.s3.ticketmaster.business.exception.InvalidBoxPricingRuleException;
import nl.fontys.s3.ticketmaster.business.interfaces.boxpricingrule.BoxPricingRuleConverter;
import nl.fontys.s3.ticketmaster.business.interfaces.boxpricingrule.BoxPricingRuleValidator;
import nl.fontys.s3.ticketmaster.business.interfaces.boxpricingrule.GetBoxPricingRuleUseCase;
import nl.fontys.s3.ticketmaster.domain.boxpricingrule.GetBoxPricingRuleResponse;
import nl.fontys.s3.ticketmaster.persitence.BoxPricingRuleRepository;
import nl.fontys.s3.ticketmaster.persitence.entity.BoxPricingRuleEntity;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class GetBoxPricingRuleUseCaseImpl implements GetBoxPricingRuleUseCase {
    private final BoxPricingRuleRepository boxPricingRuleRepository;
    private final BoxPricingRuleConverter boxPricingRuleConverter;
    private final BoxPricingRuleValidator boxPricingRuleValidator;

    @Override
    public GetBoxPricingRuleResponse getPricingRule(Long ruleId) {
        boxPricingRuleValidator.validateBoxPricingRuleExists(ruleId);

        BoxPricingRuleEntity entity = boxPricingRuleRepository.findById(ruleId)
                .orElseThrow(() -> new InvalidBoxPricingRuleException("Box pricing rule not found"));

        return boxPricingRuleConverter.convertToGetBoxPricingRuleResponse(entity);
    }
}