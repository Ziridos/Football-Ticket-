package nl.fontys.s3.ticketmaster.business.impl.boximpl;

import lombok.AllArgsConstructor;
import nl.fontys.s3.ticketmaster.business.interfaces.boxinterfaces.DeleteBoxUseCase;
import nl.fontys.s3.ticketmaster.persitence.BoxRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@AllArgsConstructor
public class DeleteBoxUseCaseImpl implements DeleteBoxUseCase {
    private final BoxRepository boxRepository;

    @Override
    @Transactional
    public void deleteBox(long boxId)
    {
        boxRepository.deleteById(boxId);
    }
}
