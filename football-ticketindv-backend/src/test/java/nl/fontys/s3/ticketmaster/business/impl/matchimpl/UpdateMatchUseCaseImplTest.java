package nl.fontys.s3.ticketmaster.business.impl.matchimpl;


import nl.fontys.s3.ticketmaster.business.interfaces.matchinterfaces.MatchConverter;
import nl.fontys.s3.ticketmaster.business.interfaces.matchinterfaces.MatchValidator;
import nl.fontys.s3.ticketmaster.domain.match.UpdateMatchRequest;
import nl.fontys.s3.ticketmaster.domain.match.UpdateMatchResponse;
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
class UpdateMatchUseCaseImplTest {

    @Mock
    private MatchRepository matchRepository;

    @Mock
    private MatchValidator matchValidator;

    @Mock
    private MatchConverter matchConverter;

    @InjectMocks
    private UpdateMatchUseCaseImpl updateMatchUseCase;

    @Test
    void updateMatch_shouldUpdateMatchSuccessfully() {
        // Arrange
        Long matchId = 1L;
        UpdateMatchRequest request = mock(UpdateMatchRequest.class);
        MatchEntity existingMatch = mock(MatchEntity.class);
        MatchEntity updatedMatch = mock(MatchEntity.class);
        MatchEntity savedMatch = mock(MatchEntity.class);
        UpdateMatchResponse expectedResponse = mock(UpdateMatchResponse.class);

        when(matchRepository.findById(matchId)).thenReturn(Optional.ofNullable(existingMatch));
        when(matchConverter.updateEntityFromRequest(existingMatch, request)).thenReturn(updatedMatch);
        when(matchRepository.save(updatedMatch)).thenReturn(savedMatch);
        when(matchConverter.convertToUpdateMatchResponse(savedMatch)).thenReturn(expectedResponse);

        // Act
        UpdateMatchResponse actualResponse = updateMatchUseCase.updateMatch(request, matchId);

        // Assert
        assertSame(expectedResponse, actualResponse);
        verify(matchValidator).validateUpdateMatchRequest(request, matchId);
        verify(matchRepository).findById(matchId);
        verify(matchConverter).updateEntityFromRequest(existingMatch, request);
        verify(matchRepository).save(updatedMatch);
        verify(matchConverter).convertToUpdateMatchResponse(savedMatch);
    }


}