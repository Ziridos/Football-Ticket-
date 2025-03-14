package nl.fontys.s3.ticketmaster.business.impl.blockimpl;

import nl.fontys.s3.ticketmaster.business.exception.InvalidBlockException;
import nl.fontys.s3.ticketmaster.business.interfaces.boxinterfaces.BoxValidator;
import nl.fontys.s3.ticketmaster.domain.block.CreateBlockRequest;
import nl.fontys.s3.ticketmaster.persitence.BlockRepository;
import nl.fontys.s3.ticketmaster.persitence.entity.BoxEntity;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.*;

class BlockValidatorImplTest {

    @Mock
    private BlockRepository blockRepository;

    @Mock
    private BoxValidator boxValidator;

    @InjectMocks
    private BlockValidatorImpl blockValidator;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void validateCreateBlockRequest_validRequest() {
        // Arrange
        Long boxId = 1L;
        CreateBlockRequest request = CreateBlockRequest.builder()
                .blockName("Block A")
                .boxId(boxId)
                .width(5)
                .height(5)
                .build();

        // Simulate that the box exists
        doNothing().when(boxValidator).validateBoxExists(boxId);
        when(blockRepository.existsById(anyLong())).thenReturn(false);

        // Act & Assert (No exception should be thrown)
        assertDoesNotThrow(() -> blockValidator.validateCreateBlockRequest(request));
    }

    @Test
    void validateCreateBlockRequest_emptyBlockName() {
        // Arrange
        CreateBlockRequest request = CreateBlockRequest.builder()
                .blockName("")
                .boxId(1L)
                .width(5)
                .height(5)
                .build();

        // Act & Assert
        InvalidBlockException exception = assertThrows(InvalidBlockException.class,
                () -> blockValidator.validateCreateBlockRequest(request));
        assertEquals("Block name cannot be null or empty.", exception.getMessage());
    }

    @Test
    void validateCreateBlockRequest_nullBoxId() {
        // Arrange
        CreateBlockRequest request = CreateBlockRequest.builder()
                .blockName("Block A")
                .boxId(null)
                .width(5)
                .height(5)
                .build();

        // Act & Assert
        InvalidBlockException exception = assertThrows(InvalidBlockException.class,
                () -> blockValidator.validateCreateBlockRequest(request));
        assertEquals("Box ID cannot be null.", exception.getMessage());
    }

    @Test
    void validateCreateBlockRequest_invalidBox() {
        // Arrange
        CreateBlockRequest request = CreateBlockRequest.builder()
                .blockName("Block A")
                .boxId(1L)
                .width(5)
                .height(5)
                .build();

        // Simulate an exception being thrown when validating the box
        doThrow(new InvalidBlockException("Box does not exist")).when(boxValidator).validateBoxExists(1L);

        // Act & Assert
        InvalidBlockException exception = assertThrows(InvalidBlockException.class,
                () -> blockValidator.validateCreateBlockRequest(request));
        assertEquals("Invalid box ID: Box does not exist", exception.getMessage());
    }

    @Test
    void validateCreateBlockRequest_negativeDimensions() {
        // Arrange
        CreateBlockRequest request = CreateBlockRequest.builder()
                .blockName("Block A")
                .boxId(1L)
                .width(-5)
                .height(5)
                .build();

        // Act & Assert
        InvalidBlockException exception = assertThrows(InvalidBlockException.class,
                () -> blockValidator.validateCreateBlockRequest(request));
        assertEquals("Block dimensions must be positive.", exception.getMessage());
    }

    @Test
    void validateBlockExists_blockDoesNotExist() {
        // Arrange
        Long blockId = 1L;
        when(blockRepository.existsById(blockId)).thenReturn(false);

        // Act & Assert
        InvalidBlockException exception = assertThrows(InvalidBlockException.class,
                () -> blockValidator.validateBlockExists(blockId));
        assertEquals("Block with the given ID does not exist.", exception.getMessage());
    }

    @Test
    void validateUpdatedBox_boxNotFound() {
        // Arrange
        Long boxId = 1L;
        BoxEntity updatedBox = null;

        // Act & Assert
        InvalidBlockException exception = assertThrows(InvalidBlockException.class,
                () -> blockValidator.validateUpdatedBox(updatedBox, boxId));
        assertEquals("Failed to add block to box. Box not found with id: " + boxId, exception.getMessage());
    }
}
