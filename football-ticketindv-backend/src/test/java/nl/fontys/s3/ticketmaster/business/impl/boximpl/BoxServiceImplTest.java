package nl.fontys.s3.ticketmaster.business.impl.boximpl;

import nl.fontys.s3.ticketmaster.persitence.BoxRepository;
import nl.fontys.s3.ticketmaster.persitence.entity.BoxEntity;
import nl.fontys.s3.ticketmaster.persitence.entity.BlockEntity;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

class BoxServiceImplTest {

    @Mock
    private BoxRepository boxRepository;

    @InjectMocks
    private BoxServiceImpl boxService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void save() {
        // Arrange
        BoxEntity box = BoxEntity.builder().id(1L).boxName("VIP Box").build();
        when(boxRepository.save(any(BoxEntity.class))).thenReturn(box);

        // Act
        BoxEntity result = boxService.save(box);

        // Assert
        assertEquals("VIP Box", result.getBoxName());
        verify(boxRepository, times(1)).save(box);
    }

    @Test
    void updatePrice() {
        // Arrange
        Long boxId = 1L;
        Double newPrice = 100.0;
        BoxEntity box = BoxEntity.builder().id(boxId).price(newPrice).build();
        when(boxRepository.updatePriceWithSeats(boxId, newPrice)).thenReturn(box);

        // Act
        BoxEntity result = boxService.updatePrice(boxId, newPrice);

        // Assert
        assertEquals(newPrice, result.getPrice());
        verify(boxRepository, times(1)).updatePriceWithSeats(boxId, newPrice);
    }

    @Test
    void findById() {
        // Arrange
        Long boxId = 1L;
        BoxEntity box = BoxEntity.builder().id(boxId).build();
        when(boxRepository.findById(boxId)).thenReturn(Optional.of(box));

        // Act
        Optional<BoxEntity> result = boxService.findById(boxId);

        // Assert
        assertTrue(result.isPresent());
        assertEquals(boxId, result.get().getId());
        verify(boxRepository, times(1)).findById(boxId);
    }

    @Test
    void addBlockToBox() {
        // Arrange
        Long boxId = 1L;
        BlockEntity block = BlockEntity.builder().blockName("Block A").build();
        BoxEntity box = BoxEntity.builder().id(boxId).blocks(Collections.singletonList(block)).build();
        when(boxRepository.addBlockToBox(boxId, block)).thenReturn(box);

        // Act
        BoxEntity result = boxService.addBlockToBox(boxId, block);

        // Assert
        assertEquals(1, result.getBlocks().size());
        assertEquals("Block A", result.getBlocks().get(0).getBlockName());
        verify(boxRepository, times(1)).addBlockToBox(boxId, block);
    }

    @Test
    void findByStadiumId() {
        // Arrange
        Long stadiumId = 1L;
        BoxEntity box = BoxEntity.builder().id(1L).build();
        when(boxRepository.findByStadiumId(stadiumId)).thenReturn(Collections.singletonList(box));

        // Act
        List<BoxEntity> result = boxService.findByStadiumId(stadiumId);

        // Assert
        assertEquals(1, result.size());
        assertEquals(1L, result.get(0).getId());
        verify(boxRepository, times(1)).findByStadiumId(stadiumId);
    }

    @Test
    void existsByBoxNameAndStadiumId() {
        // Arrange
        String boxName = "VIP Box";
        Long stadiumId = 1L;
        when(boxRepository.existsByBoxNameAndStadiumId(boxName, stadiumId)).thenReturn(true);

        // Act
        boolean exists = boxService.existsByBoxNameAndStadiumId(boxName, stadiumId);

        // Assert
        assertTrue(exists);
        verify(boxRepository, times(1)).existsByBoxNameAndStadiumId(boxName, stadiumId);
    }

    @Test
    void deleteById() {
        // Arrange
        Long boxId = 1L;

        // Act
        boxService.deleteById(boxId);

        // Assert
        verify(boxRepository, times(1)).deleteById(boxId);
    }

    @Test
    void existsById() {
        // Arrange
        Long boxId = 1L;
        when(boxRepository.existsById(boxId)).thenReturn(true);

        // Act
        boolean exists = boxService.existsById(boxId);

        // Assert
        assertTrue(exists);
        verify(boxRepository, times(1)).existsById(boxId);
    }
}
