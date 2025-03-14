package nl.fontys.s3.ticketmaster.business.impl.matchimpl;

import nl.fontys.s3.ticketmaster.business.interfaces.matchinterfaces.MatchConverter;
import nl.fontys.s3.ticketmaster.business.interfaces.matchinterfaces.MatchValidator;
import nl.fontys.s3.ticketmaster.domain.match.GetMatchRequest;
import nl.fontys.s3.ticketmaster.domain.match.GetMatchResponse;
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
class GetMatchUseCaseImplTest {

    @Mock
    private MatchRepository matchRepository;

    @Mock
    private MatchValidator matchValidator;

    @Mock
    private MatchConverter matchConverter;

    @InjectMocks
    private GetMatchUseCaseImpl getMatchUseCase;

    @Test
    void getMatch_shouldReturnMatchSuccessfully() {
        // Arrange
        Long matchId = 1L;
        GetMatchRequest request = new GetMatchRequest(matchId);
        MatchEntity matchEntity = mock(MatchEntity.class);
        GetMatchResponse expectedResponse = mock(GetMatchResponse.class);

        when(matchRepository.findById(matchId)).thenReturn(Optional.ofNullable(matchEntity));
        when(matchConverter.convertToGetMatchResponse(matchEntity)).thenReturn(expectedResponse);

        // Act
        GetMatchResponse actualResponse = getMatchUseCase.getMatch(request);

        // Assert
        assertSame(expectedResponse, actualResponse);
        verify(matchValidator).validateMatchExists(matchId);
        verify(matchRepository).findById(matchId);
        verify(matchConverter).convertToGetMatchResponse(matchEntity);
    }
}