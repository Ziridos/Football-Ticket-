package nl.fontys.s3.ticketmaster.business.impl.boxpricingrule;

import lombok.AllArgsConstructor;
import nl.fontys.s3.ticketmaster.business.interfaces.boxpricingrule.GetBoxPricingRulesForStadiumUseCase;
import nl.fontys.s3.ticketmaster.business.interfaces.boxpricingrule.BoxPricingRuleConverter;
import nl.fontys.s3.ticketmaster.domain.boxpricingrule.GetBoxPricingRuleResponse;
import nl.fontys.s3.ticketmaster.persitence.BoxPricingRuleRepository;
import nl.fontys.s3.ticketmaster.persitence.entity.BoxPricingRuleEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@AllArgsConstructor
public class GetBoxPricingRulesForStadiumUseCaseImpl implements GetBoxPricingRulesForStadiumUseCase {
    private final BoxPricingRuleRepository boxPricingRuleRepository;
    private final BoxPricingRuleConverter boxPricingRuleConverter;

    @Override
    @Transactional
    public List<GetBoxPricingRuleResponse> getPricingRulesForStadium(Long stadiumId) {
        List<BoxPricingRuleEntity> rules = boxPricingRuleRepository.findAllByStadiumId(stadiumId);
        return rules.stream()
                .map(boxPricingRuleConverter::convertToGetBoxPricingRuleResponse)
                .toList();
    }
}