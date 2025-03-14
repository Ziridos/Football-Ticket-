package nl.fontys.s3.ticketmaster.persitence;

import nl.fontys.s3.ticketmaster.persitence.entity.StadiumEntity;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.ANY)
class StadiumRepositoryTest {
    @Autowired
    private TestEntityManager entityManager;

    @Autowired
    private StadiumRepository stadiumRepository;

    private StadiumEntity createTestStadium(String name) {
        return StadiumEntity.builder()
                .stadiumName(name)
                .stadiumAddress("Test Address")
                .stadiumPostalCode("1234AB")
                .stadiumCity("Test City")
                .stadiumCountry("Test Country")
                .build();
    }

    @Test
    void existsByStadiumName_ExistingName_ReturnsTrue() {
        // Arrange
        StadiumEntity stadium = createTestStadium("Test Stadium 1");
        entityManager.persist(stadium);
        entityManager.flush();

        // Act
        boolean exists = stadiumRepository.existsByStadiumName("Test Stadium 1");

        // Assert
        assertTrue(exists);
    }

    @Test
    void existsByStadiumName_NonExistingName_ReturnsFalse() {
        // Act
        boolean exists = stadiumRepository.existsByStadiumName("Nonexistent Stadium");

        // Assert
        assertFalse(exists);
    }

    @Test
    void findByStadiumName_ExistingName_ReturnsStadium() {
        // Arrange
        StadiumEntity stadium = createTestStadium("Test Stadium 2");
        entityManager.persist(stadium);
        entityManager.flush();

        // Act
        Optional<StadiumEntity> foundStadium = stadiumRepository.findByStadiumName("Test Stadium 2");

        // Assert
        assertTrue(foundStadium.isPresent());
        assertEquals("Test Stadium 2", foundStadium.get().getStadiumName());
    }

    @Test
    void findByStadiumName_NonExistingName_ReturnsEmpty() {
        // Act
        Optional<StadiumEntity> foundStadium = stadiumRepository.findByStadiumName("Nonexistent Stadium");

        // Assert
        assertTrue(foundStadium.isEmpty());
    }

    @Test
    void save_ValidStadium_SavesSuccessfully() {
        // Arrange
        StadiumEntity stadium = createTestStadium("Test Stadium 3");

        // Act
        StadiumEntity savedStadium = stadiumRepository.save(stadium);
        entityManager.flush();

        // Assert
        StadiumEntity foundStadium = entityManager.find(StadiumEntity.class, savedStadium.getId());
        assertNotNull(foundStadium);
        assertEquals("Test Stadium 3", foundStadium.getStadiumName());
        assertEquals("Test Address", foundStadium.getStadiumAddress());
    }

    @Test
    void findById_ExistingId_ReturnsStadium() {
        // Arrange
        StadiumEntity stadium = createTestStadium("Test Stadium 4");
        entityManager.persist(stadium);
        entityManager.flush();

        // Act
        Optional<StadiumEntity> foundStadium = stadiumRepository.findById(stadium.getId());

        // Assert
        assertTrue(foundStadium.isPresent());
        assertEquals("Test Stadium 4", foundStadium.get().getStadiumName());
    }

    @Test
    void findById_NonExistingId_ReturnsEmpty() {
        // Act
        Optional<StadiumEntity> foundStadium = stadiumRepository.findById(999L);

        // Assert
        assertTrue(foundStadium.isEmpty());
    }

    @Test
    void delete_ExistingStadium_DeletesSuccessfully() {
        // Arrange
        StadiumEntity stadium = createTestStadium("Test Stadium 5");
        entityManager.persist(stadium);
        entityManager.flush();
        Long stadiumId = stadium.getId();

        // Act
        stadiumRepository.deleteById(stadiumId);
        entityManager.flush();

        // Assert
        StadiumEntity foundStadium = entityManager.find(StadiumEntity.class, stadiumId);
        assertNull(foundStadium);
    }

    @Test
    void findAll_MultipleStadiums_ReturnsAllStadiums() {
        // Arrange
        StadiumEntity stadium1 = createTestStadium("Test Stadium 6");
        StadiumEntity stadium2 = createTestStadium("Test Stadium 7");
        entityManager.persist(stadium1);
        entityManager.persist(stadium2);
        entityManager.flush();

        // Act
        List<StadiumEntity> stadiums = stadiumRepository.findAll();

        // Assert
        assertEquals(2, stadiums.size());
        assertTrue(stadiums.stream()
                .map(StadiumEntity::getStadiumName)
                .anyMatch(name -> name.equals("Test Stadium 6")));
        assertTrue(stadiums.stream()
                .map(StadiumEntity::getStadiumName)
                .anyMatch(name -> name.equals("Test Stadium 7")));
    }

    @Test
    void update_ExistingStadium_UpdatesSuccessfully() {
        // Arrange
        StadiumEntity stadium = createTestStadium("Test Stadium 8");
        entityManager.persist(stadium);
        entityManager.flush();

        // Act
        stadium.setStadiumName("Updated Stadium");
        stadium.setStadiumCity("Updated City");
        stadiumRepository.save(stadium);
        entityManager.flush();

        // Assert
        StadiumEntity foundStadium = entityManager.find(StadiumEntity.class, stadium.getId());
        assertEquals("Updated Stadium", foundStadium.getStadiumName());
        assertEquals("Updated City", foundStadium.getStadiumCity());
    }
}