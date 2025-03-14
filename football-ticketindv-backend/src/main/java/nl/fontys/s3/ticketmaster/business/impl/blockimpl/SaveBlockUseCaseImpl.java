package nl.fontys.s3.ticketmaster.business.impl.blockimpl;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import nl.fontys.s3.ticketmaster.business.interfaces.blockinterfaces.SaveBlockUseCase;
import nl.fontys.s3.ticketmaster.business.interfaces.blockinterfaces.BlockConverter;
import nl.fontys.s3.ticketmaster.business.interfaces.blockinterfaces.BlockValidator;
import nl.fontys.s3.ticketmaster.business.interfaces.boxinterfaces.BoxService;
import nl.fontys.s3.ticketmaster.domain.block.CreateBlockRequest;
import nl.fontys.s3.ticketmaster.domain.block.CreateBlockResponse;
import nl.fontys.s3.ticketmaster.persitence.BlockRepository;
import nl.fontys.s3.ticketmaster.persitence.entity.BlockEntity;
import nl.fontys.s3.ticketmaster.persitence.entity.BoxEntity;
import nl.fontys.s3.ticketmaster.persitence.entity.SeatEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Service
@AllArgsConstructor
public class SaveBlockUseCaseImpl implements SaveBlockUseCase {
    private final BlockRepository blockRepository;
    private final BlockConverter blockConverter;
    private final BlockValidator blockValidator;
    private final BoxService boxService;

    @Override
    @Transactional
    public CreateBlockResponse saveBlock(CreateBlockRequest request) {
        blockValidator.validateCreateBlockRequest(request);
        BlockEntity block = blockConverter.convertToEntity(request);

        if (request.getSeats() != null && !request.getSeats().isEmpty()) {
            for (SeatEntity seat : block.getSeats()) {
                seat.setBlock(block);
            }
        }

        BlockEntity savedBlock = blockRepository.save(block);
        BoxEntity updatedBox = boxService.addBlockToBox(request.getBoxId(), savedBlock);
        blockValidator.validateUpdatedBox(updatedBox, request.getBoxId());

        if (savedBlock.getSeats() != null && !savedBlock.getSeats().isEmpty()) {
            savedBlock.getSeats().forEach(seat ->
                    seat.setSeatNumber(String.valueOf(seat.getId()))
            );
        }

        return blockConverter.convertToCreateBlockResponse(savedBlock);
    }
}