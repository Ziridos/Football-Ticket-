package nl.fontys.s3.ticketmaster.business.impl.boximpl;

import nl.fontys.s3.ticketmaster.business.exception.InvalidBoxException;
import nl.fontys.s3.ticketmaster.business.interfaces.stadiuminterfaces.StadiumValidator;
import nl.fontys.s3.ticketmaster.domain.box.CreateBoxRequest;
import nl.fontys.s3.ticketmaster.domain.box.UpdateBoxPriceRequest;
import nl.fontys.s3.ticketmaster.persitence.BoxRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import static org.junit.jupiter.api.Assertions.*;

import static org.mockito.Mockito.*;

class BoxValidatorImplTest {

    @Mock
    private BoxRepository boxRepository;

    @Mock
    private StadiumValidator stadiumValidator;

    @InjectMocks
    private BoxValidatorImpl boxValidator;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void validateCreateBoxRequest_validRequest() {
        // Arrange
        CreateBoxRequest request = CreateBoxRequest.builder()
                .boxName("VIP Box")
                .stadiumId(1L)
                .width(10)
                .height(5)
                .build();

        // Act
        // Just ensure no exception is thrown
        assertDoesNotThrow(() -> boxValidator.validateCreateBoxRequest(request));
    }

    @Test
    void validateCreateBoxRequest_emptyBoxName() {
        // Arrange
        CreateBoxRequest request = CreateBoxRequest.builder()
                .boxName("")
                .stadiumId(1L)
                .width(10)
                .height(5)
                .build();

        // Act & Assert
        InvalidBoxException thrown = assertThrows(InvalidBoxException.class,
                () -> boxValidator.validateCreateBoxRequest(request));
        assertEquals("Box name cannot be null or empty.", thrown.getMessage());
    }

    @Test
    void validateCreateBoxRequest_nullStadiumId() {
        // Arrange
        CreateBoxRequest request = CreateBoxRequest.builder()
                .boxName("VIP Box")
                .stadiumId(null)
                .width(10)
                .height(5)
                .build();

        // Act & Assert
        InvalidBoxException thrown = assertThrows(InvalidBoxException.class,
                () -> boxValidator.validateCreateBoxRequest(request));
        assertEquals("Stadium ID cannot be null.", thrown.getMessage());
    }

    @Test
    void validateCreateBoxRequest_invalidStadium() {
        // Arrange
        CreateBoxRequest request = CreateBoxRequest.builder()
                .boxName("VIP Box")
                .stadiumId(1L)
                .width(10)
                .height(5)
                .build();
        doThrow(new InvalidBoxException("Stadium does not exist.")).when(stadiumValidator).validateStadiumExists(1L);

        // Act & Assert
        InvalidBoxException thrown = assertThrows(InvalidBoxException.class,
                () -> boxValidator.validateCreateBoxRequest(request));
        assertEquals("Invalid stadium ID: Stadium does not exist.", thrown.getMessage());
    }

    @Test
    void validateCreateBoxRequest_negativeDimensions() {
        // Arrange
        CreateBoxRequest request = CreateBoxRequest.builder()
                .boxName("VIP Box")
                .stadiumId(1L)
                .width(-10)
                .height(5)
                .build();

        // Act & Assert
        InvalidBoxException thrown = assertThrows(InvalidBoxException.class,
                () -> boxValidator.validateCreateBoxRequest(request));
        assertEquals("Box dimensions must be positive.", thrown.getMessage());
    }

    @Test
    void validateUpdateBoxPriceRequest_validRequest() {
        // Arrange
        UpdateBoxPriceRequest request = UpdateBoxPriceRequest.builder()
                .boxId(1L)
                .newPrice(50.0)
                .build();
        when(boxRepository.existsById(1L)).thenReturn(true);

        // Act & Assert
        assertDoesNotThrow(() -> boxValidator.validateUpdateBoxPriceRequest(request));
    }

    @Test
    void validateUpdateBoxPriceRequest_boxDoesNotExist() {
        // Arrange
        UpdateBoxPriceRequest request = UpdateBoxPriceRequest.builder()
                .boxId(1L)
                .newPrice(50.0)
                .build();
        when(boxRepository.existsById(1L)).thenReturn(false);

        // Act & Assert
        InvalidBoxException thrown = assertThrows(InvalidBoxException.class,
                () -> boxValidator.validateUpdateBoxPriceRequest(request));
        assertEquals("Box with the given ID does not exist.", thrown.getMessage());
    }

    @Test
    void validateUpdateBoxPriceRequest_negativePrice() {
        // Arrange
        UpdateBoxPriceRequest request = UpdateBoxPriceRequest.builder()
                .boxId(1L)
                .newPrice(-50.0)
                .build();
        when(boxRepository.existsById(1L)).thenReturn(true);

        // Act & Assert
        InvalidBoxException thrown = assertThrows(InvalidBoxException.class,
                () -> boxValidator.validateUpdateBoxPriceRequest(request));
        assertEquals("Box price cannot be negative.", thrown.getMessage());
    }

    @Test
    void validateBoxExists_boxDoesNotExist() {
        // Arrange
        Long boxId = 1L;
        when(boxRepository.existsById(boxId)).thenReturn(false);

        // Act & Assert
        InvalidBoxException thrown = assertThrows(InvalidBoxException.class,
                () -> boxValidator.validateBoxExists(boxId));
        assertEquals("Box with the given ID does not exist.", thrown.getMessage());
    }
}
