package nl.fontys.s3.ticketmaster.business.interfaces.blockinterfaces;

import nl.fontys.s3.ticketmaster.persitence.entity.BlockEntity;

import java.util.List;
import java.util.Optional;

public interface BlockService {
    BlockEntity save(BlockEntity block);
    Optional<BlockEntity> findById(Long blockId);
    List<BlockEntity> findByBoxId(Long boxId);
    boolean existsByBlockNameAndBoxId(String blockName, Long boxId);
    void deleteById(Long blockId);
    boolean existsById(Long id);
}
