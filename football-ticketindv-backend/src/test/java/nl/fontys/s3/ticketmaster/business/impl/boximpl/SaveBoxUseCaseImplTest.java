package nl.fontys.s3.ticketmaster.business.impl.boximpl;

import nl.fontys.s3.ticketmaster.business.interfaces.boxinterfaces.BoxConverter;
import nl.fontys.s3.ticketmaster.business.interfaces.boxinterfaces.BoxValidator;
import nl.fontys.s3.ticketmaster.domain.box.CreateBoxRequest;
import nl.fontys.s3.ticketmaster.domain.box.CreateBoxResponse;
import nl.fontys.s3.ticketmaster.persitence.BoxRepository;
import nl.fontys.s3.ticketmaster.persitence.entity.BoxEntity;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class SaveBoxUseCaseImplTest {

    @Mock
    private BoxRepository boxRepository;

    @Mock
    private BoxConverter boxConverter;

    @Mock
    private BoxValidator boxValidator;

    private SaveBoxUseCaseImpl saveBoxUseCase;

    @BeforeEach
    void setUp() {
        saveBoxUseCase = new SaveBoxUseCaseImpl(boxRepository, boxConverter, boxValidator);
    }

    @Test
    void saveBox_shouldSaveAndReturnBoxResponse() {
        // Arrange
        CreateBoxRequest request = CreateBoxRequest.builder().boxName("Test Box").build();
        BoxEntity boxEntity = BoxEntity.builder().id(1L).boxName("Test Box").build();
        CreateBoxResponse expectedResponse = CreateBoxResponse.builder().boxId(1L).boxName("Test Box").build();

        doNothing().when(boxValidator).validateCreateBoxRequest(request);
        when(boxConverter.convertToEntity(request)).thenReturn(boxEntity);
        when(boxRepository.save(boxEntity)).thenReturn(boxEntity);
        when(boxConverter.convertToCreateBoxResponse(boxEntity)).thenReturn(expectedResponse);

        // Act
        CreateBoxResponse result = saveBoxUseCase.saveBox(request);

        // Assert
        assertEquals(expectedResponse, result);
        verify(boxValidator).validateCreateBoxRequest(request);
        verify(boxConverter).convertToEntity(request);
        verify(boxRepository).save(boxEntity);
        verify(boxConverter).convertToCreateBoxResponse(boxEntity);
    }
}