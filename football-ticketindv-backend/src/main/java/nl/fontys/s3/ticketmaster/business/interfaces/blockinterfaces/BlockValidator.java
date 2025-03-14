package nl.fontys.s3.ticketmaster.business.interfaces.blockinterfaces;

import nl.fontys.s3.ticketmaster.domain.block.CreateBlockRequest;
import nl.fontys.s3.ticketmaster.persitence.entity.BoxEntity;

public interface BlockValidator {
    void validateCreateBlockRequest(CreateBlockRequest request);
    void validateBlockExists(Long blockId);
    void validateUpdatedBox(BoxEntity updatedBox, Long boxId);

}