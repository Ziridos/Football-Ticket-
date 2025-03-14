package nl.fontys.s3.ticketmaster.business.impl.boxpricingrule;

import lombok.AllArgsConstructor;
import nl.fontys.s3.ticketmaster.business.interfaces.boxpricingrule.BoxPricingRuleConverter;
import nl.fontys.s3.ticketmaster.domain.boxpricingrule.*;
import nl.fontys.s3.ticketmaster.persitence.StadiumRepository;
import nl.fontys.s3.ticketmaster.persitence.entity.BoxPricingRuleEntity;
import nl.fontys.s3.ticketmaster.persitence.entity.StadiumEntity;
import org.springframework.stereotype.Component;

@Component
@AllArgsConstructor
public class BoxPricingRuleConverterImpl implements BoxPricingRuleConverter {
    private final StadiumRepository stadiumRepository;

    @Override
    public BoxPricingRuleEntity convertToEntity(CreateBoxPricingRuleRequest request) {

        StadiumEntity stadium = stadiumRepository.findById(request.getStadiumId())
                .orElseThrow(() -> new IllegalArgumentException("Stadium not found for ID: " + request.getStadiumId()));

        return BoxPricingRuleEntity.builder()
                .stadium(stadium)
                .occupancyThreshold(request.getOccupancyThreshold())
                .priceIncreasePercentage(request.getPriceIncreasePercentage())
                .build();
    }

    @Override
    public CreateBoxPricingRuleResponse convertToCreateBoxPricingRuleResponse(BoxPricingRuleEntity entity) {
        return CreateBoxPricingRuleResponse.builder()
                .id(entity.getId())
                .stadiumId(entity.getId())
                .occupancyThreshold(entity.getOccupancyThreshold())
                .priceIncreasePercentage(entity.getPriceIncreasePercentage())
                .build();
    }

    @Override
    public GetBoxPricingRuleResponse convertToGetBoxPricingRuleResponse(BoxPricingRuleEntity entity) {
        return GetBoxPricingRuleResponse.builder()
                .id(entity.getId())
                .stadiumId(entity.getId())
                .occupancyThreshold(entity.getOccupancyThreshold())
                .priceIncreasePercentage(entity.getPriceIncreasePercentage())
                .build();
    }

    @Override
    public BoxPricingRule convertToBoxPricingRule(BoxPricingRuleEntity entity) {
        return BoxPricingRule.builder()
                .id(entity.getId())
                .stadiumId(entity.getId())
                .occupancyThreshold(entity.getOccupancyThreshold())
                .priceIncreasePercentage(entity.getPriceIncreasePercentage())
                .build();
    }

    @Override
    public BoxPricingRuleEntity updateEntityFromRequest(BoxPricingRuleEntity entity, UpdateBoxPricingRuleRequest request) {
        entity.setOccupancyThreshold(request.getOccupancyThreshold());
        entity.setPriceIncreasePercentage(request.getPriceIncreasePercentage());
        return entity;
    }
}