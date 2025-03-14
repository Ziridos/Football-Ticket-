package nl.fontys.s3.ticketmaster.business.interfaces.stadiuminterfaces;

import nl.fontys.s3.ticketmaster.domain.stadium.GetStadiumRequest;
import nl.fontys.s3.ticketmaster.domain.stadium.GetStadiumResponse;

public interface GetStadiumUseCase {
    GetStadiumResponse getStadiumById(GetStadiumRequest request);
}
