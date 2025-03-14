package nl.fontys.s3.ticketmaster.business.impl.blockimpl;

import nl.fontys.s3.ticketmaster.business.interfaces.blockinterfaces.BlockConverter;
import nl.fontys.s3.ticketmaster.domain.block.GetBlockResponse;
import nl.fontys.s3.ticketmaster.persitence.BlockRepository;
import nl.fontys.s3.ticketmaster.persitence.entity.BlockEntity;
import nl.fontys.s3.ticketmaster.persitence.entity.BoxEntity;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class GetBlocksByStadiumUseCaseImplTest {

    @Mock
    private BlockRepository blockRepositoryMock;

    @Mock
    private BlockConverter blockConverterMock;

    @InjectMocks
    private GetBlocksByStadiumUseCaseImpl getBlocksByStadiumUseCase;

    @Test
    void getBlocksByStadium_shouldReturnBlocksSuccessfully() {
        // Arrange
        Long stadiumId = 1L;

        BoxEntity boxEntity = BoxEntity.builder()
                .id(stadiumId)
                .build();

        BlockEntity block1 = BlockEntity.builder()
                .id(1L)
                .blockName("Block A")
                .box(boxEntity) // Set the BoxEntity directly
                .build();

        BlockEntity block2 = BlockEntity.builder()
                .id(2L)
                .blockName("Block B")
                .box(boxEntity) // Set the BoxEntity directly
                .build();

        List<BlockEntity> blockEntities = Arrays.asList(block1, block2);

        GetBlockResponse blockResponse1 = GetBlockResponse.builder()
                .blockId(1L)
                .blockName("Block A")
                .boxId(stadiumId) // Make sure this matches how your GetBlockResponse is structured
                .build();

        GetBlockResponse blockResponse2 = GetBlockResponse.builder()
                .blockId(2L)
                .blockName("Block B")
                .boxId(stadiumId) // Make sure this matches how your GetBlockResponse is structured
                .build();

        when(blockRepositoryMock.findByBoxId(stadiumId)).thenReturn(blockEntities);
        when(blockConverterMock.convertToGetBlockResponse(block1)).thenReturn(blockResponse1);
        when(blockConverterMock.convertToGetBlockResponse(block2)).thenReturn(blockResponse2);

        // Act
        List<GetBlockResponse> actualResponse = getBlocksByStadiumUseCase.getBlocksByStadium(stadiumId);

        // Assert
        assertEquals(2, actualResponse.size());
        assertEquals(blockResponse1, actualResponse.get(0));
        assertEquals(blockResponse2, actualResponse.get(1));

        verify(blockRepositoryMock, times(1)).findByBoxId(stadiumId);
        verify(blockConverterMock, times(2)).convertToGetBlockResponse(any(BlockEntity.class));
    }

    @Test
    void getBlocksByStadium_shouldReturnEmptyListWhenNoBlocksFound() {
        // Arrange
        Long stadiumId = 1L;

        when(blockRepositoryMock.findByBoxId(stadiumId)).thenReturn(List.of());

        // Act
        List<GetBlockResponse> actualResponse = getBlocksByStadiumUseCase.getBlocksByStadium(stadiumId);

        // Assert
        assertTrue(actualResponse.isEmpty());
        verify(blockRepositoryMock, times(1)).findByBoxId(stadiumId);
        verify(blockConverterMock, never()).convertToGetBlockResponse(any(BlockEntity.class));
    }
}
