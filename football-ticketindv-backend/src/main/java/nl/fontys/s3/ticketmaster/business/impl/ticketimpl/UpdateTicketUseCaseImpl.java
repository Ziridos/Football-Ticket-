package nl.fontys.s3.ticketmaster.business.impl.ticketimpl;

import lombok.AllArgsConstructor;
import nl.fontys.s3.ticketmaster.business.interfaces.ticketinterfaces.UpdateTicketUseCase;
import nl.fontys.s3.ticketmaster.business.interfaces.ticketinterfaces.TicketValidationService;
import nl.fontys.s3.ticketmaster.business.interfaces.ticketinterfaces.TicketMapper;
import nl.fontys.s3.ticketmaster.business.interfaces.matchinterfaces.MatchUpdateService;
import nl.fontys.s3.ticketmaster.domain.ticket.UpdateTicketRequest;
import nl.fontys.s3.ticketmaster.domain.ticket.UpdateTicketResponse;
import nl.fontys.s3.ticketmaster.persitence.TicketRepository;
import nl.fontys.s3.ticketmaster.persitence.entity.TicketEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
@AllArgsConstructor
public class UpdateTicketUseCaseImpl implements UpdateTicketUseCase {
    private final TicketRepository ticketRepository;
    private final TicketValidationService ticketValidationService;
    private final TicketMapper ticketMapper;
    private final MatchUpdateService matchUpdateService;

    @Override
    @Transactional
    public UpdateTicketResponse updateTicket(Long ticketId, UpdateTicketRequest request) {
        ticketValidationService.validateTicketExists(ticketId);
        ticketValidationService.validateTicketUpdateRequest(request);

        Optional<TicketEntity> optionalTicket = ticketRepository.findById(ticketId);
        if (optionalTicket.isEmpty()) {
            throw new IllegalStateException("Ticket not found");
        }
        TicketEntity ticket = optionalTicket.get();

        TicketEntity updatedTicket = ticketMapper.toUpdatedTicketEntity(ticket, request);

        matchUpdateService.updateMatchSeatsForTicketUpdate(ticket, updatedTicket);

        TicketEntity savedTicket = ticketRepository.save(updatedTicket);
        return ticketMapper.toUpdateTicketResponse(savedTicket);
    }
}