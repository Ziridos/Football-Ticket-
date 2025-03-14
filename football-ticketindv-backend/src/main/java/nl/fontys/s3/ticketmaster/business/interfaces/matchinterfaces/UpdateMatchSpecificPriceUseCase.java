package nl.fontys.s3.ticketmaster.business.interfaces.matchinterfaces;

import java.util.Map;

public interface UpdateMatchSpecificPriceUseCase {
    void updateMatchSpecificBoxPrice(Long matchId, Long boxId, double newPrice);
    void updateMatchSpecificSeatPrices(Long matchId, Map<Long, Double> newSeatPrices);
}
