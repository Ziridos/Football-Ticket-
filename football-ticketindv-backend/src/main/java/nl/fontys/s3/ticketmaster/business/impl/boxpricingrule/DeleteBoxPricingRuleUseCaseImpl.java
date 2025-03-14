package nl.fontys.s3.ticketmaster.business.impl.boxpricingrule;

import lombok.AllArgsConstructor;
import nl.fontys.s3.ticketmaster.business.interfaces.boxpricingrule.DeleteBoxPricingRuleUseCase;
import nl.fontys.s3.ticketmaster.business.interfaces.boxpricingrule.BoxPricingRuleValidator;
import nl.fontys.s3.ticketmaster.persitence.BoxPricingRuleRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@AllArgsConstructor
public class DeleteBoxPricingRuleUseCaseImpl implements DeleteBoxPricingRuleUseCase {
    private final BoxPricingRuleRepository boxPricingRuleRepository;
    private final BoxPricingRuleValidator boxPricingRuleValidator;

    @Override
    @Transactional
    public void deleteBoxPricingRule(Long id) {
        boxPricingRuleValidator.validateBoxPricingRuleExists(id);
        boxPricingRuleRepository.deleteById(id);
    }
}