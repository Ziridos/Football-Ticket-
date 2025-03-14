package nl.fontys.s3.ticketmaster.business.interfaces.stadiuminterfaces;

import nl.fontys.s3.ticketmaster.domain.stadium.CreateStadiumRequest;
import nl.fontys.s3.ticketmaster.domain.stadium.CreateStadiumResponse;

public interface CreateStadiumUseCase {
    CreateStadiumResponse createStadium(CreateStadiumRequest request);
}
