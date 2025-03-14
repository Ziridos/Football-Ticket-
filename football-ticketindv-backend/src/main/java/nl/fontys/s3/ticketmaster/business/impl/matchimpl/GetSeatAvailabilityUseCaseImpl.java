package nl.fontys.s3.ticketmaster.business.impl.matchimpl;

import lombok.AllArgsConstructor;
import nl.fontys.s3.ticketmaster.business.interfaces.matchinterfaces.GetSeatAvailabilityUseCase;
import nl.fontys.s3.ticketmaster.business.interfaces.seatinterfaces.SeatConverter;
import nl.fontys.s3.ticketmaster.business.interfaces.seatinterfaces.SeatService;
import nl.fontys.s3.ticketmaster.business.interfaces.matchinterfaces.MatchService;
import nl.fontys.s3.ticketmaster.domain.seat.SeatAvailabilityResponse;
import nl.fontys.s3.ticketmaster.persitence.entity.MatchEntity;
import nl.fontys.s3.ticketmaster.persitence.entity.SeatEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class GetSeatAvailabilityUseCaseImpl implements GetSeatAvailabilityUseCase {
    private final MatchService matchService;
    private final SeatService seatService;
    private final SeatConverter seatConverter;

    @Override
    @Transactional
    public List<SeatAvailabilityResponse> getSeatAvailability(Long matchId) {
        MatchEntity match = matchService.getMatchById(matchId)
                .orElseThrow(() -> new RuntimeException("Match not found with id: " + matchId));

        Long stadiumId = match.getHomeClub().getStadium().getId();
        List<SeatEntity> allSeats = seatService.getSeatsByStadium(stadiumId);

        // Convert Map<SeatEntity, Boolean> to Map<Long, Boolean>
        Map<Long, Boolean> availableSeatsMap = match.getAvailableSeats().entrySet().stream()
                .collect(Collectors.toMap(
                        entry -> entry.getKey().getId(),
                        Map.Entry::getValue
                ));

        return seatConverter.convertToSeatAvailabilityResponses(allSeats, availableSeatsMap);
    }
}