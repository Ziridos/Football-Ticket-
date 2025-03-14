package nl.fontys.s3.ticketmaster.business.interfaces.ticketinterfaces;

import nl.fontys.s3.ticketmaster.domain.ticket.CreateTicketRequest;
import nl.fontys.s3.ticketmaster.domain.ticket.CreateTicketResponse;

public interface CreateTicketUseCase {
    CreateTicketResponse createTicket(CreateTicketRequest request);
}