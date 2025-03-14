package nl.fontys.s3.ticketmaster.business.interfaces.matchinterfaces;

import nl.fontys.s3.ticketmaster.domain.seat.SeatAvailabilityResponse;

import java.util.List;

public interface GetSeatAvailabilityUseCase {
    List<SeatAvailabilityResponse> getSeatAvailability(Long matchId);
}