package nl.fontys.s3.ticketmaster.business.impl.boxpricingrule;

import nl.fontys.s3.ticketmaster.domain.boxpricingrule.*;
import nl.fontys.s3.ticketmaster.persitence.StadiumRepository;
import nl.fontys.s3.ticketmaster.persitence.entity.BoxPricingRuleEntity;
import nl.fontys.s3.ticketmaster.persitence.entity.StadiumEntity;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class BoxPricingRuleConverterImplTest {

    @Mock
    private StadiumRepository stadiumRepository;

    @InjectMocks
    private BoxPricingRuleConverterImpl converter;

    private StadiumEntity stadium;
    private BoxPricingRuleEntity pricingRule;
    private static final double OCCUPANCY_THRESHOLD = 80.0;
    private static final double PRICE_INCREASE_PERCENTAGE = 20.0;

    @BeforeEach
    void setUp() {
        stadium = StadiumEntity.builder()
                .id(1L)
                .stadiumName("Test Stadium")
                .build();

        pricingRule = BoxPricingRuleEntity.builder()
                .id(1L)
                .stadium(stadium)
                .occupancyThreshold(OCCUPANCY_THRESHOLD)
                .priceIncreasePercentage(PRICE_INCREASE_PERCENTAGE)
                .build();
    }

    @Test
    void convertToEntity_ValidRequest_ReturnsEntity() {
        // Arrange
        CreateBoxPricingRuleRequest request = CreateBoxPricingRuleRequest.builder()
                .stadiumId(1L)
                .occupancyThreshold(OCCUPANCY_THRESHOLD)
                .priceIncreasePercentage(PRICE_INCREASE_PERCENTAGE)
                .build();

        when(stadiumRepository.findById(1L)).thenReturn(Optional.of(stadium));

        // Act
        BoxPricingRuleEntity result = converter.convertToEntity(request);

        // Assert
        assertNotNull(result);
        assertEquals(stadium, result.getStadium());
        assertEquals(OCCUPANCY_THRESHOLD, result.getOccupancyThreshold());
        assertEquals(PRICE_INCREASE_PERCENTAGE, result.getPriceIncreasePercentage());
    }

    @Test
    void convertToEntity_StadiumNotFound_ThrowsException() {
        // Arrange
        CreateBoxPricingRuleRequest request = CreateBoxPricingRuleRequest.builder()
                .stadiumId(999L)
                .occupancyThreshold(OCCUPANCY_THRESHOLD)
                .priceIncreasePercentage(PRICE_INCREASE_PERCENTAGE)
                .build();

        when(stadiumRepository.findById(999L)).thenReturn(Optional.empty());

        // Act & Assert
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class,
                () -> converter.convertToEntity(request));
        assertEquals("Stadium not found for ID: 999", exception.getMessage());
    }

    @Test
    void convertToCreateBoxPricingRuleResponse_ValidEntity_ReturnsResponse() {
        // Act
        CreateBoxPricingRuleResponse response = converter.convertToCreateBoxPricingRuleResponse(pricingRule);

        // Assert
        assertNotNull(response);
        assertEquals(pricingRule.getId(), response.getId());
        assertEquals(stadium.getId(), response.getStadiumId()); // Fixed: Should use stadium's ID
        assertEquals(OCCUPANCY_THRESHOLD, response.getOccupancyThreshold());
        assertEquals(PRICE_INCREASE_PERCENTAGE, response.getPriceIncreasePercentage());
    }

    @Test
    void convertToGetBoxPricingRuleResponse_ValidEntity_ReturnsResponse() {
        // Act
        GetBoxPricingRuleResponse response = converter.convertToGetBoxPricingRuleResponse(pricingRule);

        // Assert
        assertNotNull(response);
        assertEquals(pricingRule.getId(), response.getId());
        assertEquals(stadium.getId(), response.getStadiumId()); // Fixed: Should use stadium's ID
        assertEquals(OCCUPANCY_THRESHOLD, response.getOccupancyThreshold());
        assertEquals(PRICE_INCREASE_PERCENTAGE, response.getPriceIncreasePercentage());
    }

    @Test
    void convertToBoxPricingRule_ValidEntity_ReturnsDomainObject() {
        // Act
        BoxPricingRule result = converter.convertToBoxPricingRule(pricingRule);

        // Assert
        assertNotNull(result);
        assertEquals(pricingRule.getId(), result.getId());
        assertEquals(stadium.getId(), result.getStadiumId()); // Fixed: Should use stadium's ID
        assertEquals(OCCUPANCY_THRESHOLD, result.getOccupancyThreshold());
        assertEquals(PRICE_INCREASE_PERCENTAGE, result.getPriceIncreasePercentage());
    }

    @Test
    void updateEntityFromRequest_ValidRequest_UpdatesEntity() {
        // Arrange
        UpdateBoxPricingRuleRequest request = UpdateBoxPricingRuleRequest.builder()
                .occupancyThreshold(90.0)
                .priceIncreasePercentage(30.0)
                .build();

        BoxPricingRuleEntity existingEntity = BoxPricingRuleEntity.builder()
                .id(1L)
                .stadium(stadium)
                .occupancyThreshold(OCCUPANCY_THRESHOLD)
                .priceIncreasePercentage(PRICE_INCREASE_PERCENTAGE)
                .build();

        // Act
        BoxPricingRuleEntity result = converter.updateEntityFromRequest(existingEntity, request);

        // Assert
        assertNotNull(result);
        assertEquals(1L, result.getId());
        assertEquals(stadium, result.getStadium());
        assertEquals(90.0, result.getOccupancyThreshold());
        assertEquals(30.0, result.getPriceIncreasePercentage());
    }
}