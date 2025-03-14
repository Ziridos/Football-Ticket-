package nl.fontys.s3.ticketmaster.business.impl.boxpricingrule;

import nl.fontys.s3.ticketmaster.business.interfaces.boxpricingrule.BoxPricingRuleValidator;
import nl.fontys.s3.ticketmaster.persitence.BoxPricingRuleRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class DeleteBoxPricingRuleUseCaseImplTest {

    @Mock
    private BoxPricingRuleRepository boxPricingRuleRepositoryMock;

    @Mock
    private BoxPricingRuleValidator boxPricingRuleValidatorMock;

    @InjectMocks
    private DeleteBoxPricingRuleUseCaseImpl deleteBoxPricingRuleUseCase;

    @Test
    void deleteBoxPricingRule_shouldDeleteBoxPricingRuleSuccessfully() {
        // Arrange
        Long ruleId = 1L;

        // Act
        deleteBoxPricingRuleUseCase.deleteBoxPricingRule(ruleId);

        // Assert
        verify(boxPricingRuleValidatorMock).validateBoxPricingRuleExists(ruleId);
        verify(boxPricingRuleRepositoryMock).deleteById(ruleId);
    }
}