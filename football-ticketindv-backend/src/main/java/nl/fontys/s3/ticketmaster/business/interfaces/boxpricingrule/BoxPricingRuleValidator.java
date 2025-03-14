package nl.fontys.s3.ticketmaster.business.interfaces.boxpricingrule;

import nl.fontys.s3.ticketmaster.domain.boxpricingrule.CreateBoxPricingRuleRequest;
import nl.fontys.s3.ticketmaster.domain.boxpricingrule.UpdateBoxPricingRuleRequest;

public interface BoxPricingRuleValidator {
    void validateCreateBoxPricingRuleRequest(CreateBoxPricingRuleRequest request);
    void validateUpdateBoxPricingRuleRequest(UpdateBoxPricingRuleRequest request, Long ruleId);
    void validateBoxPricingRuleExists(Long ruleId);
}