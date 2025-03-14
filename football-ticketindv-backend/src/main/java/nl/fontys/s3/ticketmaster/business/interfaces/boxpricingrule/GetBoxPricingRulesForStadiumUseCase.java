package nl.fontys.s3.ticketmaster.business.interfaces.boxpricingrule;

import nl.fontys.s3.ticketmaster.domain.boxpricingrule.GetBoxPricingRuleResponse;
import java.util.List;

public interface GetBoxPricingRulesForStadiumUseCase {
    List<GetBoxPricingRuleResponse> getPricingRulesForStadium(Long stadiumId);
}