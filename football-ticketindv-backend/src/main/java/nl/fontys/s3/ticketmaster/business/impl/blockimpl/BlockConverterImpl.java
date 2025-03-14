package nl.fontys.s3.ticketmaster.business.impl.blockimpl;

import nl.fontys.s3.ticketmaster.business.interfaces.blockinterfaces.BlockConverter;
import nl.fontys.s3.ticketmaster.domain.block.*;
import nl.fontys.s3.ticketmaster.domain.seat.CreateSeatRequest;
import nl.fontys.s3.ticketmaster.domain.seat.SeatResponse;
import nl.fontys.s3.ticketmaster.persitence.entity.BlockEntity;
import nl.fontys.s3.ticketmaster.persitence.entity.BoxEntity;
import nl.fontys.s3.ticketmaster.persitence.entity.SeatEntity;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
public class BlockConverterImpl implements BlockConverter {

    @Override
    public BlockEntity convertToEntity(CreateBlockRequest request) {
        List<SeatEntity> seatEntities = mapSeats(request.getSeats());
        return BlockEntity.builder()
                .blockName(request.getBlockName())
                .box(BoxEntity.builder().id(request.getBoxId()).build())
                .xPosition(request.getXPosition())
                .yPosition(request.getYPosition())
                .height(request.getHeight())
                .width(request.getWidth())
                .seats(seatEntities)
                .build();
    }

    @Override
    public CreateBlockResponse convertToCreateBlockResponse(BlockEntity blockEntity) {
        return CreateBlockResponse.builder()
                .blockId(blockEntity.getId())
                .blockName(blockEntity.getBlockName())
                .xPosition(blockEntity.getXPosition())
                .yPosition(blockEntity.getYPosition())
                .width(blockEntity.getWidth())
                .height(blockEntity.getHeight())
                .seats(convertToSeatResponses(blockEntity.getSeats()))
                .build();
    }

    @Override
    public GetBlockResponse convertToGetBlockResponse(BlockEntity blockEntity) {
        return GetBlockResponse.builder()
                .blockId(blockEntity.getId())
                .blockName(blockEntity.getBlockName())
                .boxId(blockEntity.getBox().getId())
                .xPosition(blockEntity.getXPosition())
                .yPosition(blockEntity.getYPosition())
                .width(blockEntity.getWidth())
                .height(blockEntity.getHeight())
                .seats(convertToSeatResponses(blockEntity.getSeats()))
                .build();
    }

    @Override
    public List<SeatResponse> convertToSeatResponses(List<SeatEntity> seatEntities) {
        return new ArrayList<>(seatEntities.stream()
                .map(this::convertToSeatResponse)
                .toList());
    }

    @Override
    public SeatResponse convertToSeatResponse(SeatEntity seatEntity) {
        return SeatResponse.builder()
                .seatId(seatEntity.getId())
                .seatNumber(seatEntity.getSeatNumber())
                .xPosition(seatEntity.getXPosition())
                .yPosition(seatEntity.getYPosition())
                .price(seatEntity.getPrice())
                .build();
    }

    private List<SeatEntity> mapSeats(List<CreateSeatRequest> seatRequests) {
        if (seatRequests == null || seatRequests.isEmpty()) {
            return new ArrayList<>();
        }
        return new ArrayList<>(seatRequests.stream()
                .map(seatRequest -> SeatEntity.builder()
                        .seatNumber(seatRequest.getSeatNumber())
                        .xPosition(seatRequest.getXPosition())
                        .yPosition(seatRequest.getYPosition())
                        .build())
                .toList());
    }

    @Override
    public BlockDTO toBlockDTO(BlockEntity entity) {
        return BlockDTO.builder()
                .blockId(entity.getId())
                .blockName(entity.getBlockName())
                .build();
    }
}