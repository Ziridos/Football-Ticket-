package nl.fontys.s3.ticketmaster.business.impl.boxpricingrule;

import nl.fontys.s3.ticketmaster.business.interfaces.boxpricingrule.BoxPricingRuleConverter;
import nl.fontys.s3.ticketmaster.domain.boxpricingrule.GetBoxPricingRuleResponse;
import nl.fontys.s3.ticketmaster.persitence.BoxPricingRuleRepository;
import nl.fontys.s3.ticketmaster.persitence.entity.BoxPricingRuleEntity;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class GetBoxPricingRulesForStadiumUseCaseImplTest {

    @Mock
    private BoxPricingRuleRepository boxPricingRuleRepositoryMock;

    @Mock
    private BoxPricingRuleConverter boxPricingRuleConverterMock;

    @InjectMocks
    private GetBoxPricingRulesForStadiumUseCaseImpl getBoxPricingRulesForStadiumUseCase;

    @Test
    void getPricingRulesForStadium_shouldReturnConvertedResponses() {
        // Arrange
        Long stadiumId = 1L;

        BoxPricingRuleEntity ruleEntity1 = new BoxPricingRuleEntity();
        ruleEntity1.setId(1L);  // Ensure this entity has a unique ID for clarity
        BoxPricingRuleEntity ruleEntity2 = new BoxPricingRuleEntity();
        ruleEntity2.setId(2L);  // Ensure this entity has a unique ID

        List<BoxPricingRuleEntity> ruleEntities = List.of(ruleEntity1, ruleEntity2);

        GetBoxPricingRuleResponse response1 = new GetBoxPricingRuleResponse(1L, stadiumId, 80.0, 15.0);
        GetBoxPricingRuleResponse response2 = new GetBoxPricingRuleResponse(2L, stadiumId, 90.0, 20.0);
        List<GetBoxPricingRuleResponse> expectedResponses = List.of(response1, response2);

        when(boxPricingRuleRepositoryMock.findAllByStadiumId(stadiumId)).thenReturn(ruleEntities);
        when(boxPricingRuleConverterMock.convertToGetBoxPricingRuleResponse(ruleEntity1)).thenReturn(response1);
        when(boxPricingRuleConverterMock.convertToGetBoxPricingRuleResponse(ruleEntity2)).thenReturn(response2);

        // Act
        List<GetBoxPricingRuleResponse> actualResponses = getBoxPricingRulesForStadiumUseCase.getPricingRulesForStadium(stadiumId);

        // Assert
        assertEquals(expectedResponses, actualResponses);
        verify(boxPricingRuleRepositoryMock).findAllByStadiumId(stadiumId);
        verify(boxPricingRuleConverterMock).convertToGetBoxPricingRuleResponse(ruleEntity1);
        verify(boxPricingRuleConverterMock).convertToGetBoxPricingRuleResponse(ruleEntity2);
    }

}
