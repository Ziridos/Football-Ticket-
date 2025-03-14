package nl.fontys.s3.ticketmaster.business.interfaces.boxpricingrule;

import nl.fontys.s3.ticketmaster.domain.boxpricingrule.UpdateBoxPricingRuleRequest;
import nl.fontys.s3.ticketmaster.domain.boxpricingrule.UpdateBoxPricingRuleResponse;

public interface UpdateBoxPricingRuleUseCase {
    UpdateBoxPricingRuleResponse updateBoxPricingRule(UpdateBoxPricingRuleRequest request);
}
