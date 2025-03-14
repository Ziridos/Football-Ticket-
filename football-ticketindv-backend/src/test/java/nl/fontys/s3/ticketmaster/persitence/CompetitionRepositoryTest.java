package nl.fontys.s3.ticketmaster.persitence;

import nl.fontys.s3.ticketmaster.persitence.entity.CompetitionEntity;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
class CompetitionRepositoryTest {
    @Autowired
    private TestEntityManager entityManager;

    @Autowired
    private CompetitionRepository competitionRepository;

    private CompetitionEntity createTestCompetition(String name) {
        return CompetitionEntity.builder()
                .competitionName(name)
                .build();
    }

    @Test
    void save_ValidCompetition_SavesSuccessfully() {
        // Arrange
        CompetitionEntity competition = createTestCompetition("Test Competition");

        // Act
        CompetitionEntity savedCompetition = competitionRepository.save(competition);
        competitionRepository.flush();

        // Assert
        CompetitionEntity foundCompetition = entityManager.find(CompetitionEntity.class, savedCompetition.getId());
        assertNotNull(foundCompetition);
        assertEquals("Test Competition", foundCompetition.getCompetitionName());
    }

    @Test
    void existsByCompetitionName_ExistingName_ReturnsTrue() {
        // Arrange
        CompetitionEntity competition = createTestCompetition("Test Competition");
        entityManager.persist(competition);
        entityManager.flush();

        // Act
        boolean exists = competitionRepository.existsByCompetitionName("Test Competition");

        // Assert
        assertTrue(exists);
    }

    @Test
    void existsByCompetitionName_NonExistingName_ReturnsFalse() {
        // Act
        boolean exists = competitionRepository.existsByCompetitionName("Non Existing Competition");

        // Assert
        assertFalse(exists);
    }

    @Test
    void findByCompetitionName_ExistingName_ReturnsCompetition() {
        // Arrange
        CompetitionEntity competition = createTestCompetition("Test Competition");
        entityManager.persist(competition);
        entityManager.flush();

        // Act
        Optional<CompetitionEntity> foundCompetition = competitionRepository.findByCompetitionName("Test Competition");

        // Assert
        assertTrue(foundCompetition.isPresent());
        assertEquals("Test Competition", foundCompetition.get().getCompetitionName());
    }

    @Test
    void findByCompetitionName_NonExistingName_ReturnsEmpty() {
        // Act
        Optional<CompetitionEntity> foundCompetition = competitionRepository.findByCompetitionName("Non Existing Competition");

        // Assert
        assertFalse(foundCompetition.isPresent());
    }

    @Test
    void findById_ExistingId_ReturnsCompetition() {
        // Arrange
        CompetitionEntity competition = createTestCompetition("Test Competition");
        entityManager.persist(competition);
        entityManager.flush();

        // Act
        Optional<CompetitionEntity> foundCompetition = competitionRepository.findById(competition.getId());

        // Assert
        assertTrue(foundCompetition.isPresent());
        assertEquals("Test Competition", foundCompetition.get().getCompetitionName());
    }

    @Test
    void findById_NonExistingId_ReturnsEmpty() {
        // Act
        Optional<CompetitionEntity> foundCompetition = competitionRepository.findById(999L);

        // Assert
        assertFalse(foundCompetition.isPresent());
    }

    @Test
    void delete_ExistingCompetition_DeletesSuccessfully() {
        // Arrange
        CompetitionEntity competition = createTestCompetition("Test Competition");
        entityManager.persist(competition);
        entityManager.flush();
        Long competitionId = competition.getId();

        // Act
        competitionRepository.deleteById(competitionId);
        competitionRepository.flush();

        // Assert
        CompetitionEntity foundCompetition = entityManager.find(CompetitionEntity.class, competitionId);
        assertNull(foundCompetition);
    }
}