package nl.fontys.s3.ticketmaster.business.impl.ticketimpl;

import lombok.AllArgsConstructor;
import nl.fontys.s3.ticketmaster.business.interfaces.ticketinterfaces.GetTicketUseCase;
import nl.fontys.s3.ticketmaster.business.interfaces.ticketinterfaces.TicketValidationService;
import nl.fontys.s3.ticketmaster.business.interfaces.ticketinterfaces.TicketMapper;
import nl.fontys.s3.ticketmaster.domain.ticket.GetTicketRequest;
import nl.fontys.s3.ticketmaster.domain.ticket.GetTicketResponse;
import nl.fontys.s3.ticketmaster.persitence.TicketRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


@Service
@AllArgsConstructor
public class GetTicketUseCaseImpl implements GetTicketUseCase {
    private final TicketRepository ticketRepository;
    private final TicketValidationService ticketValidationService;
    private final TicketMapper ticketMapper;

    @Override
    @Transactional
    public GetTicketResponse getTicket(GetTicketRequest request) {
        ticketValidationService.validateTicketExists(request.getId());
        return ticketRepository.findById(request.getId())
                .map(ticketMapper::toGetTicketResponse)
                .orElseThrow(() -> new RuntimeException("Ticket not found with id: " + request.getId()));
    }
}