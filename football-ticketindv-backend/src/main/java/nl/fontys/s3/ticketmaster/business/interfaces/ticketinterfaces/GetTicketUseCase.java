package nl.fontys.s3.ticketmaster.business.interfaces.ticketinterfaces;

import nl.fontys.s3.ticketmaster.domain.ticket.GetTicketRequest;
import nl.fontys.s3.ticketmaster.domain.ticket.GetTicketResponse;

public interface GetTicketUseCase {
    GetTicketResponse getTicket(GetTicketRequest request);
}
