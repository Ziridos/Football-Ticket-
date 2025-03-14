package nl.fontys.s3.ticketmaster.persitence;

import nl.fontys.s3.ticketmaster.persitence.entity.BlockEntity;
import nl.fontys.s3.ticketmaster.persitence.entity.BoxEntity;
import nl.fontys.s3.ticketmaster.persitence.entity.StadiumEntity;
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
class BlockRepositoryTest {
    @Autowired
    private TestEntityManager entityManager;

    @Autowired
    private BlockRepository blockRepository;

    private StadiumEntity stadium;
    private BoxEntity box;
    private BlockEntity block1;
    private BlockEntity block2;

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

        // Create and persist blocks
        block1 = BlockEntity.builder()
                .blockName("Test Block 1")
                .box(box)
                .xPosition(0)
                .yPosition(0)
                .width(5)
                .height(5)
                .seats(new ArrayList<>())
                .build();
        box.getBlocks().add(block1);
        entityManager.persist(block1);

        block2 = BlockEntity.builder()
                .blockName("Test Block 2")
                .box(box)
                .xPosition(5)
                .yPosition(0)
                .width(5)
                .height(5)
                .seats(new ArrayList<>())
                .build();
        box.getBlocks().add(block2);
        entityManager.persist(block2);

        entityManager.flush();
        entityManager.clear();
    }

    @Test
    void findByBoxId_ExistingBox_ReturnsBlocks() {
        // Act
        List<BlockEntity> foundBlocks = blockRepository.findByBoxId(box.getId());

        // Assert
        assertNotNull(foundBlocks);
        assertEquals(2, foundBlocks.size());
        assertTrue(foundBlocks.stream().anyMatch(block -> block.getBlockName().equals("Test Block 1")));
        assertTrue(foundBlocks.stream().anyMatch(block -> block.getBlockName().equals("Test Block 2")));
    }

    @Test
    void findByBoxId_NonExistingBox_ReturnsEmptyList() {
        // Act
        List<BlockEntity> foundBlocks = blockRepository.findByBoxId(999L);

        // Assert
        assertNotNull(foundBlocks);
        assertTrue(foundBlocks.isEmpty());
    }

    @Test
    void existsByBlockNameAndBoxId_ExistingBlockAndBox_ReturnsTrue() {
        // Act
        boolean exists = blockRepository.existsByBlockNameAndBoxId("Test Block 1", box.getId());

        // Assert
        assertTrue(exists);
    }

    @Test
    void existsByBlockNameAndBoxId_NonExistingBlock_ReturnsFalse() {
        // Act
        boolean exists = blockRepository.existsByBlockNameAndBoxId("Non Existing Block", box.getId());

        // Assert
        assertFalse(exists);
    }

    @Test
    void existsByBlockNameAndBoxId_NonExistingBox_ReturnsFalse() {
        // Act
        boolean exists = blockRepository.existsByBlockNameAndBoxId("Test Block 1", 999L);

        // Assert
        assertFalse(exists);
    }

    @Test
    void save_ValidBlock_SavesSuccessfully() {
        // Arrange
        BlockEntity newBlock = BlockEntity.builder()
                .blockName("New Test Block")
                .box(box)
                .xPosition(0)
                .yPosition(5)
                .width(5)
                .height(5)
                .seats(new ArrayList<>())
                .build();

        // Act
        BlockEntity savedBlock = blockRepository.save(newBlock);
        entityManager.flush();

        // Assert
        BlockEntity foundBlock = entityManager.find(BlockEntity.class, savedBlock.getId());
        assertNotNull(foundBlock);
        assertEquals("New Test Block", foundBlock.getBlockName());
        assertEquals(box.getId(), foundBlock.getBox().getId());
    }

    @Test
    void findById_ExistingBlock_ReturnsBlock() {
        // Act
        Optional<BlockEntity> foundBlock = blockRepository.findById(block1.getId());

        // Assert
        assertTrue(foundBlock.isPresent());
        assertEquals("Test Block 1", foundBlock.get().getBlockName());
        assertEquals(box.getId(), foundBlock.get().getBox().getId());
    }

    @Test
    void findById_NonExistingBlock_ReturnsEmpty() {
        // Act
        Optional<BlockEntity> foundBlock = blockRepository.findById(999L);

        // Assert
        assertFalse(foundBlock.isPresent());
    }


}