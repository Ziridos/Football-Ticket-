package nl.fontys.s3.ticketmaster.business.impl.blockimpl;

import nl.fontys.s3.ticketmaster.business.interfaces.blockinterfaces.BlockConverter;
import nl.fontys.s3.ticketmaster.business.interfaces.matchinterfaces.MatchService;

import nl.fontys.s3.ticketmaster.persitence.BlockRepository;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;


import java.util.List;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class GetMatchSpecificBlocksUseCaseImplTest {

    @Mock
    private BlockRepository blockRepositoryMock;

    @Mock
    private BlockConverter blockConverterMock;

    @Mock
    private MatchService matchServiceMock;

    @InjectMocks
    private GetMatchSpecificBlocksUseCaseImpl getMatchSpecificBlocksUseCase;




    @Test
    void getBlocksByStadiumForMatch_shouldThrowExceptionWhenMatchNotFound() {
        // Arrange
        Long boxId = 1L;
        Long matchId = 999L; // Invalid match ID

        when(blockRepositoryMock.findByBoxId(boxId)).thenReturn(List.of());
        when(matchServiceMock.getMatchById(matchId)).thenReturn(Optional.empty());

        // Act & Assert
        Exception exception = assertThrows(IllegalArgumentException.class, () -> {
            getMatchSpecificBlocksUseCase.getBlocksByStadiumForMatch(boxId, matchId);
        });

        assertEquals("Match not found for id: " + matchId, exception.getMessage());
        verify(blockRepositoryMock, times(1)).findByBoxId(boxId);
        verify(matchServiceMock, times(1)).getMatchById(matchId);
    }
}
