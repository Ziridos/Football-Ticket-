package nl.fontys.s3.ticketmaster.business.interfaces.ticketinterfaces;

import nl.fontys.s3.ticketmaster.domain.ticket.CreateTicketRequest;
import nl.fontys.s3.ticketmaster.domain.ticket.UpdateTicketRequest;

public interface TicketValidationService {
    void validateTicketRequest(CreateTicketRequest request);
    void validateTicketExists(Long ticketId);
    void validateTicketUpdateRequest(UpdateTicketRequest request);
}
