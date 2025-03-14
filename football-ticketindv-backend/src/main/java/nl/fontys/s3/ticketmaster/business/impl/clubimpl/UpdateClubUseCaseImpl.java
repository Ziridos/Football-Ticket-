package nl.fontys.s3.ticketmaster.business.impl.clubimpl;

import lombok.AllArgsConstructor;
import nl.fontys.s3.ticketmaster.business.exception.InvalidClubException;
import nl.fontys.s3.ticketmaster.business.interfaces.clubinterfaces.ClubConverter;
import nl.fontys.s3.ticketmaster.business.interfaces.clubinterfaces.ClubValidator;
import nl.fontys.s3.ticketmaster.business.interfaces.clubinterfaces.UpdateClubUseCase;
import nl.fontys.s3.ticketmaster.domain.club.UpdateClubRequest;
import nl.fontys.s3.ticketmaster.domain.club.UpdateClubResponse;
import nl.fontys.s3.ticketmaster.persitence.ClubRepository;
import nl.fontys.s3.ticketmaster.persitence.entity.ClubEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


@Service
@AllArgsConstructor
public class UpdateClubUseCaseImpl implements UpdateClubUseCase {
    private final ClubRepository clubRepository;
    private final ClubConverter clubConverter;
    private final ClubValidator clubValidator;

    @Override
    @Transactional
    public UpdateClubResponse updateClub(Long clubId, UpdateClubRequest request) {
        clubValidator.validateUpdateClubRequest(request, clubId);
        ClubEntity clubEntity = clubRepository.findById(clubId)
                .orElseThrow(() -> new InvalidClubException("Club not found with id: " + clubId));
        ClubEntity updatedClub = clubConverter.updateEntityFromRequest(clubEntity, request);
        ClubEntity savedClub = clubRepository.save(updatedClub);
        return clubConverter.convertToUpdateClubResponse(savedClub);
    }
}
