package nl.fontys.s3.ticketmaster.business.interfaces.stadiuminterfaces;

import nl.fontys.s3.ticketmaster.domain.stadium.UpdateStadiumRequest;
import nl.fontys.s3.ticketmaster.domain.stadium.UpdateStadiumResponse;

public interface UpdateStadiumUseCase {
    UpdateStadiumResponse updateStadium(Long id, UpdateStadiumRequest request);
}
