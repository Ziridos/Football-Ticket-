package nl.fontys.s3.ticketmaster.business.interfaces.blockinterfaces;

import nl.fontys.s3.ticketmaster.domain.block.*;
import nl.fontys.s3.ticketmaster.persitence.entity.BlockEntity;
import nl.fontys.s3.ticketmaster.domain.seat.SeatResponse;
import nl.fontys.s3.ticketmaster.persitence.entity.SeatEntity;

import java.util.List;

public interface BlockConverter {
    BlockEntity convertToEntity(CreateBlockRequest request);
    CreateBlockResponse convertToCreateBlockResponse(BlockEntity blockEntity);
    GetBlockResponse convertToGetBlockResponse(BlockEntity blockEntity);
    List<SeatResponse> convertToSeatResponses(List<SeatEntity> seatEntities);
    SeatResponse convertToSeatResponse(SeatEntity seatEntity);
    BlockDTO toBlockDTO(BlockEntity entity);
}