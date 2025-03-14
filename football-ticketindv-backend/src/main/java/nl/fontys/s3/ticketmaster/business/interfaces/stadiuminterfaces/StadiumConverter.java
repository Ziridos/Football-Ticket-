package nl.fontys.s3.ticketmaster.business.interfaces.stadiuminterfaces;

import nl.fontys.s3.ticketmaster.domain.stadium.*;
import nl.fontys.s3.ticketmaster.persitence.entity.StadiumEntity;

public interface StadiumConverter {
    StadiumEntity convertToEntity(CreateStadiumRequest request);
    CreateStadiumResponse convertToCreateResponse(StadiumEntity entity);
    GetStadiumResponse convertToGetResponse(StadiumEntity entity);
    UpdateStadiumResponse convertToUpdateResponse(StadiumEntity entity);
    Stadium convertToDomain(StadiumEntity entity);
}