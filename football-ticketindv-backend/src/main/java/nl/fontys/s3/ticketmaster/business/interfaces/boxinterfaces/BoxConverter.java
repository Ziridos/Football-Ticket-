package nl.fontys.s3.ticketmaster.business.interfaces.boxinterfaces;

import nl.fontys.s3.ticketmaster.domain.box.*;
import nl.fontys.s3.ticketmaster.persitence.entity.BoxEntity;

public interface BoxConverter {
    BoxEntity convertToEntity(CreateBoxRequest request);
    CreateBoxResponse convertToCreateBoxResponse(BoxEntity boxEntity);
    GetBoxResponse convertToGetBoxResponse(BoxEntity boxEntity);
    UpdateBoxPriceResponse convertToUpdateBoxPriceResponse(BoxEntity boxEntity);
    BoxDTO toBoxDTO(BoxEntity entity);
}