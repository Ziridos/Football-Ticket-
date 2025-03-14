package nl.fontys.s3.ticketmaster.business.impl.boxpricingrule;

import lombok.AllArgsConstructor;
import nl.fontys.s3.ticketmaster.business.interfaces.boxpricingrule.CreateBoxPricingRuleUseCase;
import nl.fontys.s3.ticketmaster.business.interfaces.boxpricingrule.BoxPricingRuleConverter;
import nl.fontys.s3.ticketmaster.business.interfaces.boxpricingrule.BoxPricingRuleValidator;
import nl.fontys.s3.ticketmaster.domain.boxpricingrule.CreateBoxPricingRuleRequest;
import nl.fontys.s3.ticketmaster.domain.boxpricingrule.CreateBoxPricingRuleResponse;
import nl.fontys.s3.ticketmaster.persitence.BoxPricingRuleRepository;
import nl.fontys.s3.ticketmaster.persitence.entity.BoxPricingRuleEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@AllArgsConstructor
public class CreateBoxPricingRuleUseCaseImpl implements CreateBoxPricingRuleUseCase {
    private final BoxPricingRuleRepository boxPricingRuleRepository;
    private final BoxPricingRuleConverter boxPricingRuleConverter;
    private final BoxPricingRuleValidator boxPricingRuleValidator;

    @Override
    @Transactional
    public CreateBoxPricingRuleResponse createBoxPricingRule(CreateBoxPricingRuleRequest request) {
        boxPricingRuleValidator.validateCreateBoxPricingRuleRequest(request);

        BoxPricingRuleEntity boxPricingRuleEntity = boxPricingRuleConverter.convertToEntity(request);
        BoxPricingRuleEntity savedEntity = boxPricingRuleRepository.save(boxPricingRuleEntity);

        return boxPricingRuleConverter.convertToCreateBoxPricingRuleResponse(savedEntity);
    }
}