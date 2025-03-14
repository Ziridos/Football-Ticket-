package nl.fontys.s3.ticketmaster.controller;

import nl.fontys.s3.ticketmaster.business.interfaces.boxpricingrule.*;
import nl.fontys.s3.ticketmaster.domain.boxpricingrule.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class BoxPricingRuleControllerTest {

    @Mock
    private CreateBoxPricingRuleUseCase createBoxPricingRuleUseCase;
    @Mock
    private GetBoxPricingRulesForStadiumUseCase getBoxPricingRulesForStadiumUseCase;
    @Mock
    private DeleteBoxPricingRuleUseCase deleteBoxPricingRuleUseCase;

    @InjectMocks
    private BoxPricingRuleController boxPricingRuleController;

    private CreateBoxPricingRuleRequest createRequest;
    private CreateBoxPricingRuleResponse createResponse;
    private GetBoxPricingRuleResponse pricingRuleResponse;

    @BeforeEach
    void setUp() {
        createRequest = CreateBoxPricingRuleRequest.builder()
                .stadiumId(1L)
                .occupancyThreshold(80.0)
                .priceIncreasePercentage(20.0)
                .build();

        createResponse = CreateBoxPricingRuleResponse.builder()
                .id(1L)
                .stadiumId(1L)
                .occupancyThreshold(80.0)
                .priceIncreasePercentage(20.0)
                .build();

        pricingRuleResponse = GetBoxPricingRuleResponse.builder()
                .id(1L)
                .stadiumId(1L)
                .occupancyThreshold(80.0)
                .priceIncreasePercentage(20.0)
                .build();
    }

    @Test
    void createBoxPricingRule_ValidRequest_ReturnsCreated() {
        // Arrange
        when(createBoxPricingRuleUseCase.createBoxPricingRule(createRequest))
                .thenReturn(createResponse);

        // Act
        ResponseEntity<CreateBoxPricingRuleResponse> response =
                boxPricingRuleController.createBoxPricingRule(createRequest);

        // Assert
        assertEquals(HttpStatus.CREATED, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals(createResponse, response.getBody());
        assertEquals(1L, response.getBody().getId());
        assertEquals(80.0, response.getBody().getOccupancyThreshold());
        assertEquals(20.0, response.getBody().getPriceIncreasePercentage());
        verify(createBoxPricingRuleUseCase).createBoxPricingRule(createRequest);
    }

    @Test
    void createBoxPricingRule_InvalidRequest_UseCaseThrowsException() {
        // Arrange
        when(createBoxPricingRuleUseCase.createBoxPricingRule(createRequest))
                .thenThrow(new IllegalArgumentException("Invalid pricing rule data"));

        // Act & Assert
        Exception exception = assertThrows(IllegalArgumentException.class,
                () -> boxPricingRuleController.createBoxPricingRule(createRequest));
        assertEquals("Invalid pricing rule data", exception.getMessage());
        verify(createBoxPricingRuleUseCase).createBoxPricingRule(createRequest);
    }

    @Test
    void getPricingRulesForStadium_ExistingStadium_ReturnsPricingRules() {
        // Arrange
        List<GetBoxPricingRuleResponse> expectedRules = Arrays.asList(
                pricingRuleResponse,
                GetBoxPricingRuleResponse.builder()
                        .id(2L)
                        .stadiumId(1L)
                        .occupancyThreshold(90.0)
                        .priceIncreasePercentage(30.0)
                        .build()
        );

        when(getBoxPricingRulesForStadiumUseCase.getPricingRulesForStadium(1L))
                .thenReturn(expectedRules);

        // Act
        ResponseEntity<List<GetBoxPricingRuleResponse>> response =
                boxPricingRuleController.getPricingRulesForStadium(1L);

        // Assert
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals(2, response.getBody().size());
        assertEquals(expectedRules, response.getBody());
        verify(getBoxPricingRulesForStadiumUseCase).getPricingRulesForStadium(1L);
    }

    @Test
    void getPricingRulesForStadium_NoRulesFound_ReturnsEmptyList() {
        // Arrange
        when(getBoxPricingRulesForStadiumUseCase.getPricingRulesForStadium(1L))
                .thenReturn(List.of());

        // Act
        ResponseEntity<List<GetBoxPricingRuleResponse>> response =
                boxPricingRuleController.getPricingRulesForStadium(1L);

        // Assert
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertTrue(response.getBody().isEmpty());
        verify(getBoxPricingRulesForStadiumUseCase).getPricingRulesForStadium(1L);
    }

    @Test
    void getPricingRulesForStadium_InvalidStadium_UseCaseThrowsException() {
        // Arrange
        when(getBoxPricingRulesForStadiumUseCase.getPricingRulesForStadium(999L))
                .thenThrow(new IllegalArgumentException("Stadium not found"));

        // Act & Assert
        Exception exception = assertThrows(IllegalArgumentException.class,
                () -> boxPricingRuleController.getPricingRulesForStadium(999L));
        assertEquals("Stadium not found", exception.getMessage());
        verify(getBoxPricingRulesForStadiumUseCase).getPricingRulesForStadium(999L);
    }

    @Test
    void deleteBoxPricingRule_ExistingRule_ReturnsNoContent() {
        // Arrange
        doNothing().when(deleteBoxPricingRuleUseCase).deleteBoxPricingRule(1L);

        // Act
        ResponseEntity<Void> response = boxPricingRuleController.deleteBoxPricingRule(1L);

        // Assert
        assertEquals(HttpStatus.NO_CONTENT, response.getStatusCode());
        assertNull(response.getBody());
        verify(deleteBoxPricingRuleUseCase).deleteBoxPricingRule(1L);
    }

    @Test
    void deleteBoxPricingRule_NonExistingRule_UseCaseThrowsException() {
        // Arrange
        doThrow(new IllegalArgumentException("Pricing rule not found"))
                .when(deleteBoxPricingRuleUseCase).deleteBoxPricingRule(999L);

        // Act & Assert
        Exception exception = assertThrows(IllegalArgumentException.class,
                () -> boxPricingRuleController.deleteBoxPricingRule(999L));
        assertEquals("Pricing rule not found", exception.getMessage());
        verify(deleteBoxPricingRuleUseCase).deleteBoxPricingRule(999L);
    }
}