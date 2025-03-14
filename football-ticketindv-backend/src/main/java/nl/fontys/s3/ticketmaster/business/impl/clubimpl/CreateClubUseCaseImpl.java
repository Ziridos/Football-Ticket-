package nl.fontys.s3.ticketmaster.business.impl.clubimpl;

import lombok.AllArgsConstructor;
import nl.fontys.s3.ticketmaster.business.interfaces.clubinterfaces.ClubConverter;
import nl.fontys.s3.ticketmaster.business.interfaces.clubinterfaces.ClubValidator;
import nl.fontys.s3.ticketmaster.business.interfaces.clubinterfaces.CreateClubUseCase;
import nl.fontys.s3.ticketmaster.domain.club.CreateClubRequest;
import nl.fontys.s3.ticketmaster.domain.club.CreateClubResponse;
import nl.fontys.s3.ticketmaster.persitence.ClubRepository;
import nl.fontys.s3.ticketmaster.persitence.entity.ClubEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@AllArgsConstructor
public class CreateClubUseCaseImpl implements CreateClubUseCase {
    private final ClubRepository clubRepository;
    private final ClubConverter clubConverter;
    private final ClubValidator clubValidator;

    @Override
    @Transactional
    public CreateClubResponse createClub(CreateClubRequest request) {
        clubValidator.validateCreateClubRequest(request);
        ClubEntity newClub = clubConverter.convertToEntity(request);
        ClubEntity savedClub = clubRepository.save(newClub);
        return clubConverter.convertToCreateClubResponse(savedClub);
    }
}
