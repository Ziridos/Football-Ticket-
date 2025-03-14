package nl.fontys.s3.ticketmaster.business.impl.clubimpl;

import lombok.AllArgsConstructor;
import nl.fontys.s3.ticketmaster.business.interfaces.clubinterfaces.ClubValidator;
import nl.fontys.s3.ticketmaster.business.interfaces.clubinterfaces.DeleteClubUseCase;
import nl.fontys.s3.ticketmaster.persitence.ClubRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@AllArgsConstructor
public class DeleteClubUseCaseImpl implements DeleteClubUseCase {
    private final ClubRepository clubRepository;
    private final ClubValidator clubValidator;

    @Override
    @Transactional
    public void deleteClub(Long clubId) {
        clubValidator.validateClubExists(clubId);
        clubRepository.deleteById(clubId);
    }
}
