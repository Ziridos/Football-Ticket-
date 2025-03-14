package nl.fontys.s3.ticketmaster.persitence;

import nl.fontys.s3.ticketmaster.persitence.entity.*;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.ANY)
class SeatRepositoryTest {
    @Autowired
    private TestEntityManager entityManager;

    @Autowired
    private SeatRepository seatRepository;

    private StadiumEntity createTestStadium(String name) {
        return entityManager.persist(StadiumEntity.builder()
                .stadiumName(name)
                .stadiumAddress("Test Address")
                .stadiumPostalCode("1234AB")
                .stadiumCity("Test City")
                .stadiumCountry("Test Country")
                .build());
    }

    private BoxEntity createTestBox(StadiumEntity stadium, String name) {
        return entityManager.persist(BoxEntity.builder()
                .boxName(name)
                .xPosition(0)
                .yPosition(0)
                .width(10)
                .height(10)
                .stadium(stadium)
                .price(100.0)
                .build());
    }

    private BlockEntity createTestBlock(BoxEntity box, String name) {
        return entityManager.persist(BlockEntity.builder()
                .blockName(name)
                .xPosition(0)
                .yPosition(0)
                .width(5)
                .height(5)
                .box(box)
                .build());
    }

    private SeatEntity createTestSeat(BlockEntity block, String seatNumber, double price) {
        return entityManager.persist(SeatEntity.builder()
                .seatNumber(seatNumber)
                .xPosition(0)
                .yPosition(0)
                .block(block)
                .price(price)
                .build());
    }

    @Test
    void getSeatsByStadium_ExistingStadium_ReturnsSeats() {
        // Arrange
        StadiumEntity stadium = createTestStadium("Test Stadium 1");
        BoxEntity box = createTestBox(stadium, "Box 1");
        BlockEntity block = createTestBlock(box, "Block 1");
        // Create seats directly without storing in variables
        createTestSeat(block, "A1", 50.0);
        createTestSeat(block, "A2", 50.0);

        entityManager.flush();
        entityManager.clear();

        // Act
        List<SeatEntity> seats = seatRepository.getSeatsByStadium(stadium.getId());

        // Assert
        assertEquals(2, seats.size());
        assertTrue(seats.stream()
                .map(SeatEntity::getSeatNumber)
                .allMatch(number -> Arrays.asList("A1", "A2").contains(number)));
    }

    @Test
    void getSeatsByStadium_StadiumWithNoSeats_ReturnsEmptyList() {
        // Arrange
        StadiumEntity stadium = createTestStadium("Test Stadium 2");
        entityManager.flush();
        entityManager.clear();

        // Act
        List<SeatEntity> seats = seatRepository.getSeatsByStadium(stadium.getId());

        // Assert
        assertTrue(seats.isEmpty());
    }

    @Test
    void getSeatsByStadium_NonExistentStadium_ReturnsEmptyList() {
        // Act
        List<SeatEntity> seats = seatRepository.getSeatsByStadium(999L);

        // Assert
        assertTrue(seats.isEmpty());
    }

    @Test
    void save_ValidSeat_SavesSuccessfully() {
        // Arrange
        StadiumEntity stadium = createTestStadium("Test Stadium 3");
        BoxEntity box = createTestBox(stadium, "Box 2");
        BlockEntity block = createTestBlock(box, "Block 2");
        SeatEntity seat = SeatEntity.builder()
                .seatNumber("B1")
                .xPosition(0)
                .yPosition(0)
                .block(block)
                .price(75.0)
                .build();

        // Act
        SeatEntity savedSeat = seatRepository.save(seat);
        entityManager.flush();

        // Assert
        SeatEntity foundSeat = entityManager.find(SeatEntity.class, savedSeat.getId());
        assertNotNull(foundSeat);
        assertEquals("B1", foundSeat.getSeatNumber());
        assertEquals(75.0, foundSeat.getPrice());
        assertEquals(block.getId(), foundSeat.getBlock().getId());
    }

    @Test
    void findById_ExistingSeat_ReturnsSeat() {
        // Arrange
        StadiumEntity stadium = createTestStadium("Test Stadium 4");
        BoxEntity box = createTestBox(stadium, "Box 3");
        BlockEntity block = createTestBlock(box, "Block 3");
        SeatEntity seat = createTestSeat(block, "C1", 60.0);
        entityManager.flush();
        entityManager.clear();

        // Act
        Optional<SeatEntity> foundSeat = seatRepository.findById(seat.getId());

        // Assert
        assertTrue(foundSeat.isPresent());
        assertEquals("C1", foundSeat.get().getSeatNumber());
        assertEquals(60.0, foundSeat.get().getPrice());
    }

    @Test
    void findById_NonExistingSeat_ReturnsEmpty() {
        // Act
        Optional<SeatEntity> foundSeat = seatRepository.findById(999L);

        // Assert
        assertTrue(foundSeat.isEmpty());
    }

    @Test
    void delete_ExistingSeat_DeletesSuccessfully() {
        // Arrange
        StadiumEntity stadium = createTestStadium("Test Stadium 5");
        BoxEntity box = createTestBox(stadium, "Box 4");
        BlockEntity block = createTestBlock(box, "Block 4");
        SeatEntity seat = createTestSeat(block, "D1", 80.0);
        entityManager.flush();
        Long seatId = seat.getId();

        // Act
        seatRepository.deleteById(seatId);
        entityManager.flush();

        // Assert
        SeatEntity foundSeat = entityManager.find(SeatEntity.class, seatId);
        assertNull(foundSeat);
    }

    @Test
    void findAll_MultipleSeats_ReturnsAllSeats() {
        // Arrange
        StadiumEntity stadium = createTestStadium("Test Stadium 6");
        BoxEntity box = createTestBox(stadium, "Box 5");
        BlockEntity block = createTestBlock(box, "Block 5");
        createTestSeat(block, "E1", 90.0);
        createTestSeat(block, "E2", 90.0);
        entityManager.flush();
        entityManager.clear();

        // Act
        List<SeatEntity> seats = seatRepository.findAll();

        // Assert
        assertEquals(2, seats.size());
        assertTrue(seats.stream()
                .map(SeatEntity::getSeatNumber)
                .allMatch(number -> Arrays.asList("E1", "E2").contains(number)));
    }

    @Test
    void update_ExistingSeat_UpdatesSuccessfully() {
        // Arrange
        StadiumEntity stadium = createTestStadium("Test Stadium 7");
        BoxEntity box = createTestBox(stadium, "Box 6");
        BlockEntity block = createTestBlock(box, "Block 6");
        SeatEntity seat = createTestSeat(block, "F1", 100.0);
        entityManager.flush();

        // Act
        seat.setSeatNumber("F2");
        seat.setPrice(120.0);
        seatRepository.save(seat);
        entityManager.flush();

        // Assert
        SeatEntity foundSeat = entityManager.find(SeatEntity.class, seat.getId());
        assertEquals("F2", foundSeat.getSeatNumber());
        assertEquals(120.0, foundSeat.getPrice());
    }
}