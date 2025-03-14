package nl.fontys.s3.ticketmaster.business.impl.stadiumimpl;

import nl.fontys.s3.ticketmaster.business.interfaces.stadiuminterfaces.StadiumValidator;
import nl.fontys.s3.ticketmaster.business.interfaces.stadiuminterfaces.StadiumConverter;
import nl.fontys.s3.ticketmaster.domain.stadium.UpdateStadiumRequest;
import nl.fontys.s3.ticketmaster.domain.stadium.UpdateStadiumResponse;
import nl.fontys.s3.ticketmaster.persitence.StadiumRepository;
import nl.fontys.s3.ticketmaster.persitence.entity.StadiumEntity;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class UpdateStadiumUseCaseImplTest {

    @Mock
    private StadiumRepository stadiumRepositoryMock;

    @Mock
    private StadiumValidator stadiumValidatorMock;

    @Mock
    private StadiumConverter stadiumConverterMock;

    @InjectMocks
    private UpdateStadiumUseCaseImpl updateStadiumUseCase;

    @Test
    void updateStadium_shouldUpdateStadiumSuccessfully() {
        // Arrange
        Long stadiumId = 1L;
        UpdateStadiumRequest request = UpdateStadiumRequest.builder()
                .stadiumName("Updated Stadium")
                .stadiumAddress("123 Main St")
                .stadiumPostalCode("12345")
                .stadiumCity("City")
                .stadiumCountry("Country")
                .build();

        StadiumEntity existingStadium = StadiumEntity.builder()
                .id(stadiumId)
                .stadiumName("Old Stadium")
                .stadiumAddress("456 Old St")
                .stadiumPostalCode("67890")
                .stadiumCity("Old City")
                .stadiumCountry("Old Country")
                .build();

        StadiumEntity updatedStadium = StadiumEntity.builder()
                .id(stadiumId)
                .stadiumName(request.getStadiumName())
                .stadiumAddress(request.getStadiumAddress())
                .stadiumPostalCode(request.getStadiumPostalCode())
                .stadiumCity(request.getStadiumCity())
                .stadiumCountry(request.getStadiumCountry())
                .build();

        UpdateStadiumResponse expectedResponse = UpdateStadiumResponse.builder()
                .stadiumId(stadiumId)
                .stadiumName(request.getStadiumName())
                .stadiumAddress(request.getStadiumAddress())
                .stadiumPostalCode(request.getStadiumPostalCode())
                .stadiumCity(request.getStadiumCity())
                .stadiumCountry(request.getStadiumCountry())
                .build();

        when(stadiumRepositoryMock.findById(stadiumId)).thenReturn(Optional.ofNullable(existingStadium));
        when(stadiumRepositoryMock.save(any(StadiumEntity.class))).thenReturn(updatedStadium);
        when(stadiumConverterMock.convertToUpdateResponse(updatedStadium)).thenReturn(expectedResponse);

        // Act
        UpdateStadiumResponse response = updateStadiumUseCase.updateStadium(stadiumId, request);

        // Assert
        assertNotNull(response);
        assertEquals(expectedResponse, response);
        verify(stadiumValidatorMock).validateUpdateStadiumRequest(stadiumId, request);
        verify(stadiumRepositoryMock).findById(stadiumId);
        verify(stadiumRepositoryMock).save(argThat(savedStadium ->
                savedStadium.getStadiumName().equals(request.getStadiumName()) &&
                        savedStadium.getStadiumAddress().equals(request.getStadiumAddress()) &&
                        savedStadium.getStadiumPostalCode().equals(request.getStadiumPostalCode()) &&
                        savedStadium.getStadiumCity().equals(request.getStadiumCity()) &&
                        savedStadium.getStadiumCountry().equals(request.getStadiumCountry())
        ));
        verify(stadiumConverterMock).convertToUpdateResponse(updatedStadium);
    }

    @Test
    void updateStadium_shouldThrowExceptionWhenValidationFails() {
        // Arrange
        Long stadiumId = 1L;
        UpdateStadiumRequest request = UpdateStadiumRequest.builder()
                .stadiumName("Invalid Stadium")
                .build();
        doThrow(new IllegalArgumentException("Invalid update request"))
                .when(stadiumValidatorMock).validateUpdateStadiumRequest(stadiumId, request);

        // Act & Assert
        assertThrows(IllegalArgumentException.class, () -> updateStadiumUseCase.updateStadium(stadiumId, request));
        verify(stadiumValidatorMock).validateUpdateStadiumRequest(stadiumId, request);
        verifyNoInteractions(stadiumRepositoryMock, stadiumConverterMock);
    }
}