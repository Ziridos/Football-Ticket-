package nl.fontys.s3.ticketmaster.persitence;

import nl.fontys.s3.ticketmaster.persitence.entity.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
class BoxRepositoryTest {
    @Autowired
    private TestEntityManager entityManager;

    @Autowired
    private BoxRepository boxRepository;

    private StadiumEntity stadium;
    private BoxEntity box;
    private BlockEntity block;
    private SeatEntity seat;

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

        // Create and persist box
        box = BoxEntity.builder()
                .boxName("Test Box")
                .stadium(stadium)
                .price(100.0)
                .xPosition(0)
                .yPosition(0)
                .width(10)
                .height(10)
                .blocks(new ArrayList<>())
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
                .seats(new ArrayList<>())
                .build();
        box.getBlocks().add(block);
        entityManager.persist(block);

        // Create and persist seat
        seat = SeatEntity.builder()
                .seatNumber("A1")
                .block(block)
                .price(50.0)
                .xPosition(0)
                .yPosition(0)
                .build();
        block.getSeats().add(seat);
        entityManager.persist(seat);

        entityManager.flush();
        entityManager.clear();
    }

    @Test
    void findByStadiumId_ExistingStadium_ReturnsBoxes() {
        // Act
        List<BoxEntity> boxes = boxRepository.findByStadiumId(stadium.getId());

        // Assert
        assertFalse(boxes.isEmpty());
        assertEquals(1, boxes.size());
        assertEquals("Test Box", boxes.get(0).getBoxName());
    }

    @Test
    void findByStadiumId_NonExistingStadium_ReturnsEmptyList() {
        // Act
        List<BoxEntity> boxes = boxRepository.findByStadiumId(999L);

        // Assert
        assertTrue(boxes.isEmpty());
    }

    @Test
    void existsByBoxNameAndStadiumId_ExistingBox_ReturnsTrue() {
        // Act
        boolean exists = boxRepository.existsByBoxNameAndStadiumId("Test Box", stadium.getId());

        // Assert
        assertTrue(exists);
    }

    @Test
    void existsByBoxNameAndStadiumId_NonExistingBox_ReturnsFalse() {
        // Act
        boolean exists = boxRepository.existsByBoxNameAndStadiumId("Non Existing Box", stadium.getId());

        // Assert
        assertFalse(exists);
    }

    @Test
    void updatePrice_ValidBox_UpdatesSuccessfully() {
        // Act
        boxRepository.updatePrice(box.getId(), 150.0);
        entityManager.flush();
        entityManager.clear();

        // Assert
        BoxEntity updatedBox = entityManager.find(BoxEntity.class, box.getId());
        assertEquals(150.0, updatedBox.getPrice());
    }

    @Test
    void findByIdWithBlocks_ExistingBox_ReturnsBoxWithBlocks() {
        // Act
        Optional<BoxEntity> foundBox = boxRepository.findByIdWithBlocks(box.getId());

        // Assert
        assertTrue(foundBox.isPresent());
        assertFalse(foundBox.get().getBlocks().isEmpty());
        assertEquals("Test Block", foundBox.get().getBlocks().get(0).getBlockName());
    }

    @Test
    void findByIdWithBlocks_NonExistingBox_ReturnsEmpty() {
        // Act
        Optional<BoxEntity> foundBox = boxRepository.findByIdWithBlocks(999L);

        // Assert
        assertFalse(foundBox.isPresent());
    }



    @Test
    void addBlockToBox_ValidBox_AddsBlockSuccessfully() {
        // Arrange
        BlockEntity newBlock = BlockEntity.builder()
                .blockName("New Test Block")
                .box(box)
                .xPosition(5)
                .yPosition(5)
                .width(5)
                .height(5)
                .seats(new ArrayList<>())
                .build();

        // Act
        boxRepository.addBlockToBox(box.getId(), newBlock);
        entityManager.flush();
        entityManager.clear();

        // Assert
        BoxEntity foundBox = entityManager.find(BoxEntity.class, box.getId());
        assertEquals(2, foundBox.getBlocks().size());
        assertTrue(foundBox.getBlocks().stream()
                .anyMatch(b -> b.getBlockName().equals("New Test Block")));
    }

    @Test
    void addBlockToBox_NonExistingBox_ReturnsNull() {
        // Arrange
        BlockEntity newBlock = BlockEntity.builder()
                .blockName("New Test Block")
                .xPosition(5)
                .yPosition(5)
                .width(5)
                .height(5)
                .seats(new ArrayList<>())
                .build();

        // Act
        BoxEntity result = boxRepository.addBlockToBox(999L, newBlock);

        // Assert
        assertNull(result);
    }
}