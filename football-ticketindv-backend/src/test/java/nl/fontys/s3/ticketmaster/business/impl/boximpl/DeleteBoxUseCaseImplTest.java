package nl.fontys.s3.ticketmaster.business.impl.boximpl;

import nl.fontys.s3.ticketmaster.persitence.BoxRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
class DeleteBoxUseCaseImplTest {
    @Mock
    private BoxRepository boxRepository;

    @InjectMocks
    private DeleteBoxUseCaseImpl deleteBoxUseCase;

    @Test
    void deleteBox_CallsRepository() {
        long boxId = 1L;
        deleteBoxUseCase.deleteBox(boxId);
        verify(boxRepository).deleteById(boxId);
    }
}