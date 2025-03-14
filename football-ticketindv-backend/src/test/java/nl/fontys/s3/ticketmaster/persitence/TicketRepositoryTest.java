package nl.fontys.s3.ticketmaster.persitence;

import nl.fontys.s3.ticketmaster.domain.user.Role;
import nl.fontys.s3.ticketmaster.persitence.entity.*;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.test.context.TestPropertySource;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.ANY)
@TestPropertySource(properties = {
        "spring.jpa.properties.hibernate.globally_quoted_identifiers=true"
})
class TicketRepositoryTest {
    @Autowired
    private TestEntityManager entityManager;

    @Autowired
    private TicketRepository ticketRepository;

    private StadiumEntity createTestStadium() {
        return entityManager.persist(StadiumEntity.builder()
                .stadiumName("Test Stadium")
                .stadiumAddress("Test Address")
                .stadiumPostalCode("1234AB")
                .stadiumCity("Test City")
                .stadiumCountry("Test Country")
                .build());
    }

    private ClubEntity createTestClub(StadiumEntity stadium, String clubName) {
        return entityManager.persist(ClubEntity.builder()
                .clubName(clubName)
                .stadium(stadium)
                .build());
    }

    private BoxEntity createTestBox(StadiumEntity stadium) {
        return entityManager.persist(BoxEntity.builder()
                .boxName("Test Box")
                .xPosition(0)
                .yPosition(0)
                .width(10)
                .height(10)
                .stadium(stadium)
                .price(100.0)
                .build());
    }

    private BlockEntity createTestBlock(BoxEntity box) {
        return entityManager.persist(BlockEntity.builder()
                .blockName("Test Block")
                .xPosition(0)
                .yPosition(0)
                .width(5)
                .height(5)
                .box(box)
                .build());
    }

    private SeatEntity createTestSeat(BlockEntity block, double price) {
        return entityManager.persist(SeatEntity.builder()
                .seatNumber("A1")
                .xPosition(0)
                .yPosition(0)
                .block(block)
                .price(price)
                .build());
    }

    private UserEntity createTestUser() {
        return entityManager.persist(UserEntity.builder()
                .name("Test User")
                .email("test@test.com")
                .password("password123")
                .address("Test Address")
                .phone("1234567890")
                .country("Test Country")
                .city("Test City")
                .postalCode("1234AB")
                .role(Role.USER)
                .build());
    }

    private CompetitionEntity createTestCompetition() {
        return entityManager.persist(CompetitionEntity.builder()
                .competitionName("Test Competition")
                .build());
    }

    private MatchEntity createTestMatch(String homeClubName, String awayClubName) {
        StadiumEntity stadium = createTestStadium();
        ClubEntity homeClub = createTestClub(stadium, homeClubName);
        ClubEntity awayClub = createTestClub(stadium, awayClubName);
        CompetitionEntity competition = createTestCompetition();

        return entityManager.persist(MatchEntity.builder()
                .homeClub(homeClub)
                .awayClub(awayClub)
                .matchDateTime(LocalDateTime.now().plusDays(1))
                .competition(competition)
                .availableSeats(new HashMap<>())
                .matchSpecificBoxPrices(new HashMap<>())
                .matchSpecificSeatPrices(new HashMap<>())
                .build());
    }

    @Test
    void findByUser_UserWithTickets_ReturnsTicketList() {
        // Arrange
        UserEntity user = createTestUser();
        MatchEntity match = createTestMatch("Home Team 1", "Away Team 1");

        BoxEntity box = createTestBox(match.getHomeClub().getStadium());
        BlockEntity block = createTestBlock(box);
        List<SeatEntity> seats = Arrays.asList(
                createTestSeat(block, 50.0),
                createTestSeat(block, 75.0)
        );

        entityManager.persist(TicketEntity.builder()
                .user(user)
                .match(match)
                .seats(seats)
                .purchaseDateTime(LocalDateTime.now())
                .totalPrice(125.0)
                .build());

        entityManager.flush();
        entityManager.clear();

        // Act
        List<TicketEntity> foundTickets = ticketRepository.findByUser(user);

        // Assert
        assertFalse(foundTickets.isEmpty());
        assertEquals(1, foundTickets.size());
        assertEquals(user.getId(), foundTickets.get(0).getUser().getId());
        assertEquals(2, foundTickets.get(0).getSeats().size());
    }

    @Test
    void findByMatch_MatchWithTickets_ReturnsTicketList() {
        // Arrange
        UserEntity user = createTestUser();
        MatchEntity match = createTestMatch("Home Team 2", "Away Team 2");

        BoxEntity box = createTestBox(match.getHomeClub().getStadium());
        BlockEntity block = createTestBlock(box);
        List<SeatEntity> seats = Arrays.asList(
                createTestSeat(block, 50.0),
                createTestSeat(block, 75.0)
        );

        entityManager.persist(TicketEntity.builder()
                .user(user)
                .match(match)
                .seats(seats)
                .purchaseDateTime(LocalDateTime.now())
                .totalPrice(125.0)
                .build());

        entityManager.flush();
        entityManager.clear();

        // Act
        List<TicketEntity> foundTickets = ticketRepository.findByMatch(match);

        // Assert
        assertFalse(foundTickets.isEmpty());
        assertEquals(1, foundTickets.size());
        assertEquals(match.getId(), foundTickets.get(0).getMatch().getId());
    }


    @Test
    void calculateTotalPrice_TicketWithMultipleSeats_ReturnsTotalSum() {
        // Arrange
        UserEntity user = createTestUser();
        MatchEntity match = createTestMatch("Home Team 3", "Away Team 3");

        BoxEntity box = createTestBox(match.getHomeClub().getStadium());
        BlockEntity block = createTestBlock(box);
        List<SeatEntity> seats = Arrays.asList(
                createTestSeat(block, 50.0),
                createTestSeat(block, 75.0)
        );

        TicketEntity ticket = entityManager.persist(TicketEntity.builder()
                .user(user)
                .match(match)
                .seats(seats)
                .purchaseDateTime(LocalDateTime.now())
                .totalPrice(125.0)
                .build());

        entityManager.flush();
        entityManager.clear();

        // Act
        Double totalPrice = ticketRepository.calculateTotalPrice(ticket.getId());

        // Assert
        assertNotNull(totalPrice);
        assertEquals(125.0, totalPrice);
    }

    @Test
    void save_ValidTicket_SavesSuccessfully() {
        // Arrange
        UserEntity user = createTestUser();
        MatchEntity match = createTestMatch("Home Team 4", "Away Team 4");

        BoxEntity box = createTestBox(match.getHomeClub().getStadium());
        BlockEntity block = createTestBlock(box);
        List<SeatEntity> seats = Arrays.asList(
                createTestSeat(block, 50.0),
                createTestSeat(block, 75.0)
        );

        TicketEntity ticket = TicketEntity.builder()
                .user(user)
                .match(match)
                .seats(seats)
                .purchaseDateTime(LocalDateTime.now())
                .totalPrice(125.0)
                .build();

        // Act
        TicketEntity savedTicket = ticketRepository.save(ticket);
        entityManager.flush();
        entityManager.clear();

        // Assert
        TicketEntity foundTicket = entityManager.find(TicketEntity.class, savedTicket.getId());
        assertNotNull(foundTicket);
        assertEquals(user.getId(), foundTicket.getUser().getId());
        assertEquals(match.getId(), foundTicket.getMatch().getId());
        assertEquals(2, foundTicket.getSeats().size());
        assertEquals(125.0, foundTicket.getTotalPrice());
    }

    @Test
    void findById_ExistingTicket_ReturnsTicket() {
        // Arrange
        UserEntity user = createTestUser();
        MatchEntity match = createTestMatch("Home Team 5", "Away Team 5");

        BoxEntity box = createTestBox(match.getHomeClub().getStadium());
        BlockEntity block = createTestBlock(box);
        List<SeatEntity> seats = Arrays.asList(
                createTestSeat(block, 50.0),
                createTestSeat(block, 75.0)
        );

        TicketEntity ticket = entityManager.persist(TicketEntity.builder()
                .user(user)
                .match(match)
                .seats(seats)
                .purchaseDateTime(LocalDateTime.now())
                .totalPrice(125.0)
                .build());

        entityManager.flush();
        entityManager.clear();

        // Act
        Optional<TicketEntity> foundTicket = ticketRepository.findById(ticket.getId());

        // Assert
        assertTrue(foundTicket.isPresent());
        assertEquals(ticket.getId(), foundTicket.get().getId());
        assertEquals(user.getId(), foundTicket.get().getUser().getId());
    }

    @Test
    void findById_NonExistingTicket_ReturnsEmpty() {
        // Act
        Optional<TicketEntity> foundTicket = ticketRepository.findById(999L);

        // Assert
        assertTrue(foundTicket.isEmpty());
    }

    @Test
    void delete_ExistingTicket_DeletesSuccessfully() {
        // Arrange
        UserEntity user = createTestUser();
        MatchEntity match = createTestMatch("Home Team 6", "Away Team 6");

        BoxEntity box = createTestBox(match.getHomeClub().getStadium());
        BlockEntity block = createTestBlock(box);
        List<SeatEntity> seats = Arrays.asList(
                createTestSeat(block, 50.0),
                createTestSeat(block, 75.0)
        );

        TicketEntity ticket = entityManager.persist(TicketEntity.builder()
                .user(user)
                .match(match)
                .seats(seats)
                .purchaseDateTime(LocalDateTime.now())
                .totalPrice(125.0)
                .build());

        entityManager.flush();
        Long ticketId = ticket.getId();

        // Act
        ticketRepository.deleteById(ticketId);
        entityManager.flush();
        entityManager.clear();

        // Assert
        TicketEntity foundTicket = entityManager.find(TicketEntity.class, ticketId);
        assertNull(foundTicket);
    }


}