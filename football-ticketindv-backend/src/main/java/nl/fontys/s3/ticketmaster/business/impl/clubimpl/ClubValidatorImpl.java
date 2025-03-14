package nl.fontys.s3.ticketmaster.business.impl.clubimpl;

import lombok.AllArgsConstructor;
import nl.fontys.s3.ticketmaster.business.exception.InvalidClubException;
import nl.fontys.s3.ticketmaster.business.exception.SameClubException;
import nl.fontys.s3.ticketmaster.business.interfaces.clubinterfaces.ClubValidator;
import nl.fontys.s3.ticketmaster.business.interfaces.clubinterfaces.ClubService;
import nl.fontys.s3.ticketmaster.business.interfaces.stadiuminterfaces.StadiumValidator;
import nl.fontys.s3.ticketmaster.domain.club.CreateClubRequest;
import nl.fontys.s3.ticketmaster.domain.club.UpdateClubRequest;
import nl.fontys.s3.ticketmaster.persitence.entity.ClubEntity;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
@AllArgsConstructor
public class ClubValidatorImpl implements ClubValidator {
    private final ClubService clubService;
    private final StadiumValidator stadiumValidator;

    @Override
    public void validateCreateClubRequest(CreateClubRequest request) {
        if (request.getClubName() == null || request.getClubName().trim().isEmpty()) {
            throw new InvalidClubException("Club name cannot be null or empty.");
        }
        if (clubService.existsByName(request.getClubName())) {
            throw new InvalidClubException("Club with the given name already exists.");
        }
        stadiumValidator.validateStadiumExists(request.getStadiumId());
    }

    @Override
    public void validateUpdateClubRequest(UpdateClubRequest request, Long clubId) {
        validateClubExists(clubId);
        if (request.getClubName() == null || request.getClubName().trim().isEmpty()) {
            throw new InvalidClubException("Club name cannot be null or empty.");
        }
        stadiumValidator.validateStadiumExists(request.getStadiumId());
    }

    @Override
    public void validateClubExists(Long clubId) {
        if (!clubService.existsById(clubId)) {
            throw new InvalidClubException("Club with the given ID does not exist.");
        }
    }

    public void validateClubs(String homeClubName, String awayClubName) {
        Optional<ClubEntity> homeClub = Optional.ofNullable(clubService.findByClubName(homeClubName))
                .orElse(Optional.empty());
        if (homeClub.isEmpty()) {
            throw new InvalidClubException("Home club with the given name does not exist.");
        }

        Optional<ClubEntity> awayClub = Optional.ofNullable(clubService.findByClubName(awayClubName))
                .orElse(Optional.empty());
        if (awayClub.isEmpty()) {
            throw new InvalidClubException("Away club with the given name does not exist.");
        }

        if (homeClub.get().equals(awayClub.get())) {
            throw new SameClubException("Home club and away club cannot be the same.");
        }
    }
}