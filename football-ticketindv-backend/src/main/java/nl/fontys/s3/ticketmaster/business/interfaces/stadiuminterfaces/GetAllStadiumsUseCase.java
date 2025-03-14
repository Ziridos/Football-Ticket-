package nl.fontys.s3.ticketmaster.business.interfaces.stadiuminterfaces;

import nl.fontys.s3.ticketmaster.domain.stadium.GetAllStadiumsRequest;
import nl.fontys.s3.ticketmaster.domain.stadium.GetAllStadiumsResponse;

public interface GetAllStadiumsUseCase {
    GetAllStadiumsResponse getAllStadiums(GetAllStadiumsRequest request);
}