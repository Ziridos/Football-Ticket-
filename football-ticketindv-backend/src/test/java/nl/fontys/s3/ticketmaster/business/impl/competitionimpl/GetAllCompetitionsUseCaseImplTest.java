package nl.fontys.s3.ticketmaster.business.impl.competitionimpl;

import nl.fontys.s3.ticketmaster.business.interfaces.competitioninterfaces.CompetitionConverter;
import nl.fontys.s3.ticketmaster.domain.competition.Competition;
import nl.fontys.s3.ticketmaster.domain.competition.GetAllCompetitionsRequest;
import nl.fontys.s3.ticketmaster.domain.competition.GetAllCompetitionsResponse;
import nl.fontys.s3.ticketmaster.persitence.CompetitionRepository;
import nl.fontys.s3.ticketmaster.persitence.entity.CompetitionEntity;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.*;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class GetAllCompetitionsUseCaseImplTest {

    @Mock
    private CompetitionRepository competitionRepositoryMock;
    @Mock
    private CompetitionConverter competitionConverterMock;

    @InjectMocks
    private GetAllCompetitionsUseCaseImpl getAllCompetitionsUseCase;

    @Test
    void getAllCompetitions_WithDefaultParameters_shouldReturnAllCompetitions() {
        // Arrange
        GetAllCompetitionsRequest request = GetAllCompetitionsRequest.builder()
                .page(0)
                .size(10)
                .sortBy("id")
                .sortDirection("ASC")
                .build();

        List<CompetitionEntity> competitionEntities = List.of(
                CompetitionEntity.builder().id(1L).competitionName("Competition 1").build(),
                CompetitionEntity.builder().id(2L).competitionName("Competition 2").build()
        );

        List<Competition> competitions = List.of(
                Competition.builder().competitionId(1L).competitionName("Competition 1").build(),
                Competition.builder().competitionId(2L).competitionName("Competition 2").build()
        );

        Page<CompetitionEntity> pageResponse = new PageImpl<>(competitionEntities, PageRequest.of(0, 10), 2);

        when(competitionRepositoryMock.findByFilters(
                eq(null),
                any(Pageable.class))).thenReturn(pageResponse);
        when(competitionConverterMock.convertToCompetition(competitionEntities.get(0))).thenReturn(competitions.get(0));
        when(competitionConverterMock.convertToCompetition(competitionEntities.get(1))).thenReturn(competitions.get(1));

        // Act
        GetAllCompetitionsResponse response = getAllCompetitionsUseCase.getAllCompetitions(request);

        // Assert
        assertNotNull(response);
        assertEquals(competitions, response.getCompetitions());
        assertEquals(2, response.getTotalElements());
        assertEquals(1, response.getTotalPages());
        assertEquals(0, response.getCurrentPage());
        verify(competitionConverterMock, times(2)).convertToCompetition(any(CompetitionEntity.class));
    }

    @Test
    void getAllCompetitions_WithFilters_shouldReturnFilteredCompetitions() {
        // Arrange
        GetAllCompetitionsRequest request = GetAllCompetitionsRequest.builder()
                .name("Premier")
                .page(0)
                .size(10)
                .sortBy("competitionName")
                .sortDirection("DESC")
                .build();

        CompetitionEntity competitionEntity = CompetitionEntity.builder()
                .id(1L)
                .competitionName("Premier League")
                .build();
        Competition competition = Competition.builder()
                .competitionId(1L)
                .competitionName("Premier League")
                .build();

        Page<CompetitionEntity> pageResponse = new PageImpl<>(List.of(competitionEntity), PageRequest.of(0, 10), 1);

        when(competitionRepositoryMock.findByFilters(
                eq("Premier"),
                any(Pageable.class))).thenReturn(pageResponse);
        when(competitionConverterMock.convertToCompetition(competitionEntity)).thenReturn(competition);

        // Act
        GetAllCompetitionsResponse response = getAllCompetitionsUseCase.getAllCompetitions(request);

        // Assert
        assertNotNull(response);
        assertEquals(1, response.getCompetitions().size());
        assertEquals(1, response.getTotalElements());
        assertEquals(1, response.getTotalPages());
        assertEquals(0, response.getCurrentPage());
        verify(competitionConverterMock).convertToCompetition(any(CompetitionEntity.class));
    }

    @Test
    void getAllCompetitions_WithPagination_shouldReturnPagedResults() {
        // Arrange
        GetAllCompetitionsRequest request = GetAllCompetitionsRequest.builder()
                .page(1)
                .size(2)
                .sortBy("id")
                .sortDirection("ASC")
                .build();

        List<CompetitionEntity> competitionEntities = List.of(
                CompetitionEntity.builder().id(3L).competitionName("Competition 3").build(),
                CompetitionEntity.builder().id(4L).competitionName("Competition 4").build()
        );

        List<Competition> competitions = List.of(
                Competition.builder().competitionId(3L).competitionName("Competition 3").build(),
                Competition.builder().competitionId(4L).competitionName("Competition 4").build()
        );

        // Total of 5 elements, showing page 1 (second page) with 2 elements per page
        Page<CompetitionEntity> pageResponse = new PageImpl<>(competitionEntities, PageRequest.of(1, 2), 5);

        when(competitionRepositoryMock.findByFilters(
                eq(null),
                any(Pageable.class))).thenReturn(pageResponse);
        when(competitionConverterMock.convertToCompetition(competitionEntities.get(0))).thenReturn(competitions.get(0));
        when(competitionConverterMock.convertToCompetition(competitionEntities.get(1))).thenReturn(competitions.get(1));

        // Act
        GetAllCompetitionsResponse response = getAllCompetitionsUseCase.getAllCompetitions(request);

        // Assert
        assertNotNull(response);
        assertEquals(competitions, response.getCompetitions());
        assertEquals(5, response.getTotalElements());
        assertEquals(3, response.getTotalPages());
        assertEquals(1, response.getCurrentPage());
        verify(competitionConverterMock, times(2)).convertToCompetition(any(CompetitionEntity.class));
    }

    @Test
    void getAllCompetitions_WithCustomSorting_shouldReturnSortedResults() {
        // Arrange
        GetAllCompetitionsRequest request = GetAllCompetitionsRequest.builder()
                .page(0)
                .size(10)
                .sortBy("competitionName")
                .sortDirection("DESC")
                .build();

        List<CompetitionEntity> competitionEntities = List.of(
                CompetitionEntity.builder().id(2L).competitionName("Premier League").build(),
                CompetitionEntity.builder().id(1L).competitionName("Eredivisie").build()
        );

        List<Competition> competitions = List.of(
                Competition.builder().competitionId(2L).competitionName("Premier League").build(),
                Competition.builder().competitionId(1L).competitionName("Eredivisie").build()
        );

        Page<CompetitionEntity> pageResponse = new PageImpl<>(competitionEntities, PageRequest.of(0, 10), 2);

        when(competitionRepositoryMock.findByFilters(
                eq(null),
                any(Pageable.class))).thenReturn(pageResponse);
        when(competitionConverterMock.convertToCompetition(competitionEntities.get(0))).thenReturn(competitions.get(0));
        when(competitionConverterMock.convertToCompetition(competitionEntities.get(1))).thenReturn(competitions.get(1));

        // Act
        GetAllCompetitionsResponse response = getAllCompetitionsUseCase.getAllCompetitions(request);

        // Assert
        assertNotNull(response);
        assertEquals(competitions, response.getCompetitions());
        assertEquals(2, response.getTotalElements());
        assertEquals(1, response.getTotalPages());
        assertEquals(0, response.getCurrentPage());
        verify(competitionConverterMock, times(2)).convertToCompetition(any(CompetitionEntity.class));
    }
}