package nl.fontys.s3.ticketmaster.business.impl.blockimpl;

import lombok.AllArgsConstructor;
import nl.fontys.s3.ticketmaster.business.exception.InvalidBlockException;
import nl.fontys.s3.ticketmaster.business.interfaces.blockinterfaces.BlockValidator;
import nl.fontys.s3.ticketmaster.business.interfaces.boxinterfaces.BoxValidator;
import nl.fontys.s3.ticketmaster.domain.block.CreateBlockRequest;
import nl.fontys.s3.ticketmaster.persitence.BlockRepository;
import nl.fontys.s3.ticketmaster.persitence.entity.BoxEntity;
import org.springframework.stereotype.Component;

@Component
@AllArgsConstructor
public class BlockValidatorImpl implements BlockValidator {
    private final BlockRepository blockRepository;
    private final BoxValidator boxValidator;

    @Override
    public void validateCreateBlockRequest(CreateBlockRequest request) {
        if (request.getBlockName() == null || request.getBlockName().trim().isEmpty()) {
            throw new InvalidBlockException("Block name cannot be null or empty.");
        }
        if (request.getBoxId() == null) {
            throw new InvalidBlockException("Box ID cannot be null.");
        }
        try {
            boxValidator.validateBoxExists(request.getBoxId());
        } catch (Exception e) {
            throw new InvalidBlockException("Invalid box ID: " + e.getMessage());
        }
        if (request.getWidth() <= 0 || request.getHeight() <= 0) {
            throw new InvalidBlockException("Block dimensions must be positive.");
        }
    }

    @Override
    public void validateBlockExists(Long blockId) {
        if (!blockRepository.existsById(blockId)) {
            throw new InvalidBlockException("Block with the given ID does not exist.");
        }
    }

    @Override
    public void validateUpdatedBox(BoxEntity updatedBox, Long boxId) {
        if (updatedBox == null) {
            throw new InvalidBlockException("Failed to add block to box. Box not found with id: " + boxId);
        }
    }
}