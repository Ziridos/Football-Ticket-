package nl.fontys.s3.ticketmaster.business.impl.boxpricingrule;

import lombok.AllArgsConstructor;
import nl.fontys.s3.ticketmaster.business.interfaces.matchinterfaces.MatchService;
import nl.fontys.s3.ticketmaster.business.interfaces.matchinterfaces.UpdateMatchSpecificPriceUseCase;
import nl.fontys.s3.ticketmaster.persitence.BoxRepository;
import nl.fontys.s3.ticketmaster.persitence.MatchRepository;
import nl.fontys.s3.ticketmaster.persitence.SeatRepository;
import nl.fontys.s3.ticketmaster.persitence.entity.BoxEntity;
import nl.fontys.s3.ticketmaster.persitence.entity.MatchEntity;
import nl.fontys.s3.ticketmaster.persitence.entity.SeatEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashMap;
import java.util.Map;

@Service
@AllArgsConstructor
public class UpdateMatchSpecificPriceUseCaseImpl implements UpdateMatchSpecificPriceUseCase {
    private final MatchRepository matchRepository;
    private final MatchService matchService;
    private final BoxRepository boxRepository;
    private final SeatRepository seatRepository;

    @Override
    @Transactional
    public void updateMatchSpecificBoxPrice(Long matchId, Long boxId, double newPrice) {
        MatchEntity match = matchService.getMatchById(matchId)
                .orElseThrow(() -> new RuntimeException("Match not found with id: " + matchId));

        BoxEntity box = boxRepository.findById(boxId)
                .orElseThrow(() -> new RuntimeException("Box not found with id: " + boxId));

        Map<BoxEntity, Double> prices = match.getMatchSpecificBoxPrices();
        if (prices == null) {
            prices = new HashMap<>();
            match.setMatchSpecificBoxPrices(prices);
        }
        prices.put(box, newPrice);
        matchRepository.save(match);
    }

    @Override
    @Transactional
    public void updateMatchSpecificSeatPrices(Long matchId, Map<Long, Double> newSeatPrices) {
        MatchEntity match = matchService.getMatchById(matchId)
                .orElseThrow(() -> new RuntimeException("Match not found with id: " + matchId));

        Map<SeatEntity, Double> prices = match.getMatchSpecificSeatPrices();
        if (prices == null) {
            prices = new HashMap<>();
            match.setMatchSpecificSeatPrices(prices);
        }

        for (Map.Entry<Long, Double> entry : newSeatPrices.entrySet()) {
            SeatEntity seat = seatRepository.findById(entry.getKey())
                    .orElseThrow(() -> new RuntimeException("Seat not found with id: " + entry.getKey()));
            prices.put(seat, entry.getValue());
        }

        matchRepository.save(match);
    }
}