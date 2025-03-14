package nl.fontys.s3.ticketmaster.business.impl.boxpricingrule;

import lombok.AllArgsConstructor;
import nl.fontys.s3.ticketmaster.business.exception.InvalidBoxPricingRuleException;
import nl.fontys.s3.ticketmaster.business.interfaces.boxpricingrule.BoxPricingRuleConverter;
import nl.fontys.s3.ticketmaster.business.interfaces.boxpricingrule.BoxPricingRuleValidator;
import nl.fontys.s3.ticketmaster.business.interfaces.boxpricingrule.UpdateBoxPricingRuleUseCase;
import nl.fontys.s3.ticketmaster.domain.boxpricingrule.UpdateBoxPricingRuleRequest;
import nl.fontys.s3.ticketmaster.domain.boxpricingrule.UpdateBoxPricingRuleResponse;
import nl.fontys.s3.ticketmaster.persitence.BoxPricingRuleRepository;
import nl.fontys.s3.ticketmaster.persitence.entity.BoxPricingRuleEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


@Service
@AllArgsConstructor
public class UpdateBoxPricingRuleUseCaseImpl implements UpdateBoxPricingRuleUseCase {
    private final BoxPricingRuleRepository boxPricingRuleRepository;
    private final BoxPricingRuleConverter boxPricingRuleConverter;
    private final BoxPricingRuleValidator boxPricingRuleValidator;

    @Override
    @Transactional
    public UpdateBoxPricingRuleResponse updateBoxPricingRule(UpdateBoxPricingRuleRequest request) {
        if (request.getId() == null) {
            throw new InvalidBoxPricingRuleException("Rule ID is required for update");
        }

        boxPricingRuleValidator.validateUpdateBoxPricingRuleRequest(request, request.getId());

        BoxPricingRuleEntity existingEntity = boxPricingRuleRepository.findById(request.getId())
                .orElseThrow(() -> new InvalidBoxPricingRuleException("Box pricing rule not found"));

        BoxPricingRuleEntity updatedEntity = boxPricingRuleConverter.updateEntityFromRequest(existingEntity, request);
        BoxPricingRuleEntity savedEntity = boxPricingRuleRepository.save(updatedEntity);

        return UpdateBoxPricingRuleResponse.builder()
                .id(savedEntity.getId())
                .stadiumId(savedEntity.getStadium().getId())
                .occupancyThreshold(savedEntity.getOccupancyThreshold())
                .priceIncreasePercentage(savedEntity.getPriceIncreasePercentage())
                .build();
    }
}