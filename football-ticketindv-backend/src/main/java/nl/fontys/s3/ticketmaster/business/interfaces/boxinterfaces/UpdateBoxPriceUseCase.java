package nl.fontys.s3.ticketmaster.business.interfaces.boxinterfaces;

import nl.fontys.s3.ticketmaster.domain.box.UpdateBoxPriceRequest;
import nl.fontys.s3.ticketmaster.domain.box.UpdateBoxPriceResponse;

public interface UpdateBoxPriceUseCase {
    UpdateBoxPriceResponse updateBoxPrice(UpdateBoxPriceRequest request);
}