package nl.fontys.s3.ticketmaster.business.interfaces.clubinterfaces;

import nl.fontys.s3.ticketmaster.domain.club.UploadLogoRequest;

public interface UploadLogoUseCase {
    void uploadLogo(Long clubId, UploadLogoRequest request);
}