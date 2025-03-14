package nl.fontys.s3.ticketmaster.business.impl.stadiumimpl;

import nl.fontys.s3.ticketmaster.business.interfaces.stadiuminterfaces.StadiumValidator;
import nl.fontys.s3.ticketmaster.business.interfaces.stadiuminterfaces.StadiumConverter;
import nl.fontys.s3.ticketmaster.domain.stadium.GetStadiumRequest;
import nl.fontys.s3.ticketmaster.domain.stadium.GetStadiumResponse;
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
class GetStadiumUseCaseImplTest {

    @Mock
    private StadiumRepository stadiumRepositoryMock;

    @Mock
    private StadiumValidator stadiumValidatorMock;

    @Mock
    private StadiumConverter stadiumConverterMock;

    @InjectMocks
    private GetStadiumUseCaseImpl getStadiumUseCase;

    @Test
    void getStadiumById_shouldReturnStadium() {
        // Arrange
        Long stadiumId = 1L;
        GetStadiumRequest request = new GetStadiumRequest(stadiumId);
        StadiumEntity stadiumEntity = StadiumEntity.builder()
                .id(stadiumId)
                .stadiumName("Test Stadium")
                .stadiumAddress("123 Test St")
                .stadiumPostalCode("12345")
                .stadiumCity("Test City")
                .stadiumCountry("Test Country")
                .build();
        GetStadiumResponse expectedResponse = GetStadiumResponse.builder()
                .stadiumId(stadiumId)
                .stadiumName("Test Stadium")
                .stadiumAddress("123 Test St")
                .stadiumPostalCode("12345")
                .stadiumCity("Test City")
                .stadiumCountry("Test Country")
                .build();

        when(stadiumRepositoryMock.findById(stadiumId)).thenReturn(Optional.ofNullable(stadiumEntity));
        when(stadiumConverterMock.convertToGetResponse(stadiumEntity)).thenReturn(expectedResponse);

        // Act
        GetStadiumResponse response = getStadiumUseCase.getStadiumById(request);

        // Assert
        assertNotNull(response);
        assertEquals(expectedResponse, response);
        verify(stadiumValidatorMock).validateStadiumExists(stadiumId);
        verify(stadiumRepositoryMock).findById(stadiumId);
        verify(stadiumConverterMock).convertToGetResponse(stadiumEntity);
    }

    @Test
    void getStadiumById_shouldThrowExceptionWhenStadiumDoesNotExist() {
        // Arrange
        Long stadiumId = 1L;
        GetStadiumRequest request = new GetStadiumRequest(stadiumId);
        doThrow(new IllegalArgumentException("Stadium does not exist"))
                .when(stadiumValidatorMock).validateStadiumExists(stadiumId);

        // Act & Assert
        assertThrows(IllegalArgumentException.class, () -> getStadiumUseCase.getStadiumById(request));
        verify(stadiumValidatorMock).validateStadiumExists(stadiumId);
        verifyNoInteractions(stadiumRepositoryMock, stadiumConverterMock);
    }
}