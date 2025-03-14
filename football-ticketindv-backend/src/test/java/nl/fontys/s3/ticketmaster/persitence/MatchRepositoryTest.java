package nl.fontys.s3.ticketmaster.persitence;

import nl.fontys.s3.ticketmaster.persitence.entity.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
class MatchRepositoryTest {
    @Autowired
    private TestEntityManager entityManager;

    @Autowired
    private MatchRepository matchRepository;

    private StadiumEntity stadium;
    private ClubEntity homeClub;
    private ClubEntity awayClub;
    private CompetitionEntity competition;
    private BoxEntity box;
    private BlockEntity block;
    private SeatEntity seat;
    private MatchEntity match;

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

        // Create and persist clubs
        homeClub = ClubEntity.builder()
                .clubName("Home Club")
                .stadium(stadium)
                .build();
        entityManager.persist(homeClub);

        awayClub = ClubEntity.builder()
                .clubName("Away Club")
                .stadium(stadium)
                .build();
        entityManager.persist(awayClub);

        // Create and persist competition
        competition = CompetitionEntity.builder()
                .competitionName("Test Competition")
                .build();
        entityManager.persist(competition);

        // Create and persist box
        box = BoxEntity.builder()
                .boxName("Test Box")
                .stadium(stadium)
                .price(100.0)
                .xPosition(0)
                .yPosition(0)
                .width(10)
                .height(10)
                .build();
        entityManager.persist(box);

        // Create and persist block
        block = BlockEntity.builder()
                .blockName("Test Block")
                .box(box)
                .xPosition(0)
                .yPosition(0)
                .width(5)
                .height(5)
                .build();
        entityManager.persist(block);

        // Create and persist seat
        seat = SeatEntity.builder()
                .seatNumber("A1")
                .block(block)
                .price(50.0)
                .xPosition(0)
                .yPosition(0)
                .build();
        entityManager.persist(seat);

        // Create and persist match with available seat
        Map<SeatEntity, Boolean> availableSeats = new HashMap<>();
        availableSeats.put(seat, true);

        Map<BoxEntity, Double> boxPrices = new HashMap<>();
        boxPrices.put(box, 150.0);

        Map<SeatEntity, Double> seatPrices = new HashMap<>();
        seatPrices.put(seat, 75.0);

        match = MatchEntity.builder()
                .homeClub(homeClub)
                .awayClub(awayClub)
                .matchDateTime(LocalDateTime.now().plusDays(7))
                .competition(competition)
                .availableSeats(availableSeats)
                .matchSpecificBoxPrices(boxPrices)
                .matchSpecificSeatPrices(seatPrices)
                .build();
        entityManager.persist(match);

        entityManager.flush();
    }

    @Test
    void save_ValidMatch_SavesSuccessfully() {
        // Assert
        MatchEntity foundMatch = entityManager.find(MatchEntity.class, match.getId());
        assertNotNull(foundMatch);
        assertEquals(homeClub.getId(), foundMatch.getHomeClub().getId());
        assertEquals(awayClub.getId(), foundMatch.getAwayClub().getId());
        assertTrue(foundMatch.getAvailableSeats().containsKey(seat));
    }

    @Test
    void findById_ExistingId_ReturnsMatch() {
        // Act
        Optional<MatchEntity> foundMatch = matchRepository.findById(match.getId());

        // Assert
        assertTrue(foundMatch.isPresent());
        assertEquals(homeClub.getId(), foundMatch.get().getHomeClub().getId());
    }

    @Test
    void findById_NonExistingId_ReturnsEmpty() {
        // Act
        Optional<MatchEntity> foundMatch = matchRepository.findById(999L);

        // Assert
        assertFalse(foundMatch.isPresent());
    }

    @Test
    void isSeatAvailable_AvailableSeat_ReturnsTrue() {
        // Act
        boolean isAvailable = matchRepository.isSeatAvailable(match.getId(), seat.getId());

        // Assert
        assertTrue(isAvailable);
    }

    @Test
    void isSeatAvailable_UnavailableSeat_ReturnsFalse() {
        // Arrange
        match.getAvailableSeats().put(seat, false);
        entityManager.persist(match);
        entityManager.flush();

        // Act
        boolean isAvailable = matchRepository.isSeatAvailable(match.getId(), seat.getId());

        // Assert
        assertFalse(isAvailable);
    }

    @Test
    void isSeatAvailable_NonExistingSeat_ReturnsFalse() {
        // Act
        boolean isAvailable = matchRepository.isSeatAvailable(match.getId(), 999L);

        // Assert
        assertFalse(isAvailable);
    }

    @Test
    void updateSeatAvailability_ExistingSeat_UpdatesSuccessfully() {
        // Act
        matchRepository.updateSeatAvailability(match.getId(), seat.getId().toString(), false);
        entityManager.flush();

        // Verify directly with a query
        entityManager.clear(); // Clear persistence context to force a fresh read

        // Check if the seat is now unavailable
        boolean isStillAvailable = matchRepository.isSeatAvailable(match.getId(), seat.getId());
        assertFalse(isStillAvailable);
    }

    @Test
    void delete_ExistingMatch_DeletesSuccessfully() {
        // Arrange
        Long matchId = match.getId();

        // Act
        matchRepository.deleteById(matchId);
        matchRepository.flush();

        // Assert
        MatchEntity foundMatch = entityManager.find(MatchEntity.class, matchId);
        assertNull(foundMatch);
    }
}