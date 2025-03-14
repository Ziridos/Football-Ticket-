package nl.fontys.s3.ticketmaster.business.impl.blockimpl;

import nl.fontys.s3.ticketmaster.persitence.BlockRepository;
import nl.fontys.s3.ticketmaster.persitence.entity.BlockEntity;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

class BlockServiceImplTest {

    @Mock
    private BlockRepository blockRepository;

    @InjectMocks
    private BlockServiceImpl blockService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void save() {
        // Arrange
        BlockEntity blockEntity = new BlockEntity();
        when(blockRepository.save(any(BlockEntity.class))).thenReturn(blockEntity);

        // Act
        BlockEntity result = blockService.save(blockEntity);

        // Assert
        assertNotNull(result);
        verify(blockRepository).save(blockEntity);
    }

    @Test
    void findById() {
        // Arrange
        Long blockId = 1L;
        BlockEntity blockEntity = new BlockEntity();
        when(blockRepository.findById(blockId)).thenReturn(Optional.of(blockEntity));

        // Act
        Optional<BlockEntity> result = blockService.findById(blockId);

        // Assert
        assertTrue(result.isPresent());
        assertEquals(blockEntity, result.get());
        verify(blockRepository).findById(blockId);
    }

    @Test
    void findByBoxId() {
        // Arrange
        Long boxId = 1L;
        BlockEntity blockEntity1 = new BlockEntity();
        BlockEntity blockEntity2 = new BlockEntity();
        List<BlockEntity> blockEntities = Arrays.asList(blockEntity1, blockEntity2);
        when(blockRepository.findByBoxId(boxId)).thenReturn(blockEntities);

        // Act
        List<BlockEntity> result = blockService.findByBoxId(boxId);

        // Assert
        assertEquals(2, result.size());
        verify(blockRepository).findByBoxId(boxId);
    }

    @Test
    void existsByBlockNameAndBoxId() {
        // Arrange
        String blockName = "Block A";
        Long boxId = 1L;
        when(blockRepository.existsByBlockNameAndBoxId(blockName, boxId)).thenReturn(true);

        // Act
        boolean exists = blockService.existsByBlockNameAndBoxId(blockName, boxId);

        // Assert
        assertTrue(exists);
        verify(blockRepository).existsByBlockNameAndBoxId(blockName, boxId);
    }

    @Test
    void deleteById() {
        // Arrange
        Long blockId = 1L;

        // Act
        blockService.deleteById(blockId);

        // Assert
        verify(blockRepository).deleteById(blockId);
    }

    @Test
    void existsById() {
        // Arrange
        Long id = 1L;
        when(blockRepository.existsById(id)).thenReturn(true);

        // Act
        boolean exists = blockService.existsById(id);

        // Assert
        assertTrue(exists);
        verify(blockRepository).existsById(id);
    }
}
