package nl.fontys.s3.ticketmaster.business.interfaces.websockets;

import java.util.Map;
import java.util.Optional;

public interface SelectedSeatsService {
    void addSelectedSeat(Long matchId, Long seatId, Long userId);
    void removeSelectedSeat(Long matchId, Long seatId);
    Map<Long, Long> getSelectedSeatsForMatch(Long matchId);
    void cleanupOldSelections();
    Optional<Long> getSelectionTimeRemaining(Long matchId, Long seatId);

}
