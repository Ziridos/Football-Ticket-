package nl.fontys.s3.ticketmaster.business.impl.competitionimpl;

import nl.fontys.s3.ticketmaster.persitence.CompetitionRepository;
import nl.fontys.s3.ticketmaster.persitence.entity.CompetitionEntity;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class CompetitionServiceImplTest {

    @Mock
    private CompetitionRepository competitionRepositoryMock;

    @InjectMocks
    private CompetitionServiceImpl competitionService;

    @Test
    void existsByName_shouldReturnTrueWhenCompetitionExists() {
        when(competitionRepositoryMock.existsByCompetitionName("Existing Competition")).thenReturn(true);
        assertTrue(competitionService.existsByName("Existing Competition"));
    }

    @Test
    void findByCompetitionName_shouldReturnCompetitionWhenExists() {
        CompetitionEntity competition = CompetitionEntity.builder()
                .competitionName("Test Competition")
                .build();
        when(competitionRepositoryMock.findByCompetitionName("Test Competition"))
                .thenReturn(Optional.of(competition));


        assertEquals(Optional.of(competition),
                competitionService.findByCompetitionName("Test Competition"));
    }

    @Test
    void existsById_shouldReturnTrueWhenCompetitionExists() {
        when(competitionRepositoryMock.existsById(1L)).thenReturn(true);
        assertTrue(competitionService.existsById(1L));
    }

    @Test
    void findById_shouldReturnCompetitionWhenExists() {
        CompetitionEntity competition = CompetitionEntity.builder()
                .id(1L)
                .build();
        when(competitionRepositoryMock.findById(1L))
                .thenReturn(Optional.of(competition));

        assertEquals(Optional.of(competition),
                competitionService.findById(1L));

    }

    @Test
    void save_shouldReturnSavedCompetition() {
        CompetitionEntity competition = CompetitionEntity.builder().competitionName("New Competition").build();
        when(competitionRepositoryMock.save(competition)).thenReturn(competition);
        assertEquals(competition, competitionService.save(competition));
    }

    @Test
    void findAll_shouldReturnAllCompetitions() {
        List<CompetitionEntity> competitions = Arrays.asList(
                CompetitionEntity.builder().id(1L).build(),
                CompetitionEntity.builder().id(2L).build()
        );
        when(competitionRepositoryMock.findAll()).thenReturn(competitions);
        assertEquals(competitions, competitionService.findAll());
    }

    @Test
    void count_shouldReturnNumberOfCompetitions() {
        when(competitionRepositoryMock.count()).thenReturn(5L);
        assertEquals(5, competitionService.count());
    }

    @Test
    void deleteById_shouldCallRepositoryMethod() {
        competitionService.deleteById(1L);
        verify(competitionRepositoryMock).deleteById(1L);
    }
}