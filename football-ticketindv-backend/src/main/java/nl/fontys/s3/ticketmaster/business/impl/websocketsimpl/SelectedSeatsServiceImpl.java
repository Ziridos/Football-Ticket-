package nl.fontys.s3.ticketmaster.business.impl.websocketsimpl;

import lombok.RequiredArgsConstructor;
import nl.fontys.s3.ticketmaster.business.interfaces.websockets.SelectedSeatsService;
import nl.fontys.s3.ticketmaster.domain.websocket.SeatSelectionMessage;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

@Service
@RequiredArgsConstructor
public class SelectedSeatsServiceImpl implements SelectedSeatsService {
    private final SimpMessagingTemplate messagingTemplate;


    private static final long SELECTION_TIMEOUT = 900000;


    private final Map<Long, Map<Long, SelectionInfo>> selectedSeats = new ConcurrentHashMap<>();

    private static class SelectionInfo {
        private final Long userId;
        private final Long timestamp;

        public SelectionInfo(Long userId) {
            this.userId = userId;
            this.timestamp = System.currentTimeMillis();
        }
    }

    @Override
    public Map<Long, Long> getSelectedSeatsForMatch(Long matchId) {
        Map<Long, SelectionInfo> matchSeats = selectedSeats.get(matchId);
        Map<Long, Long> result = new HashMap<>();

        if (matchSeats != null) {
            long currentTime = System.currentTimeMillis();
            matchSeats.forEach((seatId, info) -> {

                if (currentTime - info.timestamp <= SELECTION_TIMEOUT) {
                    result.put(seatId, info.userId);
                } else {

                    matchSeats.remove(seatId);
                    notifySelectionExpired(matchId, seatId, info.userId);
                }
            });
        }

        return result;
    }

    @Scheduled(fixedRate = 60000)
    public void cleanupOldSelections() {
        long currentTime = System.currentTimeMillis();

        selectedSeats.forEach((matchId, seatMap) -> {
            Set<Long> seatsToRemove = new HashSet<>();

            seatMap.forEach((seatId, info) -> {
                if (currentTime - info.timestamp > SELECTION_TIMEOUT) {
                    seatsToRemove.add(seatId);
                }
            });

            seatsToRemove.forEach(seatId -> {
                SelectionInfo info = seatMap.remove(seatId);
                if (info != null) {
                    notifySelectionExpired(matchId, seatId, info.userId);
                }
            });

            if (seatMap.isEmpty()) {
                selectedSeats.remove(matchId);
            }
        });
    }

    private void notifySelectionExpired(Long matchId, Long seatId, Long userId) {
        SeatSelectionMessage message = new SeatSelectionMessage();
        message.setMatchId(matchId);
        message.setSeatId(seatId);
        message.setAction("DESELECT");
        message.setUserId(userId);
        message.setExpired(true);
        message.setTimestamp(System.currentTimeMillis());

        messagingTemplate.convertAndSend("/topic/seat-updates", message);
    }

    @Override
    public void addSelectedSeat(Long matchId, Long seatId, Long userId) {
        selectedSeats.computeIfAbsent(matchId, k -> new ConcurrentHashMap<>())
                .put(seatId, new SelectionInfo(userId));
    }

    @Override
    public void removeSelectedSeat(Long matchId, Long seatId) {
        if (selectedSeats.containsKey(matchId)) {
            selectedSeats.get(matchId).remove(seatId);
            if (selectedSeats.get(matchId).isEmpty()) {
                selectedSeats.remove(matchId);
            }
        }
    }


    public Optional<Long> getSelectionTimeRemaining(Long matchId, Long seatId) {
        Map<Long, SelectionInfo> matchSeats = selectedSeats.get(matchId);
        if (matchSeats != null) {
            SelectionInfo info = matchSeats.get(seatId);
            if (info != null) {
                long timeElapsed = System.currentTimeMillis() - info.timestamp;
                long timeRemaining = SELECTION_TIMEOUT - timeElapsed;
                return timeRemaining > 0 ? Optional.of(timeRemaining) : Optional.empty();
            }
        }
        return Optional.empty();
    }
}