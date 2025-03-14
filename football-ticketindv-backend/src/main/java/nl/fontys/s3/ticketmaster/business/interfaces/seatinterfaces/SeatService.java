package nl.fontys.s3.ticketmaster.business.interfaces.seatinterfaces;

import nl.fontys.s3.ticketmaster.persitence.entity.SeatEntity;

import java.util.List;

public interface SeatService {
    List<SeatEntity> getSeatsByStadium(Long stadiumId);
    void saveAllSeats(List<SeatEntity> seats);
}
