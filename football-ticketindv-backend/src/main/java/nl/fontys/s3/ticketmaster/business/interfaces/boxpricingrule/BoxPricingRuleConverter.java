package nl.fontys.s3.ticketmaster.business.interfaces.boxpricingrule;

import nl.fontys.s3.ticketmaster.domain.boxpricingrule.*;
import nl.fontys.s3.ticketmaster.persitence.entity.BoxPricingRuleEntity;

public interface BoxPricingRuleConverter {
    BoxPricingRuleEntity convertToEntity(CreateBoxPricingRuleRequest request);
    CreateBoxPricingRuleResponse convertToCreateBoxPricingRuleResponse(BoxPricingRuleEntity entity);
    GetBoxPricingRuleResponse convertToGetBoxPricingRuleResponse(BoxPricingRuleEntity entity);
    BoxPricingRule convertToBoxPricingRule(BoxPricingRuleEntity entity);
    BoxPricingRuleEntity updateEntityFromRequest(BoxPricingRuleEntity entity, UpdateBoxPricingRuleRequest request);
}