package nl.fontys.s3.ticketmaster.business.interfaces.ticketinterfaces;

import nl.fontys.s3.ticketmaster.domain.ticket.CreateTicketRequest;
import nl.fontys.s3.ticketmaster.persitence.entity.TicketEntity;

public interface TicketCreationService {
    TicketEntity createTicket(CreateTicketRequest request);
}
