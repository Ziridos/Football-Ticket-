package nl.fontys.s3.ticketmaster.business.impl.matchimpl;

import nl.fontys.s3.ticketmaster.business.interfaces.matchinterfaces.MatchConverter;
import nl.fontys.s3.ticketmaster.domain.match.GetAllMatchesRequest;
import nl.fontys.s3.ticketmaster.domain.match.GetAllMatchesResponse;
import nl.fontys.s3.ticketmaster.domain.match.Match;
import nl.fontys.s3.ticketmaster.persitence.MatchRepository;
import nl.fontys.s3.ticketmaster.persitence.entity.MatchEntity;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.*;

import java.time.LocalDate;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class GetAllMatchesUseCaseImplTest {

    @Mock
    private MatchRepository matchRepository;

    @Mock
    private MatchConverter matchConverter;

    @InjectMocks
    private GetAllMatchesUseCaseImpl getAllMatchesUseCase;

    @Test
    void getAllMatches_WithDefaultParameters_shouldReturnAllMatches() {
        // Arrange
        GetAllMatchesRequest request = GetAllMatchesRequest.builder()
                .page(0)
                .size(10)
                .sortBy("matchDateTime")
                .sortDirection("DESC")
                .build();

        List<MatchEntity> matchEntities = List.of(
                mock(MatchEntity.class),
                mock(MatchEntity.class)
        );

        List<Match> matches = List.of(
                mock(Match.class),
                mock(Match.class)
        );

        Page<MatchEntity> pageResponse = new PageImpl<>(matchEntities, PageRequest.of(0, 10), 2);

        when(matchRepository.findByFilters(
                eq(null), eq(null), eq(null), eq(null),
                any(Pageable.class))).thenReturn(pageResponse);
        when(matchConverter.convertToMatch(matchEntities.get(0))).thenReturn(matches.get(0));
        when(matchConverter.convertToMatch(matchEntities.get(1))).thenReturn(matches.get(1));

        // Act
        GetAllMatchesResponse response = getAllMatchesUseCase.getAllMatches(request);

        // Assert
        assertNotNull(response);
        assertEquals(matches, response.getMatches());
        assertEquals(2, response.getTotalElements());
        assertEquals(1, response.getTotalPages());
        assertEquals(0, response.getCurrentPage());
        verify(matchConverter, times(2)).convertToMatch(any(MatchEntity.class));
    }

    @Test
    void getAllMatches_WithFilters_shouldReturnFilteredMatches() {
        // Arrange
        LocalDate matchDate = LocalDate.now();
        GetAllMatchesRequest request = GetAllMatchesRequest.builder()
                .homeClubName("Home FC")
                .awayClubName("Away United")
                .competitionName("Premier League")
                .date(matchDate)
                .page(0)
                .size(10)
                .sortBy("matchDateTime")
                .sortDirection("DESC")
                .build();

        MatchEntity matchEntity = mock(MatchEntity.class);
        Match match = mock(Match.class);

        Page<MatchEntity> pageResponse = new PageImpl<>(List.of(matchEntity), PageRequest.of(0, 10), 1);

        when(matchRepository.findByFilters(
                eq("Home FC"),
                eq("Away United"),
                eq("Premier League"),
                eq(matchDate),
                any(Pageable.class))).thenReturn(pageResponse);
        when(matchConverter.convertToMatch(matchEntity)).thenReturn(match);

        // Act
        GetAllMatchesResponse response = getAllMatchesUseCase.getAllMatches(request);

        // Assert
        assertNotNull(response);
        assertEquals(1, response.getMatches().size());
        assertEquals(1, response.getTotalElements());
        assertEquals(1, response.getTotalPages());
        assertEquals(0, response.getCurrentPage());
        verify(matchConverter).convertToMatch(any(MatchEntity.class));
    }

    @Test
    void getAllMatches_WithPagination_shouldReturnPagedResults() {
        // Arrange
        GetAllMatchesRequest request = GetAllMatchesRequest.builder()
                .page(1)
                .size(2)
                .sortBy("matchDateTime")
                .sortDirection("DESC")
                .build();

        List<MatchEntity> matchEntities = List.of(
                mock(MatchEntity.class),
                mock(MatchEntity.class)
        );

        List<Match> matches = List.of(
                mock(Match.class),
                mock(Match.class)
        );

        // Total of 5 elements, showing page 1 (second page) with 2 elements per page
        Page<MatchEntity> pageResponse = new PageImpl<>(matchEntities, PageRequest.of(1, 2), 5);

        when(matchRepository.findByFilters(
                eq(null), eq(null), eq(null), eq(null),
                any(Pageable.class))).thenReturn(pageResponse);
        when(matchConverter.convertToMatch(matchEntities.get(0))).thenReturn(matches.get(0));
        when(matchConverter.convertToMatch(matchEntities.get(1))).thenReturn(matches.get(1));

        // Act
        GetAllMatchesResponse response = getAllMatchesUseCase.getAllMatches(request);

        // Assert
        assertNotNull(response);
        assertEquals(matches, response.getMatches());
        assertEquals(5, response.getTotalElements());
        assertEquals(3, response.getTotalPages());
        assertEquals(1, response.getCurrentPage());
        verify(matchConverter, times(2)).convertToMatch(any(MatchEntity.class));
    }

    @Test
    void getAllMatches_WithDateFilter_shouldReturnMatchesOnDate() {
        // Arrange
        LocalDate matchDate = LocalDate.now();
        GetAllMatchesRequest request = GetAllMatchesRequest.builder()
                .date(matchDate)
                .page(0)
                .size(10)
                .sortBy("matchDateTime")
                .sortDirection("ASC")
                .build();

        MatchEntity matchEntity = mock(MatchEntity.class);
        Match match = mock(Match.class);

        Page<MatchEntity> pageResponse = new PageImpl<>(List.of(matchEntity), PageRequest.of(0, 10), 1);

        when(matchRepository.findByFilters(
                eq(null), eq(null), eq(null), eq(matchDate),
                any(Pageable.class))).thenReturn(pageResponse);
        when(matchConverter.convertToMatch(matchEntity)).thenReturn(match);

        // Act
        GetAllMatchesResponse response = getAllMatchesUseCase.getAllMatches(request);

        // Assert
        assertNotNull(response);
        assertEquals(1, response.getMatches().size());
        assertEquals(1, response.getTotalElements());
        assertEquals(1, response.getTotalPages());
        assertEquals(0, response.getCurrentPage());
        verify(matchConverter).convertToMatch(any(MatchEntity.class));
    }
}