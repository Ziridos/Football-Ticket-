package nl.fontys.s3.ticketmaster.controller;

import nl.fontys.s3.ticketmaster.business.interfaces.boxinterfaces.*;
import nl.fontys.s3.ticketmaster.domain.box.*;
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
class BoxControllerTest {

    @Mock
    private GetBoxesByStadiumUseCase getBoxesByStadiumUseCase;
    @Mock
    private SaveBoxUseCase saveBoxUseCase;
    @Mock
    private UpdateBoxPriceUseCase updateBoxPriceUseCase;
    @Mock
    private GetMatchSpecificBoxesUseCase getMatchSpecificBoxesUseCase;

    @Mock
    private DeleteBoxUseCase deleteBoxUseCase;

    @InjectMocks
    private BoxController boxController;

    private CreateBoxRequest createBoxRequest;
    private CreateBoxResponse createBoxResponse;
    private UpdateBoxPriceRequest updateBoxPriceRequest;
    private UpdateBoxPriceResponse updateBoxPriceResponse;
    private GetBoxResponse boxResponse1;
    private GetBoxResponse boxResponse2;

    @BeforeEach
    void setUp() {
        createBoxRequest = CreateBoxRequest.builder()
                .boxName("Test Box")
                .xPosition(10)
                .yPosition(20)
                .width(100)
                .height(200)
                .build();

        createBoxResponse = CreateBoxResponse.builder()
                .boxId(1L)
                .boxName("Test Box")
                .xPosition(10)
                .yPosition(20)
                .width(100)
                .height(200)
                .build();

        updateBoxPriceRequest = UpdateBoxPriceRequest.builder()
                .boxId(1L)
                .newPrice(1200.0)
                .build();

        updateBoxPriceResponse = UpdateBoxPriceResponse.builder()
                .boxId(1L)
                .updatedPrice(1200.0)
                .build();

        boxResponse1 = GetBoxResponse.builder()
                .boxId(1L)
                .boxName("Box 1")
                .xPosition(10)
                .yPosition(20)
                .width(100)
                .height(200)
                .price(1000.0)
                .build();

        boxResponse2 = GetBoxResponse.builder()
                .boxId(2L)
                .boxName("Box 2")
                .xPosition(120)
                .yPosition(20)
                .width(100)
                .height(200)
                .price(1000.0)
                .build();
    }

    @Test
    void getBoxesByStadium_ExistingStadium_ReturnsBoxes() {
        // Arrange
        List<GetBoxResponse> expectedBoxes = Arrays.asList(boxResponse1, boxResponse2);
        when(getBoxesByStadiumUseCase.getBoxesByStadium(1L)).thenReturn(expectedBoxes);

        // Act
        ResponseEntity<List<GetBoxResponse>> response = boxController.getBoxesByStadium(1L);

        // Assert
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals(2, response.getBody().size());
        assertEquals(expectedBoxes, response.getBody());
        verify(getBoxesByStadiumUseCase).getBoxesByStadium(1L);
    }

    @Test
    void getBoxesByStadium_NoBoxesFound_ReturnsEmptyList() {
        // Arrange
        when(getBoxesByStadiumUseCase.getBoxesByStadium(1L)).thenReturn(List.of());

        // Act
        ResponseEntity<List<GetBoxResponse>> response = boxController.getBoxesByStadium(1L);

        // Assert
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertTrue(response.getBody().isEmpty());
        verify(getBoxesByStadiumUseCase).getBoxesByStadium(1L);
    }

    @Test
    void getBoxesByStadiumForMatch_ExistingMatch_ReturnsBoxes() {
        // Arrange
        List<GetBoxResponse> expectedBoxes = Arrays.asList(boxResponse1, boxResponse2);
        when(getMatchSpecificBoxesUseCase.getBoxesByStadiumForMatch(1L, 1L))
                .thenReturn(expectedBoxes);

        // Act
        ResponseEntity<List<GetBoxResponse>> response =
                boxController.getBoxesByStadiumForMatch(1L, 1L);

        // Assert
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals(2, response.getBody().size());
        assertEquals(expectedBoxes, response.getBody());
        verify(getMatchSpecificBoxesUseCase).getBoxesByStadiumForMatch(1L, 1L);
    }

    @Test
    void getBoxesByStadiumForMatch_NoBoxesFound_ReturnsEmptyList() {
        // Arrange
        when(getMatchSpecificBoxesUseCase.getBoxesByStadiumForMatch(1L, 1L))
                .thenReturn(List.of());

        // Act
        ResponseEntity<List<GetBoxResponse>> response =
                boxController.getBoxesByStadiumForMatch(1L, 1L);

        // Assert
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertTrue(response.getBody().isEmpty());
        verify(getMatchSpecificBoxesUseCase).getBoxesByStadiumForMatch(1L, 1L);
    }

    @Test
    void deleteBox_ValidId_ReturnsNoContent() {
        ResponseEntity<Void> response = boxController.deleteBox(1L);
        assertEquals(HttpStatus.NO_CONTENT, response.getStatusCode());
        verify(deleteBoxUseCase).deleteBox(1L);
    }

    @Test
    void deleteBox_WhenError_PropagatesException() {
        doThrow(new IllegalArgumentException("Box not found"))
                .when(deleteBoxUseCase).deleteBox(999L);

        Exception exception = assertThrows(IllegalArgumentException.class,
                () -> boxController.deleteBox(999L));
        assertEquals("Box not found", exception.getMessage());
        verify(deleteBoxUseCase).deleteBox(999L);
    }

    @Test
    void createBox_ValidRequest_ReturnsCreatedBox() {
        // Arrange
        when(saveBoxUseCase.saveBox(createBoxRequest)).thenReturn(createBoxResponse);

        // Act
        ResponseEntity<CreateBoxResponse> response =
                boxController.createBox(1L, createBoxRequest);

        // Assert
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals(createBoxResponse, response.getBody());
        assertEquals(1L, createBoxRequest.getStadiumId());
        verify(saveBoxUseCase).saveBox(createBoxRequest);
    }

    @Test
    void updateBoxPrice_ValidRequest_ReturnsUpdatedPrice() {
        // Arrange
        when(updateBoxPriceUseCase.updateBoxPrice(updateBoxPriceRequest))
                .thenReturn(updateBoxPriceResponse);

        // Act
        ResponseEntity<UpdateBoxPriceResponse> response =
                boxController.updateBoxPrice(1L, 1L, updateBoxPriceRequest);

        // Assert
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals(updateBoxPriceResponse, response.getBody());
        assertEquals(1200.0, response.getBody().getUpdatedPrice());
        verify(updateBoxPriceUseCase).updateBoxPrice(updateBoxPriceRequest);
    }

    @Test
    void updateBoxPrice_MismatchedBoxId_ReturnsBadRequest() {
        // Act
        ResponseEntity<UpdateBoxPriceResponse> response =
                boxController.updateBoxPrice(1L, 2L, updateBoxPriceRequest);

        // Assert
        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertNull(response.getBody());
        verify(updateBoxPriceUseCase, never()).updateBoxPrice(any());
    }

    @Test
    void updateBoxPrice_BoxNotFound_ReturnsNotFound() {
        // Arrange
        when(updateBoxPriceUseCase.updateBoxPrice(updateBoxPriceRequest))
                .thenReturn(null);

        // Act
        ResponseEntity<UpdateBoxPriceResponse> response =
                boxController.updateBoxPrice(1L, 1L, updateBoxPriceRequest);

        // Assert
        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        assertNull(response.getBody());
        verify(updateBoxPriceUseCase).updateBoxPrice(updateBoxPriceRequest);
    }
}