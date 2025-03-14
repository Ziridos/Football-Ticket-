package nl.fontys.s3.ticketmaster.business.impl.blockimpl;

import lombok.AllArgsConstructor;
import nl.fontys.s3.ticketmaster.business.interfaces.blockinterfaces.BlockService;
import nl.fontys.s3.ticketmaster.persitence.BlockRepository;
import nl.fontys.s3.ticketmaster.persitence.entity.BlockEntity;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@AllArgsConstructor
public class BlockServiceImpl implements BlockService {
    private final BlockRepository blockRepository;

    @Override
    public BlockEntity save(BlockEntity block) {
        return blockRepository.save(block);
    }

    @Override
    public Optional<BlockEntity> findById(Long blockId) {
        return blockRepository.findById(blockId);
    }

    @Override
    public List<BlockEntity> findByBoxId(Long boxId) {
        return blockRepository.findByBoxId(boxId);
    }

    @Override
    public boolean existsByBlockNameAndBoxId(String blockName, Long boxId) {
        return blockRepository.existsByBlockNameAndBoxId(blockName, boxId);
    }

    @Override
    public void deleteById(Long blockId) {
        blockRepository.deleteById(blockId);
    }

    @Override
    public boolean existsById(Long id) {
        return blockRepository.existsById(id);
    }
}