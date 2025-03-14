package nl.fontys.s3.ticketmaster.business.impl.boximpl;

import nl.fontys.s3.ticketmaster.business.exception.InvalidBoxException;
import nl.fontys.s3.ticketmaster.business.interfaces.boxinterfaces.BoxConverter;
import nl.fontys.s3.ticketmaster.business.interfaces.boxinterfaces.BoxValidator;
import nl.fontys.s3.ticketmaster.domain.box.UpdateBoxPriceRequest;
import nl.fontys.s3.ticketmaster.domain.box.UpdateBoxPriceResponse;
import nl.fontys.s3.ticketmaster.persitence.BoxRepository;
import nl.fontys.s3.ticketmaster.persitence.entity.BoxEntity;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

class UpdateBoxPriceUseCaseImplTest {

    @Mock
    private BoxRepository boxRepository;

    @Mock
    private BoxConverter boxConverter;

    @Mock
    private BoxValidator boxValidator;

    @InjectMocks
    private UpdateBoxPriceUseCaseImpl updateBoxPriceUseCase;

    private UpdateBoxPriceRequest request;
    private BoxEntity boxEntity;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        request = new UpdateBoxPriceRequest();
        request.setBoxId(1L);
        request.setNewPrice(150.0);

        boxEntity = new BoxEntity();
        boxEntity.setId(1L);
        boxEntity.setPrice(150.0);
    }

    @Test
    void updateBoxPrice_success() {
        // Arrange
        doNothing().when(boxValidator).validateUpdateBoxPriceRequest(request); // Correctly using doNothing for void method
        when(boxRepository.updatePriceWithSeats(request.getBoxId(), request.getNewPrice())).thenReturn(boxEntity);
        when(boxConverter.convertToUpdateBoxPriceResponse(boxEntity)).thenReturn(new UpdateBoxPriceResponse(boxEntity.getId(), boxEntity.getPrice()));

        // Act
        UpdateBoxPriceResponse response = updateBoxPriceUseCase.updateBoxPrice(request);

        // Assert
        assertNotNull(response);
        assertEquals(boxEntity.getId(), response.getBoxId());
        assertEquals(boxEntity.getPrice(), response.getUpdatedPrice());
        verify(boxValidator).validateUpdateBoxPriceRequest(request);
        verify(boxRepository).updatePriceWithSeats(request.getBoxId(), request.getNewPrice());
        verify(boxConverter).convertToUpdateBoxPriceResponse(boxEntity);
    }

    @Test
    void updateBoxPrice_invalidRequest() {
        // Arrange
        doThrow(new InvalidBoxException("Invalid box ID")).when(boxValidator).validateUpdateBoxPriceRequest(any());

        // Act & Assert
        InvalidBoxException thrown = assertThrows(InvalidBoxException.class, () -> updateBoxPriceUseCase.updateBoxPrice(request));
        assertEquals("Invalid box ID", thrown.getMessage());

        verify(boxValidator).validateUpdateBoxPriceRequest(request);
        verify(boxRepository, never()).updatePriceWithSeats(any(), any());
        verify(boxConverter, never()).convertToUpdateBoxPriceResponse(any());
    }
}
