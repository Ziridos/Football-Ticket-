package nl.fontys.s3.ticketmaster.business.impl.boximpl;

import nl.fontys.s3.ticketmaster.business.interfaces.boxinterfaces.BoxConverter;
import nl.fontys.s3.ticketmaster.domain.box.*;
import nl.fontys.s3.ticketmaster.persitence.entity.BoxEntity;
import nl.fontys.s3.ticketmaster.persitence.entity.BlockEntity;
import nl.fontys.s3.ticketmaster.persitence.entity.StadiumEntity;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;


@Component
public class BoxConverterImpl implements BoxConverter {

    @Override
    public BoxEntity convertToEntity(CreateBoxRequest request) {
        List<BlockEntity> blockEntities = new ArrayList<>();
        if (request.getBlocks() != null) {
            blockEntities = request.getBlocks().stream()
                    .map(block -> BlockEntity.builder()
                            .blockName(block.getBlockName())
                            .xPosition(block.getXPosition())
                            .yPosition(block.getYPosition())
                            .width(block.getWidth())
                            .height(block.getHeight())
                            .seats(new ArrayList<>())
                            .build())
                    .toList();
        }

        return BoxEntity.builder()
                .boxName(request.getBoxName())
                .xPosition(request.getXPosition())
                .yPosition(request.getYPosition())
                .width(request.getWidth())
                .height(request.getHeight())
                .stadium(StadiumEntity.builder().id(request.getStadiumId()).build())
                .blocks(blockEntities)
                .build();
    }

    @Override
    public CreateBoxResponse convertToCreateBoxResponse(BoxEntity boxEntity) {
        return CreateBoxResponse.builder()
                .boxId(boxEntity.getId())
                .boxName(boxEntity.getBoxName())
                .xPosition(boxEntity.getXPosition())
                .yPosition(boxEntity.getYPosition())
                .width(boxEntity.getWidth())
                .height(boxEntity.getHeight())
                .blocks(new ArrayList<>())
                .build();
    }

    @Override
    public GetBoxResponse convertToGetBoxResponse(BoxEntity boxEntity) {
        return GetBoxResponse.builder()
                .boxId(boxEntity.getId())
                .boxName(boxEntity.getBoxName())
                .xPosition(boxEntity.getXPosition())
                .yPosition(boxEntity.getYPosition())
                .width(boxEntity.getWidth())
                .height(boxEntity.getHeight())
                .stadiumId(boxEntity.getStadium().getId())
                .price(boxEntity.getPrice())
                .build();
    }

    @Override
    public UpdateBoxPriceResponse convertToUpdateBoxPriceResponse(BoxEntity boxEntity) {
        return UpdateBoxPriceResponse.builder()
                .boxId(boxEntity.getId())
                .updatedPrice(boxEntity.getPrice())
                .build();
    }

    @Override
    public BoxDTO toBoxDTO(BoxEntity entity) {
        return BoxDTO.builder()
                .boxId(entity.getId())
                .boxName(entity.getBoxName())
                .build();
    }
}