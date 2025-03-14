package nl.fontys.s3.ticketmaster.business.impl.boximpl;

import lombok.AllArgsConstructor;
import nl.fontys.s3.ticketmaster.business.exception.InvalidBoxException;
import nl.fontys.s3.ticketmaster.business.interfaces.boxinterfaces.BoxValidator;
import nl.fontys.s3.ticketmaster.business.interfaces.stadiuminterfaces.StadiumValidator;
import nl.fontys.s3.ticketmaster.domain.box.CreateBoxRequest;
import nl.fontys.s3.ticketmaster.domain.box.UpdateBoxPriceRequest;
import nl.fontys.s3.ticketmaster.persitence.BoxRepository;
import org.springframework.stereotype.Component;

@Component
@AllArgsConstructor
public class BoxValidatorImpl implements BoxValidator {
    private final BoxRepository boxRepository;
    private final StadiumValidator stadiumValidator;

    @Override
    public void validateCreateBoxRequest(CreateBoxRequest request) {
        if (request.getBoxName() == null || request.getBoxName().trim().isEmpty()) {
            throw new InvalidBoxException("Box name cannot be null or empty.");
        }

        if (request.getStadiumId() == null) {
            throw new InvalidBoxException("Stadium ID cannot be null.");
        }

        try {
            stadiumValidator.validateStadiumExists(request.getStadiumId());
        } catch (Exception e) {
            throw new InvalidBoxException("Invalid stadium ID: " + e.getMessage());
        }

        if (request.getWidth() <= 0 || request.getHeight() <= 0) {
            throw new InvalidBoxException("Box dimensions must be positive.");
        }
    }

    @Override
    public void validateUpdateBoxPriceRequest(UpdateBoxPriceRequest request) {
        validateBoxExists(request.getBoxId());
        if (request.getNewPrice() < 0) {
            throw new InvalidBoxException("Box price cannot be negative.");
        }
    }

    @Override
    public void validateBoxExists(Long boxId) {
        if (!boxRepository.existsById(boxId)) {
            throw new InvalidBoxException("Box with the given ID does not exist.");
        }
    }
}