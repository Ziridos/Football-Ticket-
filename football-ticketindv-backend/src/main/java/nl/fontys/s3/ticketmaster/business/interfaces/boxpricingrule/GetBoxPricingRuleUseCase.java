package nl.fontys.s3.ticketmaster.business.interfaces.boxpricingrule;

import nl.fontys.s3.ticketmaster.domain.boxpricingrule.GetBoxPricingRuleResponse;

public interface GetBoxPricingRuleUseCase {
    GetBoxPricingRuleResponse getPricingRule(Long ruleId);
}
