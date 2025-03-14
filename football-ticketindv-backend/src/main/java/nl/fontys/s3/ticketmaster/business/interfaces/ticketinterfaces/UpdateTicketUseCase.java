package nl.fontys.s3.ticketmaster.business.interfaces.ticketinterfaces;

import nl.fontys.s3.ticketmaster.domain.ticket.UpdateTicketRequest;
import nl.fontys.s3.ticketmaster.domain.ticket.UpdateTicketResponse;

public interface UpdateTicketUseCase {
    UpdateTicketResponse updateTicket(Long ticketId, UpdateTicketRequest request);
}
