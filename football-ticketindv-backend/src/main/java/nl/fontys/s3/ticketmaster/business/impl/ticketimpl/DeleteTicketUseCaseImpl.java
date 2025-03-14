package nl.fontys.s3.ticketmaster.business.impl.ticketimpl;

import lombok.AllArgsConstructor;
import nl.fontys.s3.ticketmaster.business.interfaces.ticketinterfaces.DeleteTicketUseCase;
import nl.fontys.s3.ticketmaster.business.interfaces.ticketinterfaces.TicketValidationService;
import nl.fontys.s3.ticketmaster.persitence.TicketRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@AllArgsConstructor
public class DeleteTicketUseCaseImpl implements DeleteTicketUseCase {
    private final TicketRepository ticketRepository;
    private final TicketValidationService ticketValidationService;

    @Override
    @Transactional
    public void deleteTicket(Long ticketId) {
        ticketValidationService.validateTicketExists(ticketId);
        ticketRepository.deleteById(ticketId);
    }
}