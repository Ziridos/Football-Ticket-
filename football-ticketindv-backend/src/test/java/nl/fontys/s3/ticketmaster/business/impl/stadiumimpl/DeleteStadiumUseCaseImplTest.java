package nl.fontys.s3.ticketmaster.business.impl.stadiumimpl;

import nl.fontys.s3.ticketmaster.business.interfaces.stadiuminterfaces.StadiumValidator;
import nl.fontys.s3.ticketmaster.persitence.StadiumRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class DeleteStadiumUseCaseImplTest {

    @Mock
    private StadiumRepository stadiumRepositoryMock;

    @Mock
    private StadiumValidator stadiumValidatorMock;

    @InjectMocks
    private DeleteStadiumUseCaseImpl deleteStadiumUseCase;

    @Test
    void deleteStadiumById_shouldDeleteStadiumSuccessfully() {
        // Arrange
        Long stadiumId = 1L;

        // Act
        deleteStadiumUseCase.deleteStadiumById(stadiumId);

        // Assert
        verify(stadiumValidatorMock).validateStadiumExists(stadiumId);
        verify(stadiumRepositoryMock).deleteById(stadiumId);
    }

    @Test
    void deleteStadiumById_shouldThrowExceptionWhenStadiumDoesNotExist() {
        // Arrange
        Long stadiumId = 1L;
        doThrow(new IllegalArgumentException("Stadium does not exist"))
                .when(stadiumValidatorMock).validateStadiumExists(stadiumId);

        // Act & Assert
        assertThrows(IllegalArgumentException.class, () -> deleteStadiumUseCase.deleteStadiumById(stadiumId));
        verify(stadiumValidatorMock).validateStadiumExists(stadiumId);
        verifyNoInteractions(stadiumRepositoryMock);
    }
}