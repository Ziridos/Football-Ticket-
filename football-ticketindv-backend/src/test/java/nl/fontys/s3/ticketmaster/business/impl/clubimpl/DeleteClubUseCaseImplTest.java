package nl.fontys.s3.ticketmaster.business.impl.clubimpl;

import nl.fontys.s3.ticketmaster.business.interfaces.clubinterfaces.ClubValidator;
import nl.fontys.s3.ticketmaster.persitence.ClubRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class DeleteClubUseCaseImplTest {

    @Mock
    private ClubRepository clubRepositoryMock;

    @Mock
    private ClubValidator clubValidatorMock;

    @InjectMocks
    private DeleteClubUseCaseImpl deleteClubUseCase;

    @Test
    void deleteClub_shouldDeleteClubSuccessfully() {
        // Arrange
        Long clubId = 1L;

        // Act
        deleteClubUseCase.deleteClub(clubId);

        // Assert
        verify(clubValidatorMock).validateClubExists(clubId);
        verify(clubRepositoryMock).deleteById(clubId);
    }
}