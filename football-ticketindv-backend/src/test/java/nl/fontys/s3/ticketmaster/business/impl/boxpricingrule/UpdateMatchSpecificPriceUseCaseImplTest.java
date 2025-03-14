package nl.fontys.s3.ticketmaster.business.impl.boxpricingrule;

import nl.fontys.s3.ticketmaster.business.interfaces.matchinterfaces.MatchService;
import nl.fontys.s3.ticketmaster.persitence.BoxRepository;
import nl.fontys.s3.ticketmaster.persitence.MatchRepository;
import nl.fontys.s3.ticketmaster.persitence.SeatRepository;
import nl.fontys.s3.ticketmaster.persitence.entity.BoxEntity;
import nl.fontys.s3.ticketmaster.persitence.entity.MatchEntity;
import nl.fontys.s3.ticketmaster.persitence.entity.SeatEntity;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class UpdateMatchSpecificPriceUseCaseImplTest {

    @Mock
    private MatchRepository matchRepositoryMock;

    @Mock
    private MatchService matchServiceMock;

    @Mock
    private BoxRepository boxRepositoryMock;

    @Mock
    private SeatRepository seatRepositoryMock;

    @InjectMocks
    private UpdateMatchSpecificPriceUseCaseImpl updateMatchSpecificPriceUseCase;

    @Test
    void updateMatchSpecificBoxPrice_shouldUpdatePriceSuccessfully() {
        // Arrange
        Long matchId = 1L;
        Long boxId = 2L;
        double newPrice = 100.0;

        MatchEntity matchEntity = MatchEntity.builder()
                .id(matchId)
                .matchSpecificBoxPrices(new HashMap<>())
                .build();

        BoxEntity boxEntity = BoxEntity.builder().id(boxId).build();

        when(matchServiceMock.getMatchById(matchId)).thenReturn(Optional.of(matchEntity));
        when(boxRepositoryMock.findById(boxId)).thenReturn(Optional.of(boxEntity));

        // Act
        updateMatchSpecificPriceUseCase.updateMatchSpecificBoxPrice(matchId, boxId, newPrice);

        // Assert
        verify(matchServiceMock).getMatchById(matchId);
        verify(boxRepositoryMock).findById(boxId);
        verify(matchRepositoryMock).save(matchEntity);
        assertEquals(newPrice, matchEntity.getMatchSpecificBoxPrices().get(boxEntity));
    }

    @Test
    void updateMatchSpecificSeatPrices_shouldUpdatePricesSuccessfully() {
        // Arrange
        Long matchId = 1L;
        Map<Long, Double> newSeatPrices = new HashMap<>();
        newSeatPrices.put(1L, 50.0);
        newSeatPrices.put(2L, 60.0);

        MatchEntity matchEntity = MatchEntity.builder()
                .id(matchId)
                .matchSpecificSeatPrices(new HashMap<>())
                .build();

        SeatEntity seatEntity1 = SeatEntity.builder().id(1L).build();
        SeatEntity seatEntity2 = SeatEntity.builder().id(2L).build();

        when(matchServiceMock.getMatchById(matchId)).thenReturn(Optional.of(matchEntity));
        when(seatRepositoryMock.findById(1L)).thenReturn(Optional.of(seatEntity1));
        when(seatRepositoryMock.findById(2L)).thenReturn(Optional.of(seatEntity2));

        // Act
        updateMatchSpecificPriceUseCase.updateMatchSpecificSeatPrices(matchId, newSeatPrices);

        // Assert
        verify(matchServiceMock).getMatchById(matchId);
        verify(seatRepositoryMock).findById(1L);
        verify(seatRepositoryMock).findById(2L);
        verify(matchRepositoryMock).save(matchEntity);
        assertEquals(newSeatPrices.size(), matchEntity.getMatchSpecificSeatPrices().size());
        assertEquals(newSeatPrices.get(1L), matchEntity.getMatchSpecificSeatPrices().get(seatEntity1));
        assertEquals(newSeatPrices.get(2L), matchEntity.getMatchSpecificSeatPrices().get(seatEntity2));
    }
}
