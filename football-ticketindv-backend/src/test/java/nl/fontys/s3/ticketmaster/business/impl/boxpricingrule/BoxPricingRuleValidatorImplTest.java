package nl.fontys.s3.ticketmaster.business.impl.boxpricingrule;

import nl.fontys.s3.ticketmaster.business.exception.InvalidBoxPricingRuleException;
import nl.fontys.s3.ticketmaster.business.interfaces.stadiuminterfaces.StadiumService;
import nl.fontys.s3.ticketmaster.domain.boxpricingrule.CreateBoxPricingRuleRequest;
import nl.fontys.s3.ticketmaster.domain.boxpricingrule.UpdateBoxPricingRuleRequest;
import nl.fontys.s3.ticketmaster.persitence.BoxPricingRuleRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class BoxPricingRuleValidatorImplTest {

    @Mock
    private BoxPricingRuleRepository boxPricingRuleRepositoryMock;

    @Mock
    private StadiumService stadiumServiceMock;

    @InjectMocks
    private BoxPricingRuleValidatorImpl validator;

    @Test
    void validateCreateBoxPricingRuleRequest_shouldPassForValidRequest() {
        CreateBoxPricingRuleRequest request = CreateBoxPricingRuleRequest.builder()
                .stadiumId(1L)
                .occupancyThreshold(80.0)
                .priceIncreasePercentage(20.0)
                .build();

        when(stadiumServiceMock.existsById(1L)).thenReturn(true);

        assertDoesNotThrow(() -> validator.validateCreateBoxPricingRuleRequest(request));
    }

    @Test
    void validateCreateBoxPricingRuleRequest_shouldThrowExceptionForNonExistentStadium() {
        CreateBoxPricingRuleRequest request = CreateBoxPricingRuleRequest.builder()
                .stadiumId(1L)
                .occupancyThreshold(80.0)
                .priceIncreasePercentage(20.0)
                .build();

        when(stadiumServiceMock.existsById(1L)).thenReturn(false);

        assertThrows(InvalidBoxPricingRuleException.class, () -> validator.validateCreateBoxPricingRuleRequest(request));
    }

    @Test
    void validateCreateBoxPricingRuleRequest_shouldThrowExceptionForInvalidOccupancyThreshold() {
        CreateBoxPricingRuleRequest request = CreateBoxPricingRuleRequest.builder()
                .stadiumId(1L)
                .occupancyThreshold(101.0)
                .priceIncreasePercentage(20.0)
                .build();

        when(stadiumServiceMock.existsById(1L)).thenReturn(true);

        assertThrows(InvalidBoxPricingRuleException.class, () -> validator.validateCreateBoxPricingRuleRequest(request));
    }

    @Test
    void validateCreateBoxPricingRuleRequest_shouldThrowExceptionForNegativePriceIncrease() {
        CreateBoxPricingRuleRequest request = CreateBoxPricingRuleRequest.builder()
                .stadiumId(1L)
                .occupancyThreshold(80.0)
                .priceIncreasePercentage(-10.0)
                .build();

        when(stadiumServiceMock.existsById(1L)).thenReturn(true);

        assertThrows(InvalidBoxPricingRuleException.class, () -> validator.validateCreateBoxPricingRuleRequest(request));
    }

    @Test
    void validateUpdateBoxPricingRuleRequest_shouldPassForValidRequest() {
        UpdateBoxPricingRuleRequest request = UpdateBoxPricingRuleRequest.builder()
                .occupancyThreshold(80.0)
                .priceIncreasePercentage(20.0)
                .build();

        when(boxPricingRuleRepositoryMock.existsById(1L)).thenReturn(true);

        assertDoesNotThrow(() -> validator.validateUpdateBoxPricingRuleRequest(request, 1L));
    }

    @Test
    void validateUpdateBoxPricingRuleRequest_shouldThrowExceptionForNonExistentRule() {
        UpdateBoxPricingRuleRequest request = UpdateBoxPricingRuleRequest.builder()
                .occupancyThreshold(80.0)
                .priceIncreasePercentage(20.0)
                .build();

        when(boxPricingRuleRepositoryMock.existsById(1L)).thenReturn(false);

        assertThrows(InvalidBoxPricingRuleException.class, () -> validator.validateUpdateBoxPricingRuleRequest(request, 1L));
    }

    @Test
    void validateBoxPricingRuleExists_shouldPassForExistingRule() {
        when(boxPricingRuleRepositoryMock.existsById(1L)).thenReturn(true);

        assertDoesNotThrow(() -> validator.validateBoxPricingRuleExists(1L));
    }

    @Test
    void validateBoxPricingRuleExists_shouldThrowExceptionForNonExistentRule() {
        when(boxPricingRuleRepositoryMock.existsById(1L)).thenReturn(false);

        assertThrows(InvalidBoxPricingRuleException.class, () -> validator.validateBoxPricingRuleExists(1L));
    }
}