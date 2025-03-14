package nl.fontys.s3.ticketmaster.business.impl.boximpl;

import nl.fontys.s3.ticketmaster.business.interfaces.boxinterfaces.BoxConverter;
import nl.fontys.s3.ticketmaster.domain.box.GetBoxResponse;
import nl.fontys.s3.ticketmaster.persitence.BoxRepository;
import nl.fontys.s3.ticketmaster.persitence.entity.BoxEntity;
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
class GetBoxesByStadiumUseCaseImplTest {

    @Mock
    private BoxRepository boxRepository;

    @Mock
    private BoxConverter boxConverter;

    private GetBoxesByStadiumUseCaseImpl getBoxesByStadiumUseCase;

    @BeforeEach
    void setUp() {
        getBoxesByStadiumUseCase = new GetBoxesByStadiumUseCaseImpl(boxRepository, boxConverter);
    }

    @Test
    void getBoxesByStadium_shouldReturnListOfBoxResponses() {
        // Arrange
        Long stadiumId = 1L;
        BoxEntity box1 = BoxEntity.builder().id(1L).build();
        BoxEntity box2 = BoxEntity.builder().id(2L).build();
        List<BoxEntity> boxEntities = Arrays.asList(box1, box2);

        GetBoxResponse response1 = GetBoxResponse.builder().boxId(1L).build();
        GetBoxResponse response2 = GetBoxResponse.builder().boxId(2L).build();

        when(boxRepository.findByStadiumId(stadiumId)).thenReturn(boxEntities);
        when(boxConverter.convertToGetBoxResponse(box1)).thenReturn(response1);
        when(boxConverter.convertToGetBoxResponse(box2)).thenReturn(response2);

        // Act
        List<GetBoxResponse> result = getBoxesByStadiumUseCase.getBoxesByStadium(stadiumId);

        // Assert
        assertEquals(2, result.size());
        assertEquals(response1, result.get(0));
        assertEquals(response2, result.get(1));
        verify(boxRepository).findByStadiumId(stadiumId);
        verify(boxConverter, times(2)).convertToGetBoxResponse(any(BoxEntity.class));
    }
}