package nl.fontys.s3.ticketmaster.business.impl.clubimpl;

import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import nl.fontys.s3.ticketmaster.business.exception.InvalidClubException;
import nl.fontys.s3.ticketmaster.business.interfaces.clubinterfaces.ClubService;
import nl.fontys.s3.ticketmaster.business.interfaces.clubinterfaces.ClubValidator;
import nl.fontys.s3.ticketmaster.business.interfaces.clubinterfaces.DeleteLogoUseCase;
import nl.fontys.s3.ticketmaster.persitence.entity.ClubEntity;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class DeleteLogoUseCaseImpl implements DeleteLogoUseCase {
    private final ClubService clubService;
    private final ClubValidator clubValidator;

    @Override
    @Transactional
    public void deleteLogo(Long clubId) {
        clubValidator.validateClubExists(clubId);
        ClubEntity club = clubService.findById(clubId)
                .orElseThrow(() -> new InvalidClubException("Club not found"));

        club.setLogo(null);
        club.setLogoContentType(null);
        clubService.save(club);
    }
}