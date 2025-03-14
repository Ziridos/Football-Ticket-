package nl.fontys.s3.ticketmaster.business.impl.blockimpl;

import lombok.AllArgsConstructor;
import nl.fontys.s3.ticketmaster.business.interfaces.blockinterfaces.DeleteBlockUseCase;
import nl.fontys.s3.ticketmaster.persitence.BlockRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


@Service
@AllArgsConstructor
public class DeleteBlockUseCaseImpl implements DeleteBlockUseCase {
    private final BlockRepository blockRepository;

    @Override
    @Transactional
    public void deleteBlock(long blockId)
    {
        blockRepository.deleteById(blockId);
    }
}
