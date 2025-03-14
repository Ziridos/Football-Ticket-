package nl.fontys.s3.ticketmaster.controller;

import nl.fontys.s3.ticketmaster.business.interfaces.matchinterfaces.*;
import nl.fontys.s3.ticketmaster.domain.match.*;
import nl.fontys.s3.ticketmaster.domain.seat.SeatAvailabilityResponse;
import nl.fontys.s3.ticketmaster.persitence.entity.ClubEntity;
import nl.fontys.s3.ticketmaster.persitence.entity.CompetitionEntity;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class MatchControllerTest {

    @Mock
    private CreateMatchUseCase createMatchUseCase;
    @Mock
    private GetAllMatchesUseCase getAllMatchesUseCase;
    @Mock
    private GetMatchUseCase getMatchUseCase;
    @Mock
    private DeleteMatchUseCase deleteMatchUseCase;
    @Mock
    private UpdateMatchUseCase updateMatchUseCase;
    @Mock
    private GetSeatAvailabilityUseCase getSeatAvailabilityUseCase;

    @InjectMocks
    private MatchController matchController;

    private CreateMatchRequest createMatchRequest;
    private CreateMatchResponse createMatchResponse;
    private GetMatchResponse getMatchResponse;
    private UpdateMatchRequest updateMatchRequest;
    private UpdateMatchResponse updateMatchResponse;
    private GetAllMatchesResponse getAllMatchesResponse;
    private LocalDateTime matchDateTime;
    private ClubEntity homeClub;
    private ClubEntity awayClub;
    private CompetitionEntity competition;

    @BeforeEach
    void setUp() {
        matchDateTime = LocalDateTime.now().plusDays(7);

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

        createMatchRequest = CreateMatchRequest.builder()
                .homeClubName("Home Club")
                .awayClubName("Away Club")
                .competitionName("Test Competition")
                .matchDateTime(matchDateTime)
                .build();

        createMatchResponse = CreateMatchResponse.builder()
                .matchId(1L)
                .homeClub(homeClub)
                .awayClub(awayClub)
                .competition(competition)
                .matchDateTime(matchDateTime)
                .build();

        getMatchResponse = GetMatchResponse.builder()
                .matchId(1L)
                .homeClub(homeClub)
                .awayClub(awayClub)
                .competition(competition)
                .matchDateTime(matchDateTime)
                .build();

        updateMatchRequest = UpdateMatchRequest.builder()
                .homeClubName("New Home Club")
                .awayClubName("New Away Club")
                .competitionName("New Competition")
                .matchDateTime(matchDateTime.plusDays(1))
                .build();

        updateMatchResponse = UpdateMatchResponse.builder()
                .matchId(1L)
                .homeClub(homeClub)
                .awayClub(awayClub)
                .competition(competition)
                .matchDateTime(matchDateTime.plusDays(1))
                .build();

        Match match = Match.builder()
                .matchId(1L)
                .homeClub(homeClub)
                .awayClub(awayClub)
                .competition(competition)
                .matchDateTime(matchDateTime)
                .build();

        getAllMatchesResponse = GetAllMatchesResponse.builder()
                .matches(Arrays.asList(match))
                .totalElements(1)
                .totalPages(1)
                .currentPage(0)
                .build();
    }

    @Test
    void getAllMatches_WithDefaultParameters_ReturnsAllMatches() {
        // Arrange
        GetAllMatchesRequest expectedRequest = GetAllMatchesRequest.builder()
                .page(0)
                .size(10)
                .sortBy("matchDateTime")
                .sortDirection("DESC")
                .build();

        when(getAllMatchesUseCase.getAllMatches(expectedRequest))
                .thenReturn(getAllMatchesResponse);

        // Act
        ResponseEntity<GetAllMatchesResponse> response = matchController.getAllMatches(
                null, null, null, null, 0, 10, "matchDateTime", "DESC");

        // Assert
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals(1, response.getBody().getMatches().size());
        assertEquals(1, response.getBody().getTotalPages());
        assertEquals(0, response.getBody().getCurrentPage());
        verify(getAllMatchesUseCase).getAllMatches(expectedRequest);
    }

    @Test
    void getAllMatches_WithFilters_ReturnsFilteredMatches() {
        // Arrange
        LocalDate date = LocalDate.now();
        GetAllMatchesRequest expectedRequest = GetAllMatchesRequest.builder()
                .homeClubName("Home Club")
                .awayClubName("Away Club")
                .competitionName("Competition")
                .date(date)
                .page(0)
                .size(10)
                .sortBy("matchDateTime")
                .sortDirection("DESC")
                .build();

        when(getAllMatchesUseCase.getAllMatches(expectedRequest))
                .thenReturn(getAllMatchesResponse);

        // Act
        ResponseEntity<GetAllMatchesResponse> response = matchController.getAllMatches(
                "Home Club", "Away Club", "Competition", date.toString(),
                0, 10, "matchDateTime", "DESC");

        // Assert
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        verify(getAllMatchesUseCase).getAllMatches(expectedRequest);
    }

    @Test
    void getAllMatches_WithPagination_ReturnsPagedResult() {
        // Arrange
        GetAllMatchesRequest expectedRequest = GetAllMatchesRequest.builder()
                .page(2)
                .size(5)
                .sortBy("matchDateTime")
                .sortDirection("ASC")
                .build();

        GetAllMatchesResponse pagedResponse = GetAllMatchesResponse.builder()
                .matches(Arrays.asList(new Match()))
                .totalElements(11)
                .totalPages(3)
                .currentPage(2)
                .build();

        when(getAllMatchesUseCase.getAllMatches(expectedRequest))
                .thenReturn(pagedResponse);

        // Act
        ResponseEntity<GetAllMatchesResponse> response = matchController.getAllMatches(
                null, null, null, null, 2, 5, "matchDateTime", "ASC");

        // Assert
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals(2, response.getBody().getCurrentPage());
        assertEquals(3, response.getBody().getTotalPages());
        assertEquals(11, response.getBody().getTotalElements());
        verify(getAllMatchesUseCase).getAllMatches(expectedRequest);
    }

    @Test
    void getAllMatches_WithCustomSorting_ReturnsSortedResult() {
        // Arrange
        GetAllMatchesRequest expectedRequest = GetAllMatchesRequest.builder()
                .page(0)
                .size(10)
                .sortBy("homeClub")
                .sortDirection("ASC")
                .build();

        when(getAllMatchesUseCase.getAllMatches(expectedRequest))
                .thenReturn(getAllMatchesResponse);

        // Act
        ResponseEntity<GetAllMatchesResponse> response = matchController.getAllMatches(
                null, null, null, null, 0, 10, "homeClub", "ASC");

        // Assert
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        verify(getAllMatchesUseCase).getAllMatches(expectedRequest);
    }

    // Rest of the existing tests remain the same...
    @Test
    void createMatch_ValidRequest_ReturnsCreatedMatch() {
        when(createMatchUseCase.createMatch(createMatchRequest))
                .thenReturn(createMatchResponse);

        ResponseEntity<CreateMatchResponse> response = matchController.createMatch(createMatchRequest);

        assertEquals(HttpStatus.CREATED, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals(createMatchResponse, response.getBody());
        assertEquals(1L, response.getBody().getMatchId());
        verify(createMatchUseCase).createMatch(createMatchRequest);
    }

    @Test
    void getMatch_ExistingMatch_ReturnsMatch() {
        GetMatchRequest request = new GetMatchRequest(1L);
        when(getMatchUseCase.getMatch(request))
                .thenReturn(getMatchResponse);

        ResponseEntity<GetMatchResponse> response = matchController.getMatch(1L);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals(getMatchResponse, response.getBody());
        assertEquals(1L, response.getBody().getMatchId());
        verify(getMatchUseCase).getMatch(request);
    }

    @Test
    void deleteMatch_ExistingMatch_ReturnsNoContent() {
        doNothing().when(deleteMatchUseCase).deleteMatch(1L);

        ResponseEntity<Void> response = matchController.deleteMatch(1L);

        assertEquals(HttpStatus.NO_CONTENT, response.getStatusCode());
        assertNull(response.getBody());
        verify(deleteMatchUseCase).deleteMatch(1L);
    }

    @Test
    void updateMatch_ValidRequest_ReturnsUpdatedMatch() {
        when(updateMatchUseCase.updateMatch(updateMatchRequest, 1L))
                .thenReturn(updateMatchResponse);

        ResponseEntity<UpdateMatchResponse> response =
                matchController.updateMatch(1L, updateMatchRequest);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals(updateMatchResponse, response.getBody());
        assertEquals(1L, response.getBody().getMatchId());
        verify(updateMatchUseCase).updateMatch(updateMatchRequest, 1L);
    }

    @Test
    void getSeatAvailability_ReturnsAvailableSeats() {
        List<SeatAvailabilityResponse> seatResponses = Arrays.asList(
                SeatAvailabilityResponse.builder()
                        .seatId(1L)
                        .seatNumber("A1")
                        .xPosition(1)
                        .yPosition(1)
                        .isAvailable(true)
                        .blockId(1L)
                        .price(50.0)
                        .build(),
                SeatAvailabilityResponse.builder()
                        .seatId(2L)
                        .seatNumber("A2")
                        .xPosition(2)
                        .yPosition(1)
                        .isAvailable(false)
                        .blockId(1L)
                        .price(50.0)
                        .build()
        );

        when(getSeatAvailabilityUseCase.getSeatAvailability(1L))
                .thenReturn(seatResponses);

        ResponseEntity<List<SeatAvailabilityResponse>> response =
                matchController.getSeatAvailability(1L);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals(2, response.getBody().size());
        assertTrue(response.getBody().get(0).getIsAvailable());
        assertFalse(response.getBody().get(1).getIsAvailable());
        verify(getSeatAvailabilityUseCase).getSeatAvailability(1L);
    }

    @Test
    void createMatch_InvalidRequest_UseCaseThrowsException() {
        when(createMatchUseCase.createMatch(createMatchRequest))
                .thenThrow(new IllegalArgumentException("Invalid match data"));

        Exception exception = assertThrows(IllegalArgumentException.class,
                () -> matchController.createMatch(createMatchRequest));
        assertEquals("Invalid match data", exception.getMessage());
        verify(createMatchUseCase).createMatch(createMatchRequest);
    }

    @Test
    void getMatch_NonExistingMatch_UseCaseThrowsException() {
        GetMatchRequest request = new GetMatchRequest(999L);
        when(getMatchUseCase.getMatch(request))
                .thenThrow(new IllegalArgumentException("Match not found"));

        Exception exception = assertThrows(IllegalArgumentException.class,
                () -> matchController.getMatch(999L));
        assertEquals("Match not found", exception.getMessage());
        verify(getMatchUseCase).getMatch(request);
    }

    @Test
    void updateMatch_InvalidRequest_UseCaseThrowsException() {
        when(updateMatchUseCase.updateMatch(updateMatchRequest, 1L))
                .thenThrow(new IllegalArgumentException("Invalid update data"));

        Exception exception = assertThrows(IllegalArgumentException.class,
                () -> matchController.updateMatch(1L, updateMatchRequest));
        assertEquals("Invalid update data", exception.getMessage());
        verify(updateMatchUseCase).updateMatch(updateMatchRequest, 1L);
    }

    @Test
    void getSeatAvailability_MatchNotFound_UseCaseThrowsException() {
        when(getSeatAvailabilityUseCase.getSeatAvailability(999L))
                .thenThrow(new IllegalArgumentException("Match not found"));

        Exception exception = assertThrows(IllegalArgumentException.class,
                () -> matchController.getSeatAvailability(999L));
        assertEquals("Match not found", exception.getMessage());
        verify(getSeatAvailabilityUseCase).getSeatAvailability(999L);
    }
}