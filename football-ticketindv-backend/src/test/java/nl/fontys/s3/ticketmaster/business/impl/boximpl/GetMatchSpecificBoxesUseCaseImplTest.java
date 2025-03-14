package nl.fontys.s3.ticketmaster.business.impl.boximpl;

import nl.fontys.s3.ticketmaster.business.interfaces.boxinterfaces.BoxConverter;
import nl.fontys.s3.ticketmaster.business.interfaces.matchinterfaces.MatchService;
import nl.fontys.s3.ticketmaster.domain.box.GetBoxResponse;
import nl.fontys.s3.ticketmaster.persitence.BoxRepository;
import nl.fontys.s3.ticketmaster.persitence.entity.BoxEntity;
import nl.fontys.s3.ticketmaster.persitence.entity.MatchEntity;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

class GetMatchSpecificBoxesUseCaseImplTest {

    @Mock
    private BoxRepository boxRepository;

    @Mock
    private BoxConverter boxConverter;

    @Mock
    private MatchService matchService;

    @InjectMocks
    private GetMatchSpecificBoxesUseCaseImpl getMatchSpecificBoxesUseCase;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void getBoxesByStadiumForMatch_success() {
        // Arrange
        Long stadiumId = 1L;
        Long matchId = 1L;

        BoxEntity box1 = new BoxEntity();
        box1.setId(1L);
        box1.setPrice(100.0);

        BoxEntity box2 = new BoxEntity();
        box2.setId(2L);
        box2.setPrice(200.0);

        List<BoxEntity> boxes = Arrays.asList(box1, box2);
        Map<BoxEntity, Double> specificBoxPrices = new HashMap<>();
        specificBoxPrices.put(box1, 150.0); // Custom price for box1

        MatchEntity matchEntity = new MatchEntity();
        matchEntity.setMatchSpecificBoxPrices(specificBoxPrices);

        when(boxRepository.findByStadiumId(stadiumId)).thenReturn(boxes);
        when(matchService.getMatchById(matchId)).thenReturn(Optional.of(matchEntity));

        // Adjusted instantiation using builder
        when(boxConverter.convertToGetBoxResponse(box1)).thenReturn(GetBoxResponse.builder()
                .boxId(box1.getId())
                .boxName("Box 1")
                .xPosition(0) // Use actual values
                .yPosition(0) // Use actual values
                .width(10) // Use actual values
                .height(5) // Use actual values
                .stadiumId(stadiumId)
                .price(box1.getPrice())
                .build());

        when(boxConverter.convertToGetBoxResponse(box2)).thenReturn(GetBoxResponse.builder()
                .boxId(box2.getId())
                .boxName("Box 2")
                .xPosition(1) // Use actual values
                .yPosition(1) // Use actual values
                .width(10) // Use actual values
                .height(5) // Use actual values
                .stadiumId(stadiumId)
                .price(box2.getPrice())
                .build());

        // Act
        List<GetBoxResponse> responses = getMatchSpecificBoxesUseCase.getBoxesByStadiumForMatch(stadiumId, matchId);

        // Assert
        assertEquals(2, responses.size());
        assertEquals(150.0, responses.get(0).getPrice()); // box1 price
        assertEquals(200.0, responses.get(1).getPrice()); // box2 default price
    }


    @Test
    void getBoxesByStadiumForMatch_matchNotFound() {
        // Arrange
        Long stadiumId = 1L;
        Long matchId = 999L; // Non-existent match

        when(boxRepository.findByStadiumId(stadiumId)).thenReturn(Arrays.asList());
        when(matchService.getMatchById(matchId)).thenReturn(Optional.empty());

        // Act & Assert
        IllegalArgumentException thrown = assertThrows(IllegalArgumentException.class,
                () -> getMatchSpecificBoxesUseCase.getBoxesByStadiumForMatch(stadiumId, matchId));
        assertEquals("Match not found for id: " + matchId, thrown.getMessage());
    }
}
