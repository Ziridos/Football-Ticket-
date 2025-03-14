package nl.fontys.s3.ticketmaster.business.interfaces.boxinterfaces;

import nl.fontys.s3.ticketmaster.domain.box.CreateBoxRequest;
import nl.fontys.s3.ticketmaster.domain.box.UpdateBoxPriceRequest;

public interface BoxValidator {
    void validateCreateBoxRequest(CreateBoxRequest request);
    void validateUpdateBoxPriceRequest(UpdateBoxPriceRequest request);
    void validateBoxExists(Long boxId);
}