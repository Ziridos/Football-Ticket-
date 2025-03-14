package nl.fontys.s3.ticketmaster.business.impl.matchimpl;

import jakarta.persistence.EntityNotFoundException;
import nl.fontys.s3.ticketmaster.business.interfaces.clubinterfaces.ClubService;
import nl.fontys.s3.ticketmaster.business.interfaces.competitioninterfaces.CompetitionService;
import nl.fontys.s3.ticketmaster.domain.match.*;
import nl.fontys.s3.ticketmaster.persitence.entity.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class MatchConverterImplTest {
    @Mock
    private ClubService clubService;

    @Mock
    private CompetitionService competitionService;

    @InjectMocks
    private MatchConverterImpl matchConverter;

    private ClubEntity homeClub;
    private ClubEntity awayClub;
    private CompetitionEntity competition;
    private LocalDateTime matchDateTime;
    private Map<SeatEntity, Boolean> availableSeats;
    private MatchEntity matchEntity;

    @BeforeEach
    void setUp() {
        matchDateTime = LocalDateTime.now().plusDays(7);

        // Create test entities
        homeClub = ClubEntity.builder()
                .id(1L)
                .clubName("Home Club")
                .build();

        awayClub = ClubEntity.builder()
                .id(2L)
                .clubName("Away Club")
                .build();

        competition = CompetitionEntity.builder()
                .id(1L)
                .competitionName("Test Competition")
                .build();

        // Create test seats and availability map
        SeatEntity seat1 = SeatEntity.builder().id(1L).seatNumber("A1").build();
        SeatEntity seat2 = SeatEntity.builder().id(2L).seatNumber("A2").build();
        availableSeats = new HashMap<>();
        availableSeats.put(seat1, true);
        availableSeats.put(seat2, false);

        matchEntity = MatchEntity.builder()
                .id(1L)
                .homeClub(homeClub)
                .awayClub(awayClub)
                .matchDateTime(matchDateTime)
                .competition(competition)
                .availableSeats(availableSeats)
                .build();
    }

    @Test
    void convertToEntity_ValidRequest_ReturnsMatchEntity() {
        // Arrange
        CreateMatchRequest request = CreateMatchRequest.builder()
                .homeClubName("Home Club")
                .awayClubName("Away Club")
                .matchDateTime(matchDateTime)
                .competitionName("Test Competition")
                .build();

        when(clubService.findByClubName("Home Club")).thenReturn(Optional.of(homeClub));
        when(clubService.findByClubName("Away Club")).thenReturn(Optional.of(awayClub));
        when(competitionService.findByCompetitionName("Test Competition")).thenReturn(Optional.of(competition));

        // Act
        MatchEntity result = matchConverter.convertToEntity(request);

        // Assert
        assertNotNull(result);
        assertEquals(homeClub, result.getHomeClub());
        assertEquals(awayClub, result.getAwayClub());
        assertEquals(matchDateTime, result.getMatchDateTime());
        assertEquals(competition, result.getCompetition());
    }

    @Test
    void convertToEntity_HomeClubNotFound_ThrowsException() {
        // Arrange
        CreateMatchRequest request = CreateMatchRequest.builder()
                .homeClubName("Nonexistent Club")
                .awayClubName("Away Club")
                .matchDateTime(matchDateTime)
                .competitionName("Test Competition")
                .build();

        when(clubService.findByClubName("Nonexistent Club")).thenReturn(Optional.empty());

        // Act & Assert
        EntityNotFoundException exception = assertThrows(EntityNotFoundException.class,
                () -> matchConverter.convertToEntity(request));
        assertEquals("Home club not found", exception.getMessage());
    }

    @Test
    void convertToCreateMatchResponse_ValidEntity_ReturnsResponse() {
        // Act
        CreateMatchResponse response = matchConverter.convertToCreateMatchResponse(matchEntity);

        // Assert
        assertNotNull(response);
        assertEquals(matchEntity.getId(), response.getMatchId());
        assertEquals(homeClub, response.getHomeClub());
        assertEquals(awayClub, response.getAwayClub());
        assertEquals(matchDateTime, response.getMatchDateTime());
        assertEquals(competition, response.getCompetition());

        // Verify seat availability conversion
        Map<Long, Boolean> convertedSeats = response.getAvailableSeats();
        assertEquals(2, convertedSeats.size());
        assertTrue(convertedSeats.get(1L));
        assertFalse(convertedSeats.get(2L));
    }

    @Test
    void updateEntityFromRequest_ValidRequest_UpdatesEntity() {
        // Arrange
        UpdateMatchRequest request = UpdateMatchRequest.builder()
                .homeClubName("New Home Club")
                .awayClubName("New Away Club")
                .matchDateTime(matchDateTime.plusDays(1))
                .competitionName("New Competition")
                .build();

        ClubEntity newHomeClub = ClubEntity.builder().id(3L).clubName("New Home Club").build();
        ClubEntity newAwayClub = ClubEntity.builder().id(4L).clubName("New Away Club").build();
        CompetitionEntity newCompetition = CompetitionEntity.builder().id(2L).competitionName("New Competition").build();

        when(clubService.findByClubName("New Home Club")).thenReturn(Optional.of(newHomeClub));
        when(clubService.findByClubName("New Away Club")).thenReturn(Optional.of(newAwayClub));
        when(competitionService.findByCompetitionName("New Competition")).thenReturn(Optional.of(newCompetition));

        // Act
        MatchEntity result = matchConverter.updateEntityFromRequest(matchEntity, request);

        // Assert
        assertNotNull(result);
        assertEquals(newHomeClub, result.getHomeClub());
        assertEquals(newAwayClub, result.getAwayClub());
        assertEquals(request.getMatchDateTime(), result.getMatchDateTime());
        assertEquals(newCompetition, result.getCompetition());
    }

    @Test
    void convertToGetMatchResponse_ValidEntity_ReturnsResponse() {
        // Act
        GetMatchResponse response = matchConverter.convertToGetMatchResponse(matchEntity);

        // Assert
        assertNotNull(response);
        assertEquals(matchEntity.getId(), response.getMatchId());
        assertEquals(homeClub, response.getHomeClub());
        assertEquals(awayClub, response.getAwayClub());
        assertEquals(matchDateTime, response.getMatchDateTime());
        assertEquals(competition, response.getCompetition());

        Map<Long, Boolean> convertedSeats = response.getAvailableSeats();
        assertEquals(2, convertedSeats.size());
        assertTrue(convertedSeats.get(1L));
        assertFalse(convertedSeats.get(2L));
    }

    @Test
    void convertToUpdateMatchResponse_ValidEntity_ReturnsResponse() {
        // Act
        UpdateMatchResponse response = matchConverter.convertToUpdateMatchResponse(matchEntity);

        // Assert
        assertNotNull(response);
        assertEquals(matchEntity.getId(), response.getMatchId());
        assertEquals(homeClub, response.getHomeClub());
        assertEquals(awayClub, response.getAwayClub());
        assertEquals(matchDateTime, response.getMatchDateTime());
        assertEquals(competition, response.getCompetition());

        Map<Long, Boolean> convertedSeats = response.getAvailableSeats();
        assertEquals(2, convertedSeats.size());
        assertTrue(convertedSeats.get(1L));
        assertFalse(convertedSeats.get(2L));
    }

    @Test
    void convertToMatch_ValidEntity_ReturnsMatch() {
        // Act
        Match result = matchConverter.convertToMatch(matchEntity);

        // Assert
        assertNotNull(result);
        assertEquals(matchEntity.getId(), result.getMatchId());
        assertEquals(homeClub, result.getHomeClub());
        assertEquals(awayClub, result.getAwayClub());
        assertEquals(matchDateTime, result.getMatchDateTime());
        assertEquals(competition, result.getCompetition());

        Map<Long, Boolean> convertedSeats = result.getAvailableSeats();
        assertEquals(2, convertedSeats.size());
        assertTrue(convertedSeats.get(1L));
        assertFalse(convertedSeats.get(2L));
    }

    @Test
    void toMatchDTO_ValidEntity_ReturnsCorrectDTO() {
        // Arrange
        // Using the matchEntity already set up in setUp()

        // Act
        MatchDTO result = matchConverter.toMatchDTO(matchEntity);

        // Assert
        assertNotNull(result);
        assertEquals(matchEntity.getId(), result.getMatchId());
        assertEquals(homeClub, result.getHomeClub());
        assertEquals(awayClub, result.getAwayClub());
        assertEquals(matchDateTime, result.getMatchDateTime());
        assertEquals(competition, result.getCompetition());
    }



    @Test
    void toMatchDTO_EntityWithNullFields_HandlesNullsGracefully() {
        // Arrange
        MatchEntity entityWithNulls = MatchEntity.builder()
                .id(1L)
                .homeClub(null)
                .awayClub(null)
                .matchDateTime(null)
                .competition(null)
                .build();

        // Act
        MatchDTO result = matchConverter.toMatchDTO(entityWithNulls);

        // Assert
        assertNotNull(result);
        assertEquals(1L, result.getMatchId());
        assertNull(result.getHomeClub());
        assertNull(result.getAwayClub());
        assertNull(result.getMatchDateTime());
        assertNull(result.getCompetition());
    }

    @Test
    void toMatchDTO_EntityWithMinimalData_ReturnsCorrectDTO() {
        // Arrange
        MatchEntity minimalEntity = MatchEntity.builder()
                .id(1L)
                .matchDateTime(matchDateTime)
                .build();

        // Act
        MatchDTO result = matchConverter.toMatchDTO(minimalEntity);

        // Assert
        assertNotNull(result);
        assertEquals(1L, result.getMatchId());
        assertNull(result.getHomeClub());
        assertNull(result.getAwayClub());
        assertEquals(matchDateTime, result.getMatchDateTime());
        assertNull(result.getCompetition());
    }
}