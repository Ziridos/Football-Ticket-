package nl.fontys.s3.ticketmaster.business.interfaces.boxpricingrule;

import nl.fontys.s3.ticketmaster.domain.boxpricingrule.CreateBoxPricingRuleRequest;
import nl.fontys.s3.ticketmaster.domain.boxpricingrule.CreateBoxPricingRuleResponse;

public interface CreateBoxPricingRuleUseCase {
    CreateBoxPricingRuleResponse createBoxPricingRule(CreateBoxPricingRuleRequest request);
}
