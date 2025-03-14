package nl.fontys.s3.ticketmaster.business.interfaces.ticketinterfaces;

import nl.fontys.s3.ticketmaster.domain.ticket.GetAllTicketsUserRequest;
import nl.fontys.s3.ticketmaster.domain.ticket.GetAllTicketsUserResponse;

public interface GetAllTicketsUserUseCase {
    GetAllTicketsUserResponse getAllTicketsForUser(GetAllTicketsUserRequest request);
}
