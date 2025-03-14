package nl.fontys.s3.ticketmaster.business.impl.matchimpl;

import nl.fontys.s3.ticketmaster.persitence.MatchRepository;
import nl.fontys.s3.ticketmaster.persitence.entity.MatchEntity;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class MatchServiceImplTest {

    @Mock
    private MatchRepository matchRepositoryMock;

    @InjectMocks
    private MatchServiceImpl matchService;

    @Test
    void getMatchById_shouldReturnMatchWhenExists() {
        // Arrange
        Long matchId = 1L;
        MatchEntity expectedMatch = MatchEntity.builder()
                .id(matchId)
                .build();
        when(matchRepositoryMock.findById(matchId)).thenReturn(Optional.of(expectedMatch));

        // Act
        Optional<MatchEntity> result = matchService.getMatchById(matchId);

        // Assert
        assertTrue(result.isPresent());
        assertEquals(expectedMatch, result.get());  // Compare the actual entities
        verify(matchRepositoryMock).findById(matchId);
    }

    @Test
    void getMatchById_shouldReturnNullWhenMatchDoesNotExist() {
        // Arrange
        Long matchId = 1L;
        when(matchRepositoryMock.findById(matchId)).thenReturn(null);

        // Act
        Optional<MatchEntity> result = matchService.getMatchById(matchId);

        // Assert
        assertNull(result);
        verify(matchRepositoryMock).findById(matchId);
    }
}