package nl.fontys.s3.ticketmaster.business.impl.boximpl;

import lombok.AllArgsConstructor;
import nl.fontys.s3.ticketmaster.business.interfaces.boxinterfaces.SaveBoxUseCase;
import nl.fontys.s3.ticketmaster.business.interfaces.boxinterfaces.BoxConverter;
import nl.fontys.s3.ticketmaster.business.interfaces.boxinterfaces.BoxValidator;
import nl.fontys.s3.ticketmaster.domain.box.CreateBoxRequest;
import nl.fontys.s3.ticketmaster.domain.box.CreateBoxResponse;
import nl.fontys.s3.ticketmaster.persitence.BoxRepository;
import nl.fontys.s3.ticketmaster.persitence.entity.BoxEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@AllArgsConstructor
public class SaveBoxUseCaseImpl implements SaveBoxUseCase {
    private final BoxRepository boxRepository;
    private final BoxConverter boxConverter;
    private final BoxValidator boxValidator;

    @Override
    @Transactional
    public CreateBoxResponse saveBox(CreateBoxRequest request) {
        boxValidator.validateCreateBoxRequest(request);
        BoxEntity box = boxConverter.convertToEntity(request);
        BoxEntity savedBox = boxRepository.save(box);
        return boxConverter.convertToCreateBoxResponse(savedBox);
    }
}