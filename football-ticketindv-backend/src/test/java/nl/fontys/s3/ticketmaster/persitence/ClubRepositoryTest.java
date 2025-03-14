package nl.fontys.s3.ticketmaster.persitence;

import nl.fontys.s3.ticketmaster.persitence.entity.ClubEntity;
import nl.fontys.s3.ticketmaster.persitence.entity.StadiumEntity;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
class ClubRepositoryTest {
    @Autowired
    private TestEntityManager entityManager;

    @Autowired
    private ClubRepository clubRepository;

    private StadiumEntity stadium;

    @BeforeEach
    void setUp() {
        // Create and persist a stadium for the clubs
        stadium = StadiumEntity.builder()
                .stadiumName("Test Stadium")
                .stadiumAddress("Test Address")
                .stadiumPostalCode("1234AB")
                .stadiumCity("Test City")
                .stadiumCountry("Test Country")
                .build();
        entityManager.persist(stadium);
        entityManager.flush();
    }

    private ClubEntity createTestClub(String name) {
        return ClubEntity.builder()
                .clubName(name)
                .stadium(stadium)
                .build();
    }

    @Test
    void save_ValidClub_SavesSuccessfully() {
        // Arrange
        ClubEntity club = createTestClub("Test Club");

        // Act
        ClubEntity savedClub = clubRepository.save(club);
        clubRepository.flush();

        // Assert
        ClubEntity foundClub = entityManager.find(ClubEntity.class, savedClub.getId());
        assertNotNull(foundClub);
        assertEquals("Test Club", foundClub.getClubName());
        assertEquals(stadium.getId(), foundClub.getStadium().getId());
    }

    @Test
    void existsByClubName_ExistingName_ReturnsTrue() {
        // Arrange
        ClubEntity club = createTestClub("Test Club");
        entityManager.persist(club);
        entityManager.flush();

        // Act
        boolean exists = clubRepository.existsByClubName("Test Club");

        // Assert
        assertTrue(exists);
    }

    @Test
    void existsByClubName_NonExistingName_ReturnsFalse() {
        // Act
        boolean exists = clubRepository.existsByClubName("Non Existing Club");

        // Assert
        assertFalse(exists);
    }

    @Test
    void findByClubName_ExistingName_ReturnsClub() {
        // Arrange
        ClubEntity club = createTestClub("Test Club");
        entityManager.persist(club);
        entityManager.flush();

        // Act
        Optional<ClubEntity> foundClub = clubRepository.findByClubName("Test Club");

        // Assert
        assertTrue(foundClub.isPresent());
        assertEquals("Test Club", foundClub.get().getClubName());
        assertEquals(stadium.getId(), foundClub.get().getStadium().getId());
    }

    @Test
    void findByClubName_NonExistingName_ReturnsEmpty() {
        // Act
        Optional<ClubEntity> foundClub = clubRepository.findByClubName("Non Existing Club");

        // Assert
        assertFalse(foundClub.isPresent());
    }

    @Test
    void findById_ExistingId_ReturnsClub() {
        // Arrange
        ClubEntity club = createTestClub("Test Club");
        entityManager.persist(club);
        entityManager.flush();

        // Act
        Optional<ClubEntity> foundClub = clubRepository.findById(club.getId());

        // Assert
        assertTrue(foundClub.isPresent());
        assertEquals("Test Club", foundClub.get().getClubName());
        assertEquals(stadium.getId(), foundClub.get().getStadium().getId());
    }

    @Test
    void findById_NonExistingId_ReturnsEmpty() {
        // Act
        Optional<ClubEntity> foundClub = clubRepository.findById(999L);

        // Assert
        assertFalse(foundClub.isPresent());
    }

    @Test
    void delete_ExistingClub_DeletesSuccessfully() {
        // Arrange
        ClubEntity club = createTestClub("Test Club");
        entityManager.persist(club);
        entityManager.flush();
        Long clubId = club.getId();

        // Act
        clubRepository.deleteById(clubId);
        clubRepository.flush();

        // Assert
        ClubEntity foundClub = entityManager.find(ClubEntity.class, clubId);
        assertNull(foundClub);
    }
}