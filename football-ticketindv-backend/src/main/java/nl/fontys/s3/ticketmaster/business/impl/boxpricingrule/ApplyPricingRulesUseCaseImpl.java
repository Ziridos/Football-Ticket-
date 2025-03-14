package nl.fontys.s3.ticketmaster.business.impl.boxpricingrule;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import nl.fontys.s3.ticketmaster.business.interfaces.boxinterfaces.BoxService;
import nl.fontys.s3.ticketmaster.business.interfaces.matchinterfaces.ApplyPricingRulesUseCase;
import nl.fontys.s3.ticketmaster.business.interfaces.matchinterfaces.MatchService;
import nl.fontys.s3.ticketmaster.business.interfaces.matchinterfaces.UpdateMatchSpecificPriceUseCase;
import nl.fontys.s3.ticketmaster.persitence.BoxPricingRuleRepository;
import nl.fontys.s3.ticketmaster.persitence.entity.BoxEntity;
import nl.fontys.s3.ticketmaster.persitence.entity.BoxPricingRuleEntity;
import nl.fontys.s3.ticketmaster.persitence.entity.MatchEntity;
import nl.fontys.s3.ticketmaster.persitence.entity.BlockEntity;
import nl.fontys.s3.ticketmaster.persitence.entity.SeatEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;

@Slf4j
@Service
@AllArgsConstructor
public class ApplyPricingRulesUseCaseImpl implements ApplyPricingRulesUseCase {
    private final BoxPricingRuleRepository boxPricingRuleRepository;
    private final UpdateMatchSpecificPriceUseCase updateMatchSpecificPriceUseCase;
    private final MatchService matchService;
    private final BoxService boxService;

    @Override
    @Transactional
    public void applyPricingRules(Long matchId) {
        Optional<MatchEntity> matchOptional = matchService.getMatchById(matchId);

        if (matchOptional.isEmpty()) {
            log.error("Match not found with id: {}", matchId);
            throw new IllegalArgumentException("Match not found with id: " + matchId);
        }

        MatchEntity match = matchOptional.get();
        Long stadiumId = match.getHomeClub().getStadium().getId();

        List<BoxPricingRuleEntity> pricingRules = boxPricingRuleRepository.findAllByStadiumId(stadiumId);
        if (pricingRules.isEmpty()) {
            log.info("No pricing rules found for stadium id: {}", stadiumId);
            return;
        }

        pricingRules.sort(Comparator.comparingDouble(BoxPricingRuleEntity::getOccupancyThreshold).reversed());

        List<BoxEntity> boxes = boxService.findByStadiumId(stadiumId);

        for (BoxEntity box : boxes) {
            try {
                applyPricingRuleToBox(match, box, pricingRules);
            } catch (Exception e) {
                log.error("Error applying pricing rule to box {}: {}", box.getId(), e.getMessage());
            }
        }
    }

    private void applyPricingRuleToBox(MatchEntity match, BoxEntity box, List<BoxPricingRuleEntity> pricingRules) {
        int totalSeats = box.getBlocks().stream()
                .mapToInt(block -> block.getSeats().size())
                .sum();

        long occupiedSeats = box.getBlocks().stream()
                .flatMap(block -> block.getSeats().stream())
                .filter(seat -> !isAvailable(match, seat))
                .count();

        double occupancyRate = (double) occupiedSeats / totalSeats;
        double occupancyPercentage = occupancyRate * 100;

        double originalPrice = box.getPrice();
        double newPrice = originalPrice;
        boolean priceChanged = false;

        for (BoxPricingRuleEntity rule : pricingRules) {
            if (occupancyPercentage >= rule.getOccupancyThreshold()) {
                double priceIncrease = originalPrice * (rule.getPriceIncreasePercentage() / 100.0);
                newPrice = originalPrice + priceIncrease;
                priceChanged = true;
                break;
            }
        }

        if (priceChanged) {
            updateMatchSpecificPriceUseCase.updateMatchSpecificBoxPrice(match.getId(), box.getId(), newPrice);

            Map<Long, Double> newSeatPrices = new HashMap<>();
            for (BlockEntity block : box.getBlocks()) {
                for (SeatEntity seat : block.getSeats()) {
                    double originalSeatPrice = seat.getPrice();
                    double newSeatPrice = originalSeatPrice * (newPrice / originalPrice);
                    newSeatPrices.put(seat.getId(), newSeatPrice);
                }
            }
            updateMatchSpecificPriceUseCase.updateMatchSpecificSeatPrices(match.getId(), newSeatPrices);
        }
    }

    private boolean isAvailable(MatchEntity match, SeatEntity seat) {
        Map<SeatEntity, Boolean> availableSeats = match.getAvailableSeats();
        if (availableSeats == null) {
            log.warn("Available seats map is null for match {}", match.getId());
            return true; // Assume available if information is missing
        }
        Boolean isAvailable = availableSeats.get(seat);
        if (isAvailable == null) {
            log.warn("Availability information missing for seat {} in match {}", seat.getId(), match.getId());
            return true; // Assume available if information is missing
        }
        return isAvailable;
    }
}