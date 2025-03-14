package nl.fontys.s3.ticketmaster.business.impl.boxpricingrule;

import nl.fontys.s3.ticketmaster.business.interfaces.boxinterfaces.BoxService;
import nl.fontys.s3.ticketmaster.business.interfaces.matchinterfaces.MatchService;
import nl.fontys.s3.ticketmaster.business.interfaces.matchinterfaces.UpdateMatchSpecificPriceUseCase;
import nl.fontys.s3.ticketmaster.persitence.BoxPricingRuleRepository;
import nl.fontys.s3.ticketmaster.persitence.entity.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ApplyPricingRulesUseCaseImplTest {

    @Mock
    private BoxPricingRuleRepository boxPricingRuleRepository;

    @Mock
    private UpdateMatchSpecificPriceUseCase updateMatchSpecificPriceUseCase;

    @Mock
    private MatchService matchService;

    @Mock
    private BoxService boxService;

    @InjectMocks
    private ApplyPricingRulesUseCaseImpl applyPricingRulesUseCase;

    @Captor
    private ArgumentCaptor<Map<Long, Double>> seatPricesCaptor;

    private MatchEntity match;
    private StadiumEntity stadium;
    private ClubEntity homeClub;
    private BoxEntity box;
    private List<BoxPricingRuleEntity> pricingRules;
    private List<SeatEntity> seats;
    private Map<SeatEntity, Boolean> availableSeats;

    @BeforeEach
    void setUp() {
        // Create stadium
        stadium = StadiumEntity.builder()
                .id(1L)
                .stadiumName("Test Stadium")
                .build();

        // Create home club
        homeClub = ClubEntity.builder()
                .id(1L)
                .clubName("Home Club")
                .stadium(stadium)
                .build();

        // Create seats
        seats = Arrays.asList(
                SeatEntity.builder().id(1L).price(50.0).build(),
                SeatEntity.builder().id(2L).price(50.0).build(),
                SeatEntity.builder().id(3L).price(50.0).build(),
                SeatEntity.builder().id(4L).price(50.0).build()
        );

        // Create block
        BlockEntity block = BlockEntity.builder()
                .id(1L)
                .seats(seats)
                .build();

        // Create box
        box = BoxEntity.builder()
                .id(1L)
                .price(100.0)
                .blocks(List.of(block))
                .build();

        // Create pricing rules
        pricingRules = Arrays.asList(
                BoxPricingRuleEntity.builder()
                        .id(1L)
                        .stadium(stadium)
                        .occupancyThreshold(80.0)
                        .priceIncreasePercentage(20.0)
                        .build(),
                BoxPricingRuleEntity.builder()
                        .id(2L)
                        .stadium(stadium)
                        .occupancyThreshold(50.0)
                        .priceIncreasePercentage(10.0)
                        .build()
        );

        // Create available seats map
        availableSeats = new HashMap<>();
        seats.forEach(seat -> availableSeats.put(seat, true));

        // Create match
        match = MatchEntity.builder()
                .id(1L)
                .homeClub(homeClub)
                .availableSeats(availableSeats)
                .build();
    }

    @Test
    void applyPricingRules_MatchNotFound_ThrowsException() {
        // Arrange
        when(matchService.getMatchById(1L)).thenReturn(Optional.empty());

        // Act & Assert
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class,
                () -> applyPricingRulesUseCase.applyPricingRules(1L));
        assertEquals("Match not found with id: 1", exception.getMessage());

        verify(boxPricingRuleRepository, never()).findAllByStadiumId(any());
        verify(boxService, never()).findByStadiumId(any());
    }

    @Test
    void applyPricingRules_NoPricingRules_NoChanges() {
        // Arrange
        when(matchService.getMatchById(1L)).thenReturn(Optional.of(match));
        when(boxPricingRuleRepository.findAllByStadiumId(1L)).thenReturn(Collections.emptyList());

        // Act
        applyPricingRulesUseCase.applyPricingRules(1L);

        // Assert
        verify(updateMatchSpecificPriceUseCase, never()).updateMatchSpecificBoxPrice(anyLong(), anyLong(), anyDouble());
        verify(updateMatchSpecificPriceUseCase, never()).updateMatchSpecificSeatPrices(anyLong(), anyMap());
    }


    @Test
    void applyPricingRules_HighOccupancy_AppliesHighestRule() {
        // Arrange
        // Set 3 out of 4 seats as occupied (75% occupancy)
        availableSeats.put(seats.get(0), false);
        availableSeats.put(seats.get(1), false);
        availableSeats.put(seats.get(2), false);

        when(matchService.getMatchById(1L)).thenReturn(Optional.of(match));
        when(boxPricingRuleRepository.findAllByStadiumId(1L)).thenReturn(pricingRules);
        when(boxService.findByStadiumId(1L)).thenReturn(List.of(box));

        // Act
        applyPricingRulesUseCase.applyPricingRules(1L);

        // Assert
        // Verify box price update
        verify(updateMatchSpecificPriceUseCase).updateMatchSpecificBoxPrice(1L, 1L, 110.0); // 10% increase

        // Verify seat price updates
        verify(updateMatchSpecificPriceUseCase).updateMatchSpecificSeatPrices(eq(1L), seatPricesCaptor.capture());
        Map<Long, Double> updatedSeatPrices = seatPricesCaptor.getValue();
        assertEquals(4, updatedSeatPrices.size());
        assertEquals(55.0, updatedSeatPrices.get(1L), 0.001); // Added delta for floating point comparison
    }

    @Test
    void applyPricingRules_LowOccupancy_NoChanges() {
        // Arrange
        // Set 1 out of 4 seats as occupied (25% occupancy)
        availableSeats.put(seats.get(0), false);

        when(matchService.getMatchById(1L)).thenReturn(Optional.of(match));
        when(boxPricingRuleRepository.findAllByStadiumId(1L)).thenReturn(pricingRules);
        when(boxService.findByStadiumId(1L)).thenReturn(List.of(box));

        // Act
        applyPricingRulesUseCase.applyPricingRules(1L);

        // Assert
        verify(updateMatchSpecificPriceUseCase, never()).updateMatchSpecificBoxPrice(anyLong(), anyLong(), anyDouble());
        verify(updateMatchSpecificPriceUseCase, never()).updateMatchSpecificSeatPrices(anyLong(), anyMap());
    }

    @Test
    void applyPricingRules_NullAvailableSeats_HandlesGracefully() {
        // Arrange
        match.setAvailableSeats(null);

        when(matchService.getMatchById(1L)).thenReturn(Optional.of(match));
        when(boxPricingRuleRepository.findAllByStadiumId(1L)).thenReturn(pricingRules);
        when(boxService.findByStadiumId(1L)).thenReturn(List.of(box));

        // Act
        applyPricingRulesUseCase.applyPricingRules(1L);

        // Assert
        verify(updateMatchSpecificPriceUseCase, never()).updateMatchSpecificBoxPrice(anyLong(), anyLong(), anyDouble());
        verify(updateMatchSpecificPriceUseCase, never()).updateMatchSpecificSeatPrices(anyLong(), anyMap());
    }


}