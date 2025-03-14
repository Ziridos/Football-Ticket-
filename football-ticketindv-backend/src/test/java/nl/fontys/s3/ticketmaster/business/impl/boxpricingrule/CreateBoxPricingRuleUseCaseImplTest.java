package nl.fontys.s3.ticketmaster.business.impl.boxpricingrule;

import nl.fontys.s3.ticketmaster.business.interfaces.boxpricingrule.BoxPricingRuleConverter;
import nl.fontys.s3.ticketmaster.business.interfaces.boxpricingrule.BoxPricingRuleValidator;
import nl.fontys.s3.ticketmaster.domain.boxpricingrule.CreateBoxPricingRuleRequest;
import nl.fontys.s3.ticketmaster.domain.boxpricingrule.CreateBoxPricingRuleResponse;
import nl.fontys.s3.ticketmaster.persitence.BoxPricingRuleRepository;
import nl.fontys.s3.ticketmaster.persitence.entity.BoxPricingRuleEntity;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class CreateBoxPricingRuleUseCaseImplTest {

    @Mock
    private BoxPricingRuleRepository boxPricingRuleRepositoryMock;

    @Mock
    private BoxPricingRuleConverter boxPricingRuleConverterMock;

    @Mock
    private BoxPricingRuleValidator boxPricingRuleValidatorMock;

    @InjectMocks
    private CreateBoxPricingRuleUseCaseImpl createBoxPricingRuleUseCase;

    @Test
    void createBoxPricingRule_shouldCreateAndReturnResponseForValidRequest() {
        // Arrange
        CreateBoxPricingRuleRequest request = CreateBoxPricingRuleRequest.builder()
                .stadiumId(1L)
                .occupancyThreshold(75.0)
                .priceIncreasePercentage(15.0)
                .build();
        BoxPricingRuleEntity entity = new BoxPricingRuleEntity();
        BoxPricingRuleEntity savedEntity = new BoxPricingRuleEntity();
        CreateBoxPricingRuleResponse expectedResponse = new CreateBoxPricingRuleResponse(
                1L, // id
                request.getStadiumId(),
                request.getOccupancyThreshold(),
                request.getPriceIncreasePercentage()
        );

        when(boxPricingRuleConverterMock.convertToEntity(request)).thenReturn(entity);
        when(boxPricingRuleRepositoryMock.save(entity)).thenReturn(savedEntity);
        when(boxPricingRuleConverterMock.convertToCreateBoxPricingRuleResponse(savedEntity)).thenReturn(expectedResponse);

        // Act
        CreateBoxPricingRuleResponse response = createBoxPricingRuleUseCase.createBoxPricingRule(request);

        // Assert
        assertEquals(expectedResponse, response);
        verify(boxPricingRuleValidatorMock).validateCreateBoxPricingRuleRequest(request);
        verify(boxPricingRuleConverterMock).convertToEntity(request);
        verify(boxPricingRuleRepositoryMock).save(entity);
        verify(boxPricingRuleConverterMock).convertToCreateBoxPricingRuleResponse(savedEntity);
    }


    @Test
    void createBoxPricingRule_shouldThrowExceptionForInvalidRequest() {
        // Arrange
        CreateBoxPricingRuleRequest invalidRequest = CreateBoxPricingRuleRequest.builder()
                .stadiumId(1L)
                .occupancyThreshold(150.0)  // Invalid occupancy threshold
                .priceIncreasePercentage(-5.0)  // Invalid price increase percentage
                .build();

        doThrow(new IllegalArgumentException("Invalid request"))
                .when(boxPricingRuleValidatorMock).validateCreateBoxPricingRuleRequest(invalidRequest);

        // Act & Assert
        assertThrows(IllegalArgumentException.class, () ->
                createBoxPricingRuleUseCase.createBoxPricingRule(invalidRequest)
        );
        verify(boxPricingRuleValidatorMock).validateCreateBoxPricingRuleRequest(invalidRequest);
        verify(boxPricingRuleConverterMock, never()).convertToEntity(any());
        verify(boxPricingRuleRepositoryMock, never()).save(any());
    }

    @Test
    void createBoxPricingRule_shouldReturnResponseForValidConversionAndSaving() {
        // Arrange
        CreateBoxPricingRuleRequest request = CreateBoxPricingRuleRequest.builder()
                .stadiumId(1L)
                .occupancyThreshold(80.0)
                .priceIncreasePercentage(20.0)
                .build();
        BoxPricingRuleEntity entity = new BoxPricingRuleEntity();
        BoxPricingRuleEntity savedEntity = new BoxPricingRuleEntity();
        CreateBoxPricingRuleResponse expectedResponse = new CreateBoxPricingRuleResponse(
                1L, // id
                request.getStadiumId(),
                request.getOccupancyThreshold(),
                request.getPriceIncreasePercentage()
        );

        when(boxPricingRuleConverterMock.convertToEntity(request)).thenReturn(entity);
        when(boxPricingRuleRepositoryMock.save(entity)).thenReturn(savedEntity);
        when(boxPricingRuleConverterMock.convertToCreateBoxPricingRuleResponse(savedEntity)).thenReturn(expectedResponse);

        // Act
        CreateBoxPricingRuleResponse response = createBoxPricingRuleUseCase.createBoxPricingRule(request);

        // Assert
        assertEquals(expectedResponse, response);
        verify(boxPricingRuleValidatorMock).validateCreateBoxPricingRuleRequest(request);
        verify(boxPricingRuleConverterMock).convertToEntity(request);
        verify(boxPricingRuleRepositoryMock).save(entity);
        verify(boxPricingRuleConverterMock).convertToCreateBoxPricingRuleResponse(savedEntity);
    }

}
