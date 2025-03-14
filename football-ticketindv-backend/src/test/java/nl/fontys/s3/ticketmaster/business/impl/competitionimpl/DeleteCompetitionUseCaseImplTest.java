package nl.fontys.s3.ticketmaster.business.impl.competitionimpl;

import nl.fontys.s3.ticketmaster.business.interfaces.competitioninterfaces.CompetitionValidator;
import nl.fontys.s3.ticketmaster.persitence.CompetitionRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;


import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class DeleteCompetitionUseCaseImplTest {

    @Mock
    private CompetitionRepository competitionRepositoryMock;
    @Mock
    private CompetitionValidator competitionValidatorMock;

    @InjectMocks
    private DeleteCompetitionUseCaseImpl deleteCompetitionUseCase;

    @Test
    void deleteCompetitionById_shouldDeleteCompetitionSuccessfully() {
        // Arrange
        Long competitionId = 1L;

        // Act
        deleteCompetitionUseCase.deleteCompetitionById(competitionId);

        // Assert
        verify(competitionValidatorMock).validateCompetitionExists(competitionId);
        verify(competitionRepositoryMock).deleteById(competitionId);
    }
}