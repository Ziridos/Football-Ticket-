package nl.fontys.s3.ticketmaster.business.impl.ticketimpl;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import nl.fontys.s3.ticketmaster.business.exception.InvalidMatchException;
import nl.fontys.s3.ticketmaster.business.exception.InvalidSeatException;
import nl.fontys.s3.ticketmaster.business.interfaces.ticketinterfaces.CreateTicketUseCase;
import nl.fontys.s3.ticketmaster.business.interfaces.ticketinterfaces.TicketValidationService;
import nl.fontys.s3.ticketmaster.business.interfaces.ticketinterfaces.TicketCreationService;
import nl.fontys.s3.ticketmaster.business.interfaces.matchinterfaces.MatchUpdateService;
import nl.fontys.s3.ticketmaster.business.interfaces.matchinterfaces.ApplyPricingRulesUseCase;
import nl.fontys.s3.ticketmaster.business.interfaces.ticketinterfaces.TicketMapper;
import nl.fontys.s3.ticketmaster.domain.ticket.CreateTicketRequest;
import nl.fontys.s3.ticketmaster.domain.ticket.CreateTicketResponse;
import nl.fontys.s3.ticketmaster.persitence.entity.TicketEntity;
import nl.fontys.s3.ticketmaster.persitence.entity.MatchEntity;
import nl.fontys.s3.ticketmaster.persitence.entity.SeatEntity;
import nl.fontys.s3.ticketmaster.persitence.MatchRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Map;
import java.util.Optional;

@Slf4j
@Service
@AllArgsConstructor
public class CreateTicketUseCaseImpl implements CreateTicketUseCase {
    private final TicketValidationService ticketValidationService;
    private final TicketCreationService ticketCreationService;
    private final MatchUpdateService matchUpdateService;
    private final ApplyPricingRulesUseCase applyPricingRulesUseCase;
    private final TicketMapper ticketMapper;
    private final MatchRepository matchRepository;

    @Override
    @Transactional
    public CreateTicketResponse createTicket(CreateTicketRequest request) {
        log.info("Creating ticket for request: {}", request);

        try {
            ticketValidationService.validateTicketRequest(request);
        } catch (InvalidSeatException e) {
            log.error("Seat validation failed: {}", e.getMessage());
            throw e;
        }

        Optional<MatchEntity> matchOptional = matchRepository.findById(request.getMatchId());
        if (matchOptional.isEmpty()) {
            log.error("Match not found for id: {}", request.getMatchId());
            throw new InvalidMatchException("Match not found");
        }
        MatchEntity match = matchOptional.get();

        // Double-check seat availability
        Map<SeatEntity, Boolean> availableSeats = match.getAvailableSeats();
        for (Long seatId : request.getSeatIds()) {
            boolean seatAvailable = availableSeats.entrySet().stream()
                    .filter(entry -> entry.getKey().getId().equals(seatId))
                    .findFirst()
                    .map(Map.Entry::getValue)
                    .orElse(false);

            if (!seatAvailable) {
                log.error("Seat {} is not available for match {}", seatId, match.getId());
                throw new InvalidSeatException("Seat " + seatId + " is not available");
            }
        }

        TicketEntity ticket = ticketCreationService.createTicket(request);

        try {
            matchUpdateService.updateMatchSeats(request.getMatchId(), request.getSeatIds());
        } catch (Exception e) {
            log.error("Failed to update match seats: {}", e.getMessage());
            throw new InvalidSeatException("Failed to update match seats");
        }

        try {
            applyPricingRulesUseCase.applyPricingRules(request.getMatchId());
        } catch (Exception e) {
            log.error("Failed to apply pricing rules: {}", e.getMessage());
        }

        log.info("Ticket created successfully with id: {}", ticket.getId());
        return ticketMapper.toCreateTicketResponse(ticket);
    }
}