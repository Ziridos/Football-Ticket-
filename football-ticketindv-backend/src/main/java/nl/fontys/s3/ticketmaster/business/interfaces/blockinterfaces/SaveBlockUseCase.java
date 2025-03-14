package nl.fontys.s3.ticketmaster.business.interfaces.blockinterfaces;

import nl.fontys.s3.ticketmaster.domain.block.CreateBlockRequest;
import nl.fontys.s3.ticketmaster.domain.block.CreateBlockResponse;

public interface SaveBlockUseCase {
    CreateBlockResponse saveBlock(CreateBlockRequest request);
}