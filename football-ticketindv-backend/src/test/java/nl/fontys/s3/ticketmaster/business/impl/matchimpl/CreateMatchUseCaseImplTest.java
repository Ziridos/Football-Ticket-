package nl.fontys.s3.ticketmaster.business.impl.matchimpl;

import nl.fontys.s3.ticketmaster.business.interfaces.matchinterfaces.MatchConverter;
import nl.fontys.s3.ticketmaster.business.interfaces.matchinterfaces.MatchValidator;
import nl.fontys.s3.ticketmaster.domain.match.CreateMatchRequest;
import nl.fontys.s3.ticketmaster.domain.match.CreateMatchResponse;
import nl.fontys.s3.ticketmaster.persitence.MatchRepository;
import nl.fontys.s3.ticketmaster.persitence.SeatRepository;
import nl.fontys.s3.ticketmaster.persitence.entity.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;


import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class CreateMatchUseCaseImplTest {
    @Mock
    private MatchRepository matchRepository;
    @Mock
    private SeatRepository seatRepository;
    @Mock
    private MatchValidator matchValidator;
    @Mock
    private MatchConverter matchConverter;

    @InjectMocks
    private CreateMatchUseCaseImpl createMatchUseCase;

    @Captor
    private ArgumentCaptor<MatchEntity> matchEntityCaptor;

    private CreateMatchRequest request;
    private MatchEntity matchEntity;
    private CreateMatchResponse expectedResponse;
    private List<SeatEntity> stadiumSeats;
    private StadiumEntity stadium;
    private ClubEntity homeClub;
    private ClubEntity awayClub;
    private CompetitionEntity competition;

    @BeforeEach
    void setUp() {
        LocalDateTime matchDateTime = LocalDateTime.now().plusDays(7);

        stadium = StadiumEntity.builder()
                .id(1L)
                .stadiumName("Test Stadium")
                .build();

        homeClub = ClubEntity.builder()
                .id(1L)
                .clubName("Home Club")
                .stadium(stadium)
                .build();

        awayClub = ClubEntity.builder()
                .id(2L)
                .clubName("Away Club")
                .build();

        competition = CompetitionEntity.builder()
                .id(1L)
                .competitionName("Test Competition")
                .build();

        // Create request with entities
        request = CreateMatchRequest.builder()
                .homeClubName(homeClub.getClubName())
                .awayClubName(awayClub.getClubName())
                .competitionName(competition.getCompetitionName())
                .matchDateTime(matchDateTime)
                .build();

        // Create match entity
        matchEntity = MatchEntity.builder()
                .id(1L)
                .homeClub(homeClub)
                .awayClub(awayClub)
                .competition(competition)
                .matchDateTime(matchDateTime)
                .availableSeats(new HashMap<>())
                .matchSpecificBoxPrices(new HashMap<>())
                .matchSpecificSeatPrices(new HashMap<>())
                .build();

        // Create stadium seats
        stadiumSeats = Arrays.asList(
                SeatEntity.builder().id(1L).seatNumber("A1").build(),
                SeatEntity.builder().id(2L).seatNumber("A2").build()
        );

        // Create expected response with entities
        expectedResponse = CreateMatchResponse.builder()
                .matchId(matchEntity.getId())
                .build();
    }

    @Test
    void createMatch_ValidRequest_ReturnsSuccessResponse() {
        // Arrange
        when(matchConverter.convertToEntity(request)).thenReturn(matchEntity);
        when(seatRepository.getSeatsByStadium(1L)).thenReturn(stadiumSeats);
        when(matchRepository.save(any(MatchEntity.class))).thenReturn(matchEntity);
        when(matchConverter.convertToCreateMatchResponse(matchEntity)).thenReturn(expectedResponse);

        // Act
        CreateMatchResponse response = createMatchUseCase.createMatch(request);

        // Assert
        assertNotNull(response);
        assertEquals(expectedResponse, response);

        // Verify all steps were executed
        verify(matchValidator).validateCreateMatchRequest(request);
        verify(matchConverter).convertToEntity(request);
        verify(seatRepository).getSeatsByStadium(1L);
        verify(matchRepository).save(matchEntityCaptor.capture());
        verify(matchConverter).convertToCreateMatchResponse(matchEntity);

        // Verify the match entity was properly configured before saving
        MatchEntity savedMatch = matchEntityCaptor.getValue();
        assertNotNull(savedMatch.getAvailableSeats());
        assertEquals(2, savedMatch.getAvailableSeats().size());
        assertTrue(savedMatch.getAvailableSeats().values().stream().allMatch(available -> available));
        assertNotNull(savedMatch.getMatchSpecificBoxPrices());
        assertNotNull(savedMatch.getMatchSpecificSeatPrices());
    }

    @Test
    void createMatch_ValidationFails_ThrowsException() {
        // Arrange
        doThrow(new RuntimeException("Validation failed"))
                .when(matchValidator).validateCreateMatchRequest(request);

        // Act & Assert
        RuntimeException exception = assertThrows(RuntimeException.class,
                () -> createMatchUseCase.createMatch(request));
        assertEquals("Validation failed", exception.getMessage());

        // Verify other methods were not called
        verify(matchConverter, never()).convertToEntity(any());
        verify(seatRepository, never()).getSeatsByStadium(any());
        verify(matchRepository, never()).save(any());
    }

    @Test
    void createMatch_ConversionFails_ThrowsException() {
        // Arrange
        doNothing().when(matchValidator).validateCreateMatchRequest(request);
        when(matchConverter.convertToEntity(request))
                .thenThrow(new RuntimeException("Conversion failed"));

        // Act & Assert
        RuntimeException exception = assertThrows(RuntimeException.class,
                () -> createMatchUseCase.createMatch(request));
        assertEquals("Conversion failed", exception.getMessage());

        // Verify subsequent methods were not called
        verify(seatRepository, never()).getSeatsByStadium(any());
        verify(matchRepository, never()).save(any());
    }

    @Test
    void createMatch_SeatRetrievalFails_ThrowsException() {
        // Arrange
        when(matchConverter.convertToEntity(request)).thenReturn(matchEntity);
        when(seatRepository.getSeatsByStadium(1L))
                .thenThrow(new RuntimeException("Failed to retrieve seats"));

        // Act & Assert
        RuntimeException exception = assertThrows(RuntimeException.class,
                () -> createMatchUseCase.createMatch(request));
        assertEquals("Failed to retrieve seats", exception.getMessage());

        // Verify save was not called
        verify(matchRepository, never()).save(any());
    }

    @Test
    void createMatch_SaveFails_ThrowsException() {
        // Arrange
        when(matchConverter.convertToEntity(request)).thenReturn(matchEntity);
        when(seatRepository.getSeatsByStadium(1L)).thenReturn(stadiumSeats);
        when(matchRepository.save(any(MatchEntity.class)))
                .thenThrow(new RuntimeException("Save failed"));

        // Act & Assert
        RuntimeException exception = assertThrows(RuntimeException.class,
                () -> createMatchUseCase.createMatch(request));
        assertEquals("Save failed", exception.getMessage());
    }
}