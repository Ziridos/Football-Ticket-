package nl.fontys.s3.ticketmaster.business.impl.blockimpl;

import nl.fontys.s3.ticketmaster.persitence.BlockRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
class DeleteBlockUseCaseImplTest {
    @Mock
    private BlockRepository blockRepository;

    @InjectMocks
    private DeleteBlockUseCaseImpl deleteBlockUseCase;

    @Test
    void deleteBlock_CallsRepository() {
        long blockId = 1L;
        deleteBlockUseCase.deleteBlock(blockId);
        verify(blockRepository).deleteById(blockId);
    }
}