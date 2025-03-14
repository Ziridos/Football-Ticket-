package nl.fontys.s3.ticketmaster.persitence;

import nl.fontys.s3.ticketmaster.persitence.entity.BoxPricingRuleEntity;
import nl.fontys.s3.ticketmaster.persitence.entity.StadiumEntity;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
class BoxPricingRuleRepositoryTest {
    @Autowired
    private TestEntityManager entityManager;

    @Autowired
    private BoxPricingRuleRepository boxPricingRuleRepository;

    private StadiumEntity stadium;
    private BoxPricingRuleEntity pricingRule1;
    private BoxPricingRuleEntity pricingRule2;

    @BeforeEach
    void setUp() {
        // Create and persist stadium
        stadium = StadiumEntity.builder()
                .stadiumName("Test Stadium")
                .stadiumAddress("Test Address")
                .stadiumPostalCode("1234AB")
                .stadiumCity("Test City")
                .stadiumCountry("Test Country")
                .build();
        entityManager.persist(stadium);

        // Create and persist pricing rules
        pricingRule1 = BoxPricingRuleEntity.builder()
                .stadium(stadium)
                .occupancyThreshold(0.75)
                .priceIncreasePercentage(10.0)
                .build();
        entityManager.persist(pricingRule1);

        pricingRule2 = BoxPricingRuleEntity.builder()
                .stadium(stadium)
                .occupancyThreshold(0.90)
                .priceIncreasePercentage(20.0)
                .build();
        entityManager.persist(pricingRule2);

        entityManager.flush();
    }

    @Test
    void findAllByStadiumId_ExistingStadium_ReturnsPricingRules() {
        // Act
        List<BoxPricingRuleEntity> foundRules = boxPricingRuleRepository.findAllByStadiumId(stadium.getId());

        // Assert
        assertNotNull(foundRules);
        assertEquals(2, foundRules.size());
        assertTrue(foundRules.stream().anyMatch(rule -> rule.getOccupancyThreshold() == 0.75));
        assertTrue(foundRules.stream().anyMatch(rule -> rule.getOccupancyThreshold() == 0.90));
    }

    @Test
    void findAllByStadiumId_NonExistingStadium_ReturnsEmptyList() {
        // Act
        List<BoxPricingRuleEntity> foundRules = boxPricingRuleRepository.findAllByStadiumId(999L);

        // Assert
        assertNotNull(foundRules);
        assertTrue(foundRules.isEmpty());
    }

    @Test
    void save_ValidPricingRule_SavesSuccessfully() {
        // Arrange
        BoxPricingRuleEntity newRule = BoxPricingRuleEntity.builder()
                .stadium(stadium)
                .occupancyThreshold(0.95)
                .priceIncreasePercentage(30.0)
                .build();

        // Act
        BoxPricingRuleEntity savedRule = boxPricingRuleRepository.save(newRule);
        entityManager.flush();

        // Assert
        BoxPricingRuleEntity foundRule = entityManager.find(BoxPricingRuleEntity.class, savedRule.getId());
        assertNotNull(foundRule);
        assertEquals(0.95, foundRule.getOccupancyThreshold());
        assertEquals(30.0, foundRule.getPriceIncreasePercentage());
        assertEquals(stadium.getId(), foundRule.getStadium().getId());
    }

    @Test
    void findById_ExistingRule_ReturnsRule() {
        // Act
        Optional<BoxPricingRuleEntity> foundRule = boxPricingRuleRepository.findById(pricingRule1.getId());

        // Assert
        assertTrue(foundRule.isPresent());
        assertEquals(0.75, foundRule.get().getOccupancyThreshold());
        assertEquals(10.0, foundRule.get().getPriceIncreasePercentage());
    }

    @Test
    void findById_NonExistingRule_ReturnsEmpty() {
        // Act
        Optional<BoxPricingRuleEntity> foundRule = boxPricingRuleRepository.findById(999L);

        // Assert
        assertFalse(foundRule.isPresent());
    }

    @Test
    void delete_ExistingRule_DeletesSuccessfully() {
        // Arrange
        Long ruleId = pricingRule1.getId();

        // Act
        boxPricingRuleRepository.deleteById(ruleId);
        entityManager.flush();

        // Assert
        BoxPricingRuleEntity foundRule = entityManager.find(BoxPricingRuleEntity.class, ruleId);
        assertNull(foundRule);
    }
}