package nl.fontys.s3.ticketmaster.business.impl.clubimpl;

import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import nl.fontys.s3.ticketmaster.business.exception.InvalidClubException;
import nl.fontys.s3.ticketmaster.business.exception.LogoUploadException;
import nl.fontys.s3.ticketmaster.business.interfaces.clubinterfaces.ClubService;
import nl.fontys.s3.ticketmaster.business.interfaces.clubinterfaces.ClubValidator;
import nl.fontys.s3.ticketmaster.business.interfaces.clubinterfaces.UploadLogoUseCase;
import nl.fontys.s3.ticketmaster.domain.club.UploadLogoRequest;
import nl.fontys.s3.ticketmaster.persitence.entity.ClubEntity;
import org.springframework.stereotype.Service;

import java.io.IOException;

@Service
@AllArgsConstructor
public class UploadLogoUseCaseImpl implements UploadLogoUseCase {
    private final ClubService clubService;
    private final ClubValidator clubValidator;

    @Override
    @Transactional
    public void uploadLogo(Long clubId, UploadLogoRequest request) {
        try {
            clubValidator.validateClubExists(clubId);
            ClubEntity club = clubService.findById(clubId)
                    .orElseThrow(() -> new InvalidClubException("Club not found"));

            club.setLogo(request.getFile().getBytes());
            club.setLogoContentType(request.getFile().getContentType());
            clubService.save(club);
        } catch (IOException e) {
            throw new LogoUploadException("Failed to process and store club logo", e);
        }
    }
}