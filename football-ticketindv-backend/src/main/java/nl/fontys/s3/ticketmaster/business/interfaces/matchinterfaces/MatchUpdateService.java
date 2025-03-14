package nl.fontys.s3.ticketmaster.business.interfaces.matchinterfaces;

import nl.fontys.s3.ticketmaster.persitence.entity.TicketEntity;

import java.util.List;

public interface MatchUpdateService {
    void updateMatchSeats(Long matchId, List<Long> seatIds);
    void updateMatchSeatsForTicketUpdate(TicketEntity originalTicket, TicketEntity updatedTicket);
}
