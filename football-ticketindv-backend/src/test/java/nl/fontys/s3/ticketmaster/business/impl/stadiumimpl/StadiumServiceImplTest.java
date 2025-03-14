package nl.fontys.s3.ticketmaster.business.impl.stadiumimpl;

import nl.fontys.s3.ticketmaster.persitence.StadiumRepository;
import nl.fontys.s3.ticketmaster.persitence.entity.StadiumEntity;
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
class StadiumServiceImplTest {

    @Mock
    private StadiumRepository stadiumRepositoryMock;

    @InjectMocks
    private StadiumServiceImpl stadiumService;

    @Test
    void existsByName_shouldReturnTrueWhenStadiumExists() {
        when(stadiumRepositoryMock.existsByStadiumName("Existing Stadium")).thenReturn(true);
        assertTrue(stadiumService.existsByName("Existing Stadium"));
    }

    @Test
    void existsByName_shouldReturnFalseWhenStadiumDoesNotExist() {
        when(stadiumRepositoryMock.existsByStadiumName("Non-existing Stadium")).thenReturn(false);
        assertFalse(stadiumService.existsByName("Non-existing Stadium"));
    }

    @Test
    void existsById_shouldReturnTrueWhenStadiumExists() {
        when(stadiumRepositoryMock.existsById(1L)).thenReturn(true);
        assertTrue(stadiumService.existsById(1L));
    }

    @Test
    void existsById_shouldReturnFalseWhenStadiumDoesNotExist() {
        when(stadiumRepositoryMock.existsById(1L)).thenReturn(false);
        assertFalse(stadiumService.existsById(1L));
    }

    @Test
    void findById_shouldReturnStadiumWhenExists() {
        StadiumEntity stadium = StadiumEntity.builder()
                .id(1L)
                .stadiumName("Test Stadium")
                .build();

        when(stadiumRepositoryMock.findById(1L)).thenReturn(Optional.of(stadium));

        // Option 1: Compare the Optional directly
        assertEquals(Optional.of(stadium), stadiumService.findById(1L));


    }

    @Test
    void save_shouldReturnSavedStadium() {
        StadiumEntity stadium = StadiumEntity.builder().stadiumName("New Stadium").build();
        StadiumEntity savedStadium = StadiumEntity.builder().id(1L).stadiumName("New Stadium").build();
        when(stadiumRepositoryMock.save(stadium)).thenReturn(savedStadium);
        assertEquals(savedStadium, stadiumService.save(stadium));
    }

    @Test
    void findAll_shouldReturnAllStadiums() {
        List<StadiumEntity> stadiums = Arrays.asList(
                StadiumEntity.builder().id(1L).stadiumName("Stadium 1").build(),
                StadiumEntity.builder().id(2L).stadiumName("Stadium 2").build()
        );
        when(stadiumRepositoryMock.findAll()).thenReturn(stadiums);
        assertEquals(stadiums, stadiumService.findAll());
    }

    @Test
    void count_shouldReturnNumberOfStadiums() {
        when(stadiumRepositoryMock.count()).thenReturn(5L);
        assertEquals(5, stadiumService.count());
    }

    @Test
    void deleteById_shouldCallRepositoryMethod() {
        stadiumService.deleteById(1L);
        verify(stadiumRepositoryMock).deleteById(1L);
    }
}