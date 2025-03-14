package nl.fontys.s3.ticketmaster.business.impl.matchimpl;

import nl.fontys.s3.ticketmaster.business.exception.InvalidMatchException;
import nl.fontys.s3.ticketmaster.business.interfaces.clubinterfaces.ClubValidator;
import nl.fontys.s3.ticketmaster.business.interfaces.competitioninterfaces.CompetitionValidator;
import nl.fontys.s3.ticketmaster.domain.match.CreateMatchRequest;
import nl.fontys.s3.ticketmaster.domain.match.UpdateMatchRequest;
import nl.fontys.s3.ticketmaster.persitence.MatchRepository;
import nl.fontys.s3.ticketmaster.persitence.entity.MatchEntity;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class MatchValidatorImplTest {

    @Mock
    private MatchRepository matchRepository;

    @Mock
    private ClubValidator clubValidator;

    @Mock
    private CompetitionValidator competitionValidator;

    @InjectMocks
    private MatchValidatorImpl matchValidator;

    private CreateMatchRequest createRequest;
    private UpdateMatchRequest updateRequest;
    private MatchEntity matchEntity;

    @BeforeEach
    void setUp() {
        createRequest = CreateMatchRequest.builder()
                .homeClubName("Home Club")
                .awayClubName("Away Club")
                .competitionName("Test Competition")
                .matchDateTime(LocalDateTime.now().plusDays(7))
                .build();

        updateRequest = UpdateMatchRequest.builder()
                .homeClubName("Updated Home Club")
                .awayClubName("Updated Away Club")
                .competitionName("Updated Competition")
                .matchDateTime(LocalDateTime.now().plusDays(14))
                .build();

        matchEntity = MatchEntity.builder()
                .id(1L)
                .matchDateTime(LocalDateTime.now().plusDays(7))
                .build();
    }

    @Test
    void validateCreateMatchRequest_ValidRequest_NoExceptionThrown() {
        // Arrange
        doNothing().when(clubValidator).validateClubs(
                createRequest.getHomeClubName(),
                createRequest.getAwayClubName()
        );
        doNothing().when(competitionValidator).validateCompetition(
                createRequest.getCompetitionName()
        );

        // Act & Assert
        assertDoesNotThrow(() -> matchValidator.validateCreateMatchRequest(createRequest));

        // Verify
        verify(clubValidator).validateClubs(
                createRequest.getHomeClubName(),
                createRequest.getAwayClubName()
        );
        verify(competitionValidator).validateCompetition(createRequest.getCompetitionName());
    }

    @Test
    void validateCreateMatchRequest_InvalidClubs_ThrowsException() {
        // Arrange
        doThrow(new RuntimeException("Invalid clubs"))
                .when(clubValidator)
                .validateClubs(createRequest.getHomeClubName(), createRequest.getAwayClubName());

        // Act & Assert
        Exception exception = assertThrows(RuntimeException.class,
                () -> matchValidator.validateCreateMatchRequest(createRequest));
        assertEquals("Invalid clubs", exception.getMessage());

        // Verify
        verify(competitionValidator, never()).validateCompetition(any());
    }

    @Test
    void validateCreateMatchRequest_InvalidCompetition_ThrowsException() {
        // Arrange
        doNothing().when(clubValidator).validateClubs(any(), any());
        doThrow(new RuntimeException("Invalid competition"))
                .when(competitionValidator)
                .validateCompetition(createRequest.getCompetitionName());

        // Act & Assert
        Exception exception = assertThrows(RuntimeException.class,
                () -> matchValidator.validateCreateMatchRequest(createRequest));
        assertEquals("Invalid competition", exception.getMessage());
    }

    @Test
    void validateUpdateMatchRequest_ValidRequest_NoExceptionThrown() {
        // Arrange
        when(matchRepository.existsById(1L)).thenReturn(true);
        doNothing().when(clubValidator).validateClubs(
                updateRequest.getHomeClubName(),
                updateRequest.getAwayClubName()
        );
        doNothing().when(competitionValidator).validateCompetition(
                updateRequest.getCompetitionName()
        );

        // Act & Assert
        assertDoesNotThrow(() -> matchValidator.validateUpdateMatchRequest(updateRequest, 1L));

        // Verify
        verify(matchRepository).existsById(1L);
        verify(clubValidator).validateClubs(
                updateRequest.getHomeClubName(),
                updateRequest.getAwayClubName()
        );
        verify(competitionValidator).validateCompetition(updateRequest.getCompetitionName());
    }

    @Test
    void validateUpdateMatchRequest_MatchDoesNotExist_ThrowsException() {
        // Arrange
        when(matchRepository.existsById(1L)).thenReturn(false);

        // Act & Assert
        InvalidMatchException exception = assertThrows(InvalidMatchException.class,
                () -> matchValidator.validateUpdateMatchRequest(updateRequest, 1L));
        assertEquals("Match with the given ID does not exist.", exception.getMessage());

        // Verify
        verify(clubValidator, never()).validateClubs(any(), any());
        verify(competitionValidator, never()).validateCompetition(any());
    }

    @Test
    void validateMatchExists_ExistingMatch_NoExceptionThrown() {
        // Arrange
        when(matchRepository.existsById(1L)).thenReturn(true);

        // Act & Assert
        assertDoesNotThrow(() -> matchValidator.validateMatchExists(1L));

        // Verify
        verify(matchRepository).existsById(1L);
    }

    @Test
    void validateMatchExists_NonExistingMatch_ThrowsException() {
        // Arrange
        when(matchRepository.existsById(1L)).thenReturn(false);

        // Act & Assert
        InvalidMatchException exception = assertThrows(InvalidMatchException.class,
                () -> matchValidator.validateMatchExists(1L));
        assertEquals("Match with the given ID does not exist.", exception.getMessage());
    }

    @Test
    void validateAndGetMatch_ExistingMatch_ReturnsMatch() {
        // Arrange
        when(matchRepository.findById(1L)).thenReturn(Optional.of(matchEntity));

        // Act
        MatchEntity result = matchValidator.validateAndGetMatch(1L);

        // Assert
        assertNotNull(result);
        assertEquals(matchEntity, result);
        verify(matchRepository).findById(1L);
    }

    @Test
    void validateAndGetMatch_NonExistingMatch_ThrowsException() {
        // Arrange
        when(matchRepository.findById(1L)).thenReturn(Optional.empty());

        // Act & Assert
        InvalidMatchException exception = assertThrows(InvalidMatchException.class,
                () -> matchValidator.validateAndGetMatch(1L));
        assertEquals("Match with the given ID does not exist.", exception.getMessage());
    }
}