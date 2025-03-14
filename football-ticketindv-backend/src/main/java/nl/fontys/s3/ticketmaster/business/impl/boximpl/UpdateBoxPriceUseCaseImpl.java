package nl.fontys.s3.ticketmaster.business.impl.boximpl;

import lombok.AllArgsConstructor;
import nl.fontys.s3.ticketmaster.business.interfaces.boxinterfaces.UpdateBoxPriceUseCase;
import nl.fontys.s3.ticketmaster.business.interfaces.boxinterfaces.BoxConverter;
import nl.fontys.s3.ticketmaster.business.interfaces.boxinterfaces.BoxValidator;
import nl.fontys.s3.ticketmaster.domain.box.UpdateBoxPriceRequest;
import nl.fontys.s3.ticketmaster.domain.box.UpdateBoxPriceResponse;
import nl.fontys.s3.ticketmaster.persitence.BoxRepository;
import nl.fontys.s3.ticketmaster.persitence.entity.BoxEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@AllArgsConstructor
public class UpdateBoxPriceUseCaseImpl implements UpdateBoxPriceUseCase {
    private final BoxRepository boxRepository;
    private final BoxConverter boxConverter;
    private final BoxValidator boxValidator;

    @Override
    @Transactional
    public UpdateBoxPriceResponse updateBoxPrice(UpdateBoxPriceRequest request) {
        boxValidator.validateUpdateBoxPriceRequest(request);
        BoxEntity updatedBox = boxRepository.updatePriceWithSeats(request.getBoxId(), request.getNewPrice());
        return boxConverter.convertToUpdateBoxPriceResponse(updatedBox);
    }
}