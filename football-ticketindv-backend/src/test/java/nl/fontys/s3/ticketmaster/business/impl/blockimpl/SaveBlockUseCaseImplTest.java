package nl.fontys.s3.ticketmaster.business.impl.blockimpl;

import nl.fontys.s3.ticketmaster.business.interfaces.blockinterfaces.BlockConverter;
import nl.fontys.s3.ticketmaster.business.interfaces.blockinterfaces.BlockValidator;
import nl.fontys.s3.ticketmaster.business.interfaces.boxinterfaces.BoxService;
import nl.fontys.s3.ticketmaster.domain.block.CreateBlockRequest;
import nl.fontys.s3.ticketmaster.domain.block.CreateBlockResponse;
import nl.fontys.s3.ticketmaster.domain.seat.CreateSeatRequest;  // Import your CreateSeatRequest
import nl.fontys.s3.ticketmaster.persitence.BlockRepository;
import nl.fontys.s3.ticketmaster.persitence.entity.BlockEntity;
import nl.fontys.s3.ticketmaster.persitence.entity.BoxEntity;
import nl.fontys.s3.ticketmaster.persitence.entity.SeatEntity;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class SaveBlockUseCaseImplTest {

    @Mock
    private BlockRepository blockRepositoryMock;

    @Mock
    private BlockConverter blockConverterMock;

    @Mock
    private BlockValidator blockValidatorMock;

    @Mock
    private BoxService boxServiceMock;

    @InjectMocks
    private SaveBlockUseCaseImpl saveBlockUseCase;

    @Test
    void saveBlock_shouldSaveBlockSuccessfully() {
        // Arrange
        List<CreateSeatRequest> seatRequests = Arrays.asList(
                CreateSeatRequest.builder().seatNumber("1").build(),
                CreateSeatRequest.builder().seatNumber("2").build()
        );

        CreateBlockRequest request = CreateBlockRequest.builder()
                .blockName("Block A")
                .boxId(1L)
                .seats(seatRequests)  // Use List<CreateSeatRequest> here
                .build();

        BlockEntity block = BlockEntity.builder()
                .id(1L)
                .blockName("Block A")
                .seats(Arrays.asList(
                        SeatEntity.builder().id(1L).build(),
                        SeatEntity.builder().id(2L).build()
                ))
                .build();

        BlockEntity savedBlock = BlockEntity.builder()
                .id(1L)
                .blockName("Block A")
                .seats(Arrays.asList(
                        SeatEntity.builder().id(1L).seatNumber("1").build(),
                        SeatEntity.builder().id(2L).seatNumber("2").build()
                ))
                .build();

        BoxEntity updatedBox = BoxEntity.builder().id(1L).build();

        CreateBlockResponse expectedResponse = CreateBlockResponse.builder()
                .blockId(1L)
                .blockName("Block A")
                .build();

        when(blockConverterMock.convertToEntity(request)).thenReturn(block);
        when(blockRepositoryMock.save(any(BlockEntity.class))).thenReturn(savedBlock);
        when(boxServiceMock.addBlockToBox(1L, savedBlock)).thenReturn(updatedBox);
        when(blockConverterMock.convertToCreateBlockResponse(any(BlockEntity.class))).thenReturn(expectedResponse);

        // Act
        CreateBlockResponse actualResponse = saveBlockUseCase.saveBlock(request);

        // Assert
        assertNotNull(actualResponse);
        assertEquals(expectedResponse, actualResponse);

        verify(blockValidatorMock).validateCreateBlockRequest(request);
        verify(blockConverterMock).convertToEntity(request);
        verify(blockRepositoryMock).save(block);  // Check saving block entity
        verify(boxServiceMock).addBlockToBox(1L, savedBlock);
        verify(blockConverterMock).convertToCreateBlockResponse(savedBlock);
    }
}
