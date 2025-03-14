package nl.fontys.s3.ticketmaster.business.impl.clubimpl;

import lombok.AllArgsConstructor;
import nl.fontys.s3.ticketmaster.business.exception.InvalidClubException;
import nl.fontys.s3.ticketmaster.business.interfaces.clubinterfaces.ClubConverter;
import nl.fontys.s3.ticketmaster.business.interfaces.clubinterfaces.ClubValidator;
import nl.fontys.s3.ticketmaster.business.interfaces.clubinterfaces.GetClubUseCase;
import nl.fontys.s3.ticketmaster.domain.club.GetClubRequest;
import nl.fontys.s3.ticketmaster.domain.club.GetClubResponse;
import nl.fontys.s3.ticketmaster.persitence.ClubRepository;
import nl.fontys.s3.ticketmaster.persitence.entity.ClubEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


@Service
@AllArgsConstructor
public class GetClubUseCaseImpl implements GetClubUseCase {
    private final ClubRepository clubRepository;
    private final ClubConverter clubConverter;
    private final ClubValidator clubValidator;

    @Override
    @Transactional
    public GetClubResponse getClub(GetClubRequest request) {
        clubValidator.validateClubExists(request.getClubId());
        ClubEntity clubEntity = clubRepository.findById(request.getClubId())
                .orElseThrow(() -> new InvalidClubException("Club not found with id: " + request.getClubId()));
        return clubConverter.convertToGetClubResponse(clubEntity);
    }
}
