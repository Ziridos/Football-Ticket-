package nl.fontys.s3.ticketmaster.business.impl.stadiumimpl;

import nl.fontys.s3.ticketmaster.business.interfaces.stadiuminterfaces.StadiumValidator;
import nl.fontys.s3.ticketmaster.business.interfaces.stadiuminterfaces.StadiumConverter;
import nl.fontys.s3.ticketmaster.domain.stadium.CreateStadiumRequest;
import nl.fontys.s3.ticketmaster.domain.stadium.CreateStadiumResponse;
import nl.fontys.s3.ticketmaster.persitence.StadiumRepository;
import nl.fontys.s3.ticketmaster.persitence.entity.StadiumEntity;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class CreateStadiumUseCaseImplTest {

    @Mock
    private StadiumRepository stadiumRepositoryMock;

    @Mock
    private StadiumValidator stadiumValidatorMock;

    @Mock
    private StadiumConverter stadiumConverterMock;

    @InjectMocks
    private CreateStadiumUseCaseImpl createStadiumUseCase;

    @Test
    void createStadium_shouldCreateAndSaveStadiumSuccessfully() {
        // Arrange
        CreateStadiumRequest request = CreateStadiumRequest.builder()
                .stadiumName("New Stadium")
                .build();

        StadiumEntity newStadiumEntity = StadiumEntity.builder()
                .stadiumName("New Stadium")
                .build();

        StadiumEntity savedStadiumEntity = StadiumEntity.builder()
                .id(1L)
                .stadiumName("New Stadium")
                .build();

        CreateStadiumResponse expectedResponse = CreateStadiumResponse.builder()
                .stadiumId(1L)
                .stadiumName("New Stadium")
                .build();

        when(stadiumConverterMock.convertToEntity(request)).thenReturn(newStadiumEntity);
        when(stadiumRepositoryMock.save(newStadiumEntity)).thenReturn(savedStadiumEntity);
        when(stadiumConverterMock.convertToCreateResponse(savedStadiumEntity)).thenReturn(expectedResponse);

        // Act
        CreateStadiumResponse result = createStadiumUseCase.createStadium(request);

        // Assert
        assertNotNull(result);
        assertEquals(expectedResponse.getStadiumId(), result.getStadiumId());
        assertEquals(expectedResponse.getStadiumName(), result.getStadiumName());

        verify(stadiumValidatorMock).validateCreateStadiumRequest(request);
        verify(stadiumConverterMock).convertToEntity(request);
        verify(stadiumRepositoryMock).save(newStadiumEntity);
        verify(stadiumConverterMock).convertToCreateResponse(savedStadiumEntity);
    }

    @Test
    void createStadium_shouldThrowExceptionWhenValidationFails() {
        // Arrange
        CreateStadiumRequest request = CreateStadiumRequest.builder()
                .stadiumName("")  // Invalid name
                .build();

        doThrow(new IllegalArgumentException("Invalid stadium data"))
                .when(stadiumValidatorMock).validateCreateStadiumRequest(request);

        // Act & Assert
        assertThrows(IllegalArgumentException.class, () -> createStadiumUseCase.createStadium(request));

        verify(stadiumValidatorMock).validateCreateStadiumRequest(request);
        verifyNoInteractions(stadiumConverterMock, stadiumRepositoryMock);
    }

    @Test
    void createStadium_shouldThrowExceptionWhenSaveFails() {
        // Arrange
        CreateStadiumRequest request = CreateStadiumRequest.builder()
                .stadiumName("New Stadium")
                .build();

        StadiumEntity newStadiumEntity = StadiumEntity.builder()
                .stadiumName("New Stadium")
                .build();

        when(stadiumConverterMock.convertToEntity(request)).thenReturn(newStadiumEntity);
        when(stadiumRepositoryMock.save(newStadiumEntity)).thenThrow(new RuntimeException("Database error"));

        // Act & Assert
        assertThrows(RuntimeException.class, () -> createStadiumUseCase.createStadium(request));

        verify(stadiumValidatorMock).validateCreateStadiumRequest(request);
        verify(stadiumConverterMock).convertToEntity(request);
        verify(stadiumRepositoryMock).save(newStadiumEntity);
        verifyNoMoreInteractions(stadiumConverterMock);
    }
}