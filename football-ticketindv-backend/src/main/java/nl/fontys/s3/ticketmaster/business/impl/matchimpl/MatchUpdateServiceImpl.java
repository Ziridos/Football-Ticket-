package nl.fontys.s3.ticketmaster.business.impl.matchimpl;

import lombok.AllArgsConstructor;
import nl.fontys.s3.ticketmaster.business.interfaces.matchinterfaces.MatchUpdateService;
import nl.fontys.s3.ticketmaster.business.interfaces.seatinterfaces.SeatService;
import nl.fontys.s3.ticketmaster.persitence.MatchRepository;
import nl.fontys.s3.ticketmaster.persitence.entity.MatchEntity;
import nl.fontys.s3.ticketmaster.persitence.entity.SeatEntity;
import nl.fontys.s3.ticketmaster.persitence.entity.TicketEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;

@Service
@AllArgsConstructor
public class MatchUpdateServiceImpl implements MatchUpdateService {
    private final MatchRepository matchRepository;
    private final SeatService seatService;

    @Override
    @Transactional
    public void updateMatchSeats(Long matchId, List<Long> seatIds) {
        MatchEntity match = matchRepository.findById(matchId)
                .orElseThrow(() -> new RuntimeException("Match not found with id: " + matchId));

        Long stadiumId = match.getHomeClub().getStadium().getId();
        List<SeatEntity> allStadiumSeats = seatService.getSeatsByStadium(stadiumId);

        List<SeatEntity> seatsToUpdate = allStadiumSeats.stream()
                .filter(seat -> seatIds.contains(seat.getId()))
                .toList();

        for (SeatEntity seat : seatsToUpdate) {
            match.getAvailableSeats().put(seat, false);
        }
        matchRepository.save(match);
    }

    @Override
    @Transactional
    public void updateMatchSeatsForTicketUpdate(TicketEntity originalTicket, TicketEntity updatedTicket) {
        MatchEntity match = updatedTicket.getMatch();

        Map<SeatEntity, Boolean> availableSeats = match.getAvailableSeats();

        originalTicket.getSeats().forEach(oldSeat -> availableSeats.put(oldSeat, true));
        updatedTicket.getSeats().forEach(newSeat -> availableSeats.put(newSeat, false));

        matchRepository.save(match);
    }
}