package nl.fontys.s3.ticketmaster.business.impl.ticketimpl;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import nl.fontys.s3.ticketmaster.business.exception.InvalidSeatException;
import nl.fontys.s3.ticketmaster.business.exception.InvalidTicketException;
import nl.fontys.s3.ticketmaster.business.interfaces.matchinterfaces.MatchService;
import nl.fontys.s3.ticketmaster.business.interfaces.ticketinterfaces.TicketValidationService;
import nl.fontys.s3.ticketmaster.business.interfaces.userinterfaces.UserValidator;
import nl.fontys.s3.ticketmaster.business.interfaces.matchinterfaces.MatchValidator;
import nl.fontys.s3.ticketmaster.domain.ticket.CreateTicketRequest;
import nl.fontys.s3.ticketmaster.domain.ticket.UpdateTicketRequest;
import nl.fontys.s3.ticketmaster.persitence.TicketRepository;
import nl.fontys.s3.ticketmaster.persitence.entity.MatchEntity;
import nl.fontys.s3.ticketmaster.persitence.entity.SeatEntity;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;


@Slf4j
@Service
@AllArgsConstructor
public class TicketValidationServiceImpl implements TicketValidationService {
    private final UserValidator userValidator;
    private final MatchValidator matchValidator;
    private final TicketRepository ticketRepository;
    private final MatchService matchService;

    @Override
    public void validateTicketRequest(CreateTicketRequest request) {
        userValidator.validateUserExists(request.getUserId());
        MatchEntity match = validateAndGetMatch(request.getMatchId());
        validateSeats(match, request.getSeatIds());
    }

    @Override
    public void validateTicketExists(Long ticketId) {
        if (!ticketRepository.existsById(ticketId)) {
            throw new InvalidTicketException("Ticket with the given ID does not exist.");
        }
    }

    @Override
    public void validateTicketUpdateRequest(UpdateTicketRequest request) {
        userValidator.validateUserExists(request.getUserId());
        MatchEntity match = validateAndGetMatch(request.getMatchId());
        validateSeats(match, request.getSeatIds());
    }

    private MatchEntity validateAndGetMatch(Long matchId) {
        matchValidator.validateMatchExists(matchId);
        return matchService.getMatchById(matchId)
                .orElseThrow(() -> new InvalidTicketException("Match not found with id: " + matchId));
    }

    private void validateSeats(MatchEntity match, List<Long> seatIds) {
        Map<SeatEntity, Boolean> availableSeats = match.getAvailableSeats();
        log.debug("Available seats for match {}: {}", match.getId(), availableSeats);

        List<Long> unavailableSeats = seatIds.stream()
                .filter(seatId -> {
                    boolean seatFound = false;
                    boolean isAvailable = false;
                    for (Map.Entry<SeatEntity, Boolean> entry : availableSeats.entrySet()) {
                        if (entry.getKey().getId().equals(seatId)) {
                            seatFound = true;
                            Boolean availability = entry.getValue();
                            if (availability == null) {
                                log.warn("Seat {} has null availability for match {}", seatId, match.getId());
                                return true;
                            }
                            isAvailable = availability;
                            break;
                        }
                    }
                    if (!seatFound) {
                        log.warn("Seat {} not found in availableSeats for match {}", seatId, match.getId());
                        return true;
                    }
                    if (!isAvailable) {
                        log.warn("Seat {} is not available for match {}", seatId, match.getId());
                    }
                    return !isAvailable;
                })
                .toList();

        if (!unavailableSeats.isEmpty()) {
            throw new InvalidSeatException("The following seats are not available for this match: " + unavailableSeats);
        }
    }
}