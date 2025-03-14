package nl.fontys.s3.ticketmaster.business.impl.seatimpl;

import nl.fontys.s3.ticketmaster.business.interfaces.boxinterfaces.BoxService;
import nl.fontys.s3.ticketmaster.business.interfaces.blockinterfaces.BlockService;
import nl.fontys.s3.ticketmaster.persitence.SeatRepository;
import nl.fontys.s3.ticketmaster.persitence.entity.BoxEntity;
import nl.fontys.s3.ticketmaster.persitence.entity.BlockEntity;
import nl.fontys.s3.ticketmaster.persitence.entity.SeatEntity;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class SeatServiceImplTest {

    @Mock
    private SeatRepository seatRepository;

    @Mock
    private BoxService boxService;

    @Mock
    private BlockService blockService;

    private SeatServiceImpl seatService;

    @BeforeEach
    void setUp() {
        seatService = new SeatServiceImpl(seatRepository, boxService, blockService);
    }

    @Test
    void getSeatsByStadium_shouldReturnAllSeatsInStadium() {
        // Arrange
        Long stadiumId = 1L;
        BoxEntity box1 = BoxEntity.builder().id(1L).build();
        BoxEntity box2 = BoxEntity.builder().id(2L).build();
        List<BoxEntity> boxes = Arrays.asList(box1, box2);

        BlockEntity block1 = BlockEntity.builder().id(1L).build();
        BlockEntity block2 = BlockEntity.builder().id(2L).build();
        List<BlockEntity> blocks1 = Arrays.asList(block1);
        List<BlockEntity> blocks2 = Arrays.asList(block2);

        SeatEntity seat1 = SeatEntity.builder().id(1L).build();
        SeatEntity seat2 = SeatEntity.builder().id(2L).build();
        SeatEntity seat3 = SeatEntity.builder().id(3L).build();

        block1.setSeats(Arrays.asList(seat1, seat2));
        block2.setSeats(Arrays.asList(seat3));

        when(boxService.findByStadiumId(stadiumId)).thenReturn(boxes);
        when(blockService.findByBoxId(1L)).thenReturn(blocks1);
        when(blockService.findByBoxId(2L)).thenReturn(blocks2);

        // Act
        List<SeatEntity> result = seatService.getSeatsByStadium(stadiumId);

        // Assert
        assertEquals(3, result.size());
        assertTrue(result.contains(seat1));
        assertTrue(result.contains(seat2));
        assertTrue(result.contains(seat3));
        verify(boxService).findByStadiumId(stadiumId);
        verify(blockService).findByBoxId(1L);
        verify(blockService).findByBoxId(2L);
    }

    @Test
    void saveAllSeats_shouldCallRepositorySaveAll() {
        // Arrange
        List<SeatEntity> seats = Arrays.asList(
                SeatEntity.builder().id(1L).build(),
                SeatEntity.builder().id(2L).build()
        );

        // Act
        seatService.saveAllSeats(seats);

        // Assert
        verify(seatRepository).saveAll(seats);
    }
}