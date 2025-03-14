package nl.fontys.s3.ticketmaster.business.interfaces.boxinterfaces;

import nl.fontys.s3.ticketmaster.domain.box.CreateBoxRequest;
import nl.fontys.s3.ticketmaster.domain.box.CreateBoxResponse;

public interface SaveBoxUseCase {
    CreateBoxResponse saveBox(CreateBoxRequest request);
}