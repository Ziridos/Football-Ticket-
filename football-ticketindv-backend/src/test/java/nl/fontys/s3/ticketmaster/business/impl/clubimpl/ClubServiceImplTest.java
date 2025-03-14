package nl.fontys.s3.ticketmaster.business.impl.clubimpl;

import nl.fontys.s3.ticketmaster.persitence.ClubRepository;
import nl.fontys.s3.ticketmaster.persitence.entity.ClubEntity;
import nl.fontys.s3.ticketmaster.persitence.entity.StadiumEntity;
import org.junit.jupiter.api.BeforeEach;
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
class ClubServiceImplTest {

    @Mock
    private ClubRepository clubRepository;

    @InjectMocks
    private ClubServiceImpl clubService;

    private ClubEntity club1;
    private ClubEntity club2;
    private StadiumEntity stadium;

    @BeforeEach
    void setUp() {
        stadium = StadiumEntity.builder()
                .id(1L)
                .stadiumName("Test Stadium")
                .build();

        club1 = ClubEntity.builder()
                .id(1L)
                .clubName("Test Club 1")
                .stadium(stadium)
                .build();

        club2 = ClubEntity.builder()
                .id(2L)
                .clubName("Test Club 2")
                .stadium(stadium)
                .build();
    }

    @Test
    void existsByName_ExistingClub_ReturnsTrue() {
        // Arrange
        when(clubRepository.existsByClubName("Test Club 1")).thenReturn(true);

        // Act
        boolean exists = clubService.existsByName("Test Club 1");

        // Assert
        assertTrue(exists);
        verify(clubRepository).existsByClubName("Test Club 1");
    }

    @Test
    void existsByName_NonExistingClub_ReturnsFalse() {
        // Arrange
        when(clubRepository.existsByClubName("Nonexistent Club")).thenReturn(false);

        // Act
        boolean exists = clubService.existsByName("Nonexistent Club");

        // Assert
        assertFalse(exists);
        verify(clubRepository).existsByClubName("Nonexistent Club");
    }

    @Test
    void findByClubName_ExistingClub_ReturnsClub() {
        // Arrange
        when(clubRepository.findByClubName("Test Club 1")).thenReturn(Optional.of(club1));

        // Act
        Optional<ClubEntity> result = clubService.findByClubName("Test Club 1");

        // Assert
        assertTrue(result.isPresent());
        assertEquals(club1, result.get());
        verify(clubRepository).findByClubName("Test Club 1");
    }

    @Test
    void findByClubName_NonExistingClub_ReturnsEmpty() {
        // Arrange
        when(clubRepository.findByClubName("Nonexistent Club")).thenReturn(Optional.empty());

        // Act
        Optional<ClubEntity> result = clubService.findByClubName("Nonexistent Club");

        // Assert
        assertTrue(result.isEmpty());
        verify(clubRepository).findByClubName("Nonexistent Club");
    }

    @Test
    void existsById_ExistingId_ReturnsTrue() {
        // Arrange
        when(clubRepository.existsById(1L)).thenReturn(true);

        // Act
        boolean exists = clubService.existsById(1L);

        // Assert
        assertTrue(exists);
        verify(clubRepository).existsById(1L);
    }

    @Test
    void existsById_NonExistingId_ReturnsFalse() {
        // Arrange
        when(clubRepository.existsById(999L)).thenReturn(false);

        // Act
        boolean exists = clubService.existsById(999L);

        // Assert
        assertFalse(exists);
        verify(clubRepository).existsById(999L);
    }

    @Test
    void findById_ExistingId_ReturnsClub() {
        // Arrange
        when(clubRepository.findById(1L)).thenReturn(Optional.of(club1));

        // Act
        Optional<ClubEntity> result = clubService.findById(1L);

        // Assert
        assertTrue(result.isPresent());
        assertEquals(club1, result.get());
        verify(clubRepository).findById(1L);
    }

    @Test
    void findById_NonExistingId_ReturnsEmpty() {
        // Arrange
        when(clubRepository.findById(999L)).thenReturn(Optional.empty());

        // Act
        Optional<ClubEntity> result = clubService.findById(999L);

        // Assert
        assertTrue(result.isEmpty());
        verify(clubRepository).findById(999L);
    }

    @Test
    void save_ValidClub_ReturnsSavedClub() {
        // Arrange
        ClubEntity clubToSave = ClubEntity.builder()
                .clubName("New Club")
                .stadium(stadium)
                .build();

        when(clubRepository.save(clubToSave)).thenReturn(club1);

        // Act
        ClubEntity result = clubService.save(clubToSave);

        // Assert
        assertNotNull(result);
        assertEquals(club1, result);
        verify(clubRepository).save(clubToSave);
    }

    @Test
    void findAll_ExistingClubs_ReturnsAllClubs() {
        // Arrange
        List<ClubEntity> expectedClubs = Arrays.asList(club1, club2);
        when(clubRepository.findAll()).thenReturn(expectedClubs);

        // Act
        List<ClubEntity> result = clubService.findAll();

        // Assert
        assertNotNull(result);
        assertEquals(2, result.size());
        assertEquals(expectedClubs, result);
        verify(clubRepository).findAll();
    }

    @Test
    void findAll_NoClubs_ReturnsEmptyList() {
        // Arrange
        when(clubRepository.findAll()).thenReturn(List.of());

        // Act
        List<ClubEntity> result = clubService.findAll();

        // Assert
        assertNotNull(result);
        assertTrue(result.isEmpty());
        verify(clubRepository).findAll();
    }

    @Test
    void count_WithClubs_ReturnsCount() {
        // Arrange
        when(clubRepository.count()).thenReturn(2L);

        // Act
        int count = clubService.count();

        // Assert
        assertEquals(2, count);
        verify(clubRepository).count();
    }

    @Test
    void count_NoClubs_ReturnsZero() {
        // Arrange
        when(clubRepository.count()).thenReturn(0L);

        // Act
        int count = clubService.count();

        // Assert
        assertEquals(0, count);
        verify(clubRepository).count();
    }

    @Test
    void deleteById_ExistingId_DeletesClub() {
        // Arrange
        doNothing().when(clubRepository).deleteById(1L);

        // Act
        clubService.deleteById(1L);

        // Assert
        verify(clubRepository).deleteById(1L);
    }
}