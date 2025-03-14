package nl.fontys.s3.ticketmaster.business.interfaces.seatinterfaces;

import nl.fontys.s3.ticketmaster.domain.seat.SeatAvailabilityResponse;
import nl.fontys.s3.ticketmaster.domain.seat.SeatDTO;
import nl.fontys.s3.ticketmaster.persitence.entity.SeatEntity;

import java.util.List;
import java.util.Map;

public interface SeatConverter {
    SeatAvailabilityResponse convertToSeatAvailabilityResponse(SeatEntity seat, boolean isAvailable);
    List<SeatAvailabilityResponse> convertToSeatAvailabilityResponses(List<SeatEntity> seats, Map<Long, Boolean> availabilityMap);
    SeatDTO toSeatDTO(SeatEntity entity);
}