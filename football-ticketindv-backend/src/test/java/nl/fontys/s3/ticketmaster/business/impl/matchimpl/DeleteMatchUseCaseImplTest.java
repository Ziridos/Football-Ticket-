package nl.fontys.s3.ticketmaster.business.impl.matchimpl;

import nl.fontys.s3.ticketmaster.business.interfaces.matchinterfaces.MatchValidator;
import nl.fontys.s3.ticketmaster.persitence.MatchRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class DeleteMatchUseCaseImplTest {

    @Mock
    private MatchRepository matchRepository;

    @Mock
    private MatchValidator matchValidator;

    @InjectMocks
    private DeleteMatchUseCaseImpl deleteMatchUseCase;

    @Test
    void deleteMatch_shouldDeleteMatchSuccessfully() {
        // Arrange
        Long matchId = 1L;

        // Act
        deleteMatchUseCase.deleteMatch(matchId);

        // Assert
        verify(matchValidator).validateMatchExists(matchId);
        verify(matchRepository).deleteById(matchId);
    }
}