package nl.fontys.s3.ticketmaster.business.impl.boximpl;

import nl.fontys.s3.ticketmaster.domain.box.*;
import nl.fontys.s3.ticketmaster.persitence.entity.BoxEntity;

import nl.fontys.s3.ticketmaster.persitence.entity.StadiumEntity;
import org.junit.jupiter.api.Test;

import java.util.Collections;

import static org.junit.jupiter.api.Assertions.*;

class BoxConverterImplTest {

    private final BoxConverterImpl boxConverter = new BoxConverterImpl();

    @Test
    void convertToEntity() {
        // Arrange
        CreateBoxRequest request = CreateBoxRequest.builder()
                .boxName("VIP Box")
                .xPosition(100)
                .yPosition(200)
                .width(300)
                .height(400)
                .stadiumId(1L)
                .blocks(Collections.emptyList()) // Empty block list
                .build();

        // Act
        BoxEntity result = boxConverter.convertToEntity(request);

        // Assert
        assertEquals("VIP Box", result.getBoxName());
        assertEquals(100, result.getXPosition());
        assertEquals(200, result.getYPosition());
        assertEquals(300, result.getWidth());
        assertEquals(400, result.getHeight());
        assertEquals(1L, result.getStadium().getId());
        assertEquals(0, result.getBlocks().size());
    }

    @Test
    void convertToCreateBoxResponse() {
        // Arrange
        BoxEntity boxEntity = BoxEntity.builder()
                .id(1L)
                .boxName("VIP Box")
                .xPosition(10)
                .yPosition(20)
                .width(100)
                .height(50)
                .build();

        // Act
        CreateBoxResponse response = boxConverter.convertToCreateBoxResponse(boxEntity);

        // Assert
        assertNotNull(response);
        assertEquals(1L, response.getBoxId());
        assertEquals("VIP Box", response.getBoxName());
        assertEquals(10, response.getXPosition());
        assertEquals(20, response.getYPosition());
        assertEquals(100, response.getWidth());
        assertEquals(50, response.getHeight());
        assertNotNull(response.getBlocks());
        assertEquals(0, response.getBlocks().size()); // Since blocks are not included in CreateBoxResponse
    }

    @Test
    void convertToGetBoxResponse() {
        // Arrange
        BoxEntity boxEntity = BoxEntity.builder()
                .id(1L)
                .boxName("VIP Box")
                .xPosition(10)
                .yPosition(20)
                .width(100)
                .height(50)
                .stadium(StadiumEntity.builder().id(2L).build())
                .price(150.0)
                .build();

        // Act
        GetBoxResponse response = boxConverter.convertToGetBoxResponse(boxEntity);

        // Assert
        assertNotNull(response);
        assertEquals(1L, response.getBoxId());
        assertEquals("VIP Box", response.getBoxName());
        assertEquals(10, response.getXPosition());
        assertEquals(20, response.getYPosition());
        assertEquals(100, response.getWidth());
        assertEquals(50, response.getHeight());
        assertEquals(2L, response.getStadiumId());
        assertEquals(150.0, response.getPrice());
    }

    @Test
    void convertToUpdateBoxPriceResponse() {
        // Arrange
        BoxEntity boxEntity = BoxEntity.builder()
                .id(1L)
                .price(200.0)
                .build();

        // Act
        UpdateBoxPriceResponse response = boxConverter.convertToUpdateBoxPriceResponse(boxEntity);

        // Assert
        assertNotNull(response);
        assertEquals(1L, response.getBoxId());
        assertEquals(200.0, response.getUpdatedPrice());
    }
}
