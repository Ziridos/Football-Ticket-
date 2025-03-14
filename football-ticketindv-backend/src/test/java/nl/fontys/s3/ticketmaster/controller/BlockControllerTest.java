package nl.fontys.s3.ticketmaster.controller;

import nl.fontys.s3.ticketmaster.business.interfaces.blockinterfaces.*;
import nl.fontys.s3.ticketmaster.domain.block.*;
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
class BlockControllerTest {

    @Mock
    private SaveBlockUseCase saveBlockUseCase;
    @Mock
    private GetBlocksByStadiumUseCase getBlocksByStadiumUseCase;
    @Mock
    private GetMatchSpecificBlocksUseCase getMatchSpecificBlocksUseCase;

    @Mock
    private DeleteBlockUseCase deleteBlockUseCase;

    @InjectMocks
    private BlockController blockController;

    private CreateBlockRequest createBlockRequest;
    private CreateBlockResponse createBlockResponse;
    private GetBlockResponse blockResponse1;
    private GetBlockResponse blockResponse2;

    @BeforeEach
    void setUp() {
        createBlockRequest = CreateBlockRequest.builder()
                .blockName("Test Block")
                .xPosition(10)
                .yPosition(20)
                .width(100)
                .height(200)
                .build();

        createBlockResponse = CreateBlockResponse.builder()
                .blockId(1L)
                .blockName("Test Block")
                .xPosition(10)
                .yPosition(20)
                .width(100)
                .height(200)
                .build();

        blockResponse1 = GetBlockResponse.builder()
                .blockId(1L)
                .blockName("Block 1")
                .xPosition(10)
                .yPosition(20)
                .width(100)
                .height(200)
                .build();

        blockResponse2 = GetBlockResponse.builder()
                .blockId(2L)
                .blockName("Block 2")
                .xPosition(120)
                .yPosition(20)
                .width(100)
                .height(200)
                .build();
    }

    @Test
    void createBlock_ValidRequest_ReturnsCreatedBlock() {
        // Arrange
        when(saveBlockUseCase.saveBlock(createBlockRequest))
                .thenReturn(createBlockResponse);

        // Act
        ResponseEntity<CreateBlockResponse> response =
                blockController.createBlock(1L, createBlockRequest);

        // Assert
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals(createBlockResponse, response.getBody());
        assertEquals(1L, createBlockRequest.getBoxId());
        verify(saveBlockUseCase).saveBlock(createBlockRequest);
    }

    @Test
    void deleteBlock_ValidId_ReturnsNoContent() {
        // Arrange
        long blockId = 1L;

        // Act
        ResponseEntity<Void> response = blockController.deleteBlock(blockId);

        // Assert
        assertEquals(HttpStatus.NO_CONTENT, response.getStatusCode());
        verify(deleteBlockUseCase).deleteBlock(blockId);
    }

    @Test
    void deleteBlock_WhenError_PropagatesException() {
        // Arrange
        doThrow(new IllegalArgumentException("Block not found"))
                .when(deleteBlockUseCase).deleteBlock(999L);

        // Act & Assert
        Exception exception = assertThrows(IllegalArgumentException.class,
                () -> blockController.deleteBlock(999L));
        assertEquals("Block not found", exception.getMessage());
        verify(deleteBlockUseCase).deleteBlock(999L);
    }

    @Test
    void createBlock_SaveFails_ThrowsException() {
        // Arrange
        when(saveBlockUseCase.saveBlock(createBlockRequest))
                .thenThrow(new IllegalArgumentException("Invalid block data"));

        // Act & Assert
        Exception exception = assertThrows(IllegalArgumentException.class,
                () -> blockController.createBlock(1L, createBlockRequest));
        assertEquals("Invalid block data", exception.getMessage());
        verify(saveBlockUseCase).saveBlock(createBlockRequest);
    }

    @Test
    void getBlocksByBox_ExistingBox_ReturnsBlocks() {
        // Arrange
        List<GetBlockResponse> expectedBlocks = Arrays.asList(blockResponse1, blockResponse2);
        when(getBlocksByStadiumUseCase.getBlocksByStadium(1L))
                .thenReturn(expectedBlocks);

        // Act
        ResponseEntity<List<GetBlockResponse>> response =
                blockController.getBlocksByBox(1L);

        // Assert
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals(2, response.getBody().size());
        assertEquals(expectedBlocks, response.getBody());
        verify(getBlocksByStadiumUseCase).getBlocksByStadium(1L);
    }

    @Test
    void getBlocksByBox_NoBlocksFound_ReturnsEmptyList() {
        // Arrange
        when(getBlocksByStadiumUseCase.getBlocksByStadium(1L))
                .thenReturn(List.of());

        // Act
        ResponseEntity<List<GetBlockResponse>> response =
                blockController.getBlocksByBox(1L);

        // Assert
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertTrue(response.getBody().isEmpty());
        verify(getBlocksByStadiumUseCase).getBlocksByStadium(1L);
    }

    @Test
    void getBlocksByBox_InvalidBox_ThrowsException() {
        // Arrange
        when(getBlocksByStadiumUseCase.getBlocksByStadium(999L))
                .thenThrow(new IllegalArgumentException("Box not found"));

        // Act & Assert
        Exception exception = assertThrows(IllegalArgumentException.class,
                () -> blockController.getBlocksByBox(999L));
        assertEquals("Box not found", exception.getMessage());
        verify(getBlocksByStadiumUseCase).getBlocksByStadium(999L);
    }

    @Test
    void getBlocksByBoxForMatch_ExistingMatch_ReturnsBlocks() {
        // Arrange
        List<GetBlockResponse> expectedBlocks = Arrays.asList(blockResponse1, blockResponse2);
        when(getMatchSpecificBlocksUseCase.getBlocksByStadiumForMatch(1L, 1L))
                .thenReturn(expectedBlocks);

        // Act
        ResponseEntity<List<GetBlockResponse>> response =
                blockController.getBlocksByBoxForMatch(1L, 1L);

        // Assert
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals(2, response.getBody().size());
        assertEquals(expectedBlocks, response.getBody());
        verify(getMatchSpecificBlocksUseCase).getBlocksByStadiumForMatch(1L, 1L);
    }

    @Test
    void getBlocksByBoxForMatch_NoBlocksFound_ReturnsEmptyList() {
        // Arrange
        when(getMatchSpecificBlocksUseCase.getBlocksByStadiumForMatch(1L, 1L))
                .thenReturn(List.of());

        // Act
        ResponseEntity<List<GetBlockResponse>> response =
                blockController.getBlocksByBoxForMatch(1L, 1L);

        // Assert
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertTrue(response.getBody().isEmpty());
        verify(getMatchSpecificBlocksUseCase).getBlocksByStadiumForMatch(1L, 1L);
    }

    @Test
    void getBlocksByBoxForMatch_InvalidMatch_ThrowsException() {
        // Arrange
        when(getMatchSpecificBlocksUseCase.getBlocksByStadiumForMatch(1L, 999L))
                .thenThrow(new IllegalArgumentException("Match not found"));

        // Act & Assert
        Exception exception = assertThrows(IllegalArgumentException.class,
                () -> blockController.getBlocksByBoxForMatch(1L, 999L));
        assertEquals("Match not found", exception.getMessage());
        verify(getMatchSpecificBlocksUseCase).getBlocksByStadiumForMatch(1L, 999L);
    }
}