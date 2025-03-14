package nl.fontys.s3.ticketmaster.business.impl.blockimpl;

import nl.fontys.s3.ticketmaster.domain.block.*;
import nl.fontys.s3.ticketmaster.domain.seat.CreateSeatRequest;
import nl.fontys.s3.ticketmaster.domain.seat.SeatResponse;
import nl.fontys.s3.ticketmaster.persitence.entity.BlockEntity;
import nl.fontys.s3.ticketmaster.persitence.entity.SeatEntity;
import nl.fontys.s3.ticketmaster.persitence.entity.BoxEntity;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class BlockConverterImplTest {

    private BlockConverterImpl blockConverter;

    @BeforeEach
    void setUp() {
        blockConverter = new BlockConverterImpl();
    }

    @Test
    void convertToEntity() {
        // Arrange
        CreateBlockRequest request = CreateBlockRequest.builder()
                .blockName("Block A")
                .boxId(1L)
                .xPosition(10)
                .yPosition(20)
                .height(5)
                .width(5)
                .seats(Arrays.asList(
                        CreateSeatRequest.builder().seatNumber(String.valueOf(1)).xPosition(10).yPosition(10).build(),
                        CreateSeatRequest.builder().seatNumber(String.valueOf(2)).xPosition(15).yPosition(10).build()
                ))
                .build();

        // Act
        BlockEntity blockEntity = blockConverter.convertToEntity(request);

        // Assert
        assertNotNull(blockEntity);
        assertEquals("Block A", blockEntity.getBlockName());
        assertEquals(1L, blockEntity.getBox().getId());
        assertEquals(10, blockEntity.getXPosition());
        assertEquals(20, blockEntity.getYPosition());
        assertEquals(5, blockEntity.getHeight());
        assertEquals(5, blockEntity.getWidth());
        assertEquals(2, blockEntity.getSeats().size());
    }

    @Test
    void convertToCreateBlockResponse() {
        // Arrange
        BlockEntity blockEntity = BlockEntity.builder()
                .id(1L)
                .blockName("Block A")
                .xPosition(10)
                .yPosition(20)
                .width(5)
                .height(5)
                .seats(List.of())
                .build();

        // Act
        CreateBlockResponse response = blockConverter.convertToCreateBlockResponse(blockEntity);

        // Assert
        assertNotNull(response);
        assertEquals(1L, response.getBlockId());
        assertEquals("Block A", response.getBlockName());
        assertEquals(10, response.getXPosition());
        assertEquals(20, response.getYPosition());
        assertEquals(5, response.getWidth());
        assertEquals(5, response.getHeight());
        assertTrue(response.getSeats().isEmpty());
    }

    @Test
    void convertToGetBlockResponse() {
        // Arrange
        BlockEntity blockEntity = BlockEntity.builder()
                .id(1L)
                .blockName("Block A")
                .box(BoxEntity.builder().id(2L).build())
                .xPosition(10)
                .yPosition(20)
                .width(5)
                .height(5)
                .seats(List.of())
                .build();

        // Act
        GetBlockResponse response = blockConverter.convertToGetBlockResponse(blockEntity);

        // Assert
        assertNotNull(response);
        assertEquals(1L, response.getBlockId());
        assertEquals("Block A", response.getBlockName());
        assertEquals(2L, response.getBoxId());
        assertEquals(10, response.getXPosition());
        assertEquals(20, response.getYPosition());
        assertEquals(5, response.getWidth());
        assertEquals(5, response.getHeight());
        assertTrue(response.getSeats().isEmpty());
    }

    @Test
    void convertToSeatResponses() {
        // Arrange
        SeatEntity seatEntity1 = SeatEntity.builder().id(1L).seatNumber("1").xPosition(10).yPosition(10).price(100.0).build();
        SeatEntity seatEntity2 = SeatEntity.builder().id(2L).seatNumber("2").xPosition(15).yPosition(10).price(150.0).build();
        List<SeatEntity> seatEntities = List.of(seatEntity1, seatEntity2);

        // Act
        List<SeatResponse> seatResponses = blockConverter.convertToSeatResponses(seatEntities);

        // Assert
        assertEquals(2, seatResponses.size());
        assertEquals(1L, seatResponses.get(0).getSeatId());
        assertEquals("1", seatResponses.get(0).getSeatNumber()); // Change expected value to String
        assertEquals(100.0, seatResponses.get(0).getPrice());
        assertEquals(2L, seatResponses.get(1).getSeatId());
        assertEquals("2", seatResponses.get(1).getSeatNumber()); // Change expected value to String
        assertEquals(150.0, seatResponses.get(1).getPrice());
    }


    @Test
    void convertToSeatResponse() {
        // Arrange
        SeatEntity seatEntity = SeatEntity.builder().id(1L).seatNumber("1").xPosition(10).yPosition(10).price(100.0).build();

        // Act
        SeatResponse seatResponse = blockConverter.convertToSeatResponse(seatEntity);

        // Assert
        assertNotNull(seatResponse);
        assertEquals(1L, seatResponse.getSeatId());
        assertEquals("1", seatResponse.getSeatNumber()); // Change expected value to String
        assertEquals(100.0, seatResponse.getPrice());
    }

    @Test
    void toBlockDTO_ValidEntity_ReturnsCorrectDTO() {
        // Arrange
        BlockEntity blockEntity = BlockEntity.builder()
                .id(1L)
                .blockName("Block A")
                .xPosition(10)
                .yPosition(20)
                .width(5)
                .height(5)
                .seats(List.of())
                .build();

        // Act
        BlockDTO blockDTO = blockConverter.toBlockDTO(blockEntity);

        // Assert
        assertNotNull(blockDTO);
        assertEquals(1L, blockDTO.getBlockId());
        assertEquals("Block A", blockDTO.getBlockName());
    }



    @Test
    void toBlockDTO_EntityWithNullValues_HandlesNullsGracefully() {
        // Arrange
        BlockEntity blockEntity = BlockEntity.builder()
                .id(null)
                .blockName(null)
                .build();

        // Act
        BlockDTO blockDTO = blockConverter.toBlockDTO(blockEntity);

        // Assert
        assertNotNull(blockDTO);
        assertNull(blockDTO.getBlockId());
        assertNull(blockDTO.getBlockName());
    }

}
