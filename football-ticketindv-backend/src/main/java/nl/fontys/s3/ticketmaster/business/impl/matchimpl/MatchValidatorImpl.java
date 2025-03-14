package nl.fontys.s3.ticketmaster.business.impl.matchimpl;

import lombok.AllArgsConstructor;
import nl.fontys.s3.ticketmaster.business.exception.InvalidMatchException;
import nl.fontys.s3.ticketmaster.business.interfaces.clubinterfaces.ClubValidator;
import nl.fontys.s3.ticketmaster.business.interfaces.competitioninterfaces.CompetitionValidator;
import nl.fontys.s3.ticketmaster.business.interfaces.matchinterfaces.MatchValidator;
import nl.fontys.s3.ticketmaster.domain.match.CreateMatchRequest;
import nl.fontys.s3.ticketmaster.domain.match.UpdateMatchRequest;
import nl.fontys.s3.ticketmaster.persitence.MatchRepository;
import nl.fontys.s3.ticketmaster.persitence.entity.MatchEntity;
import org.springframework.stereotype.Component;

@Component
@AllArgsConstructor
public class MatchValidatorImpl implements MatchValidator {
    private final MatchRepository matchRepository;
    private final ClubValidator clubValidator;
    private final CompetitionValidator competitionValidator;

    @Override
    public void validateCreateMatchRequest(CreateMatchRequest request) {
        clubValidator.validateClubs(request.getHomeClubName(), request.getAwayClubName());
        competitionValidator.validateCompetition(request.getCompetitionName());
    }

    @Override
    public void validateUpdateMatchRequest(UpdateMatchRequest request, Long matchId) {
        validateMatchExists(matchId);
        clubValidator.validateClubs(request.getHomeClubName(), request.getAwayClubName());
        competitionValidator.validateCompetition(request.getCompetitionName());
    }

    @Override
    public void validateMatchExists(Long matchId) {
        if (!matchRepository.existsById(matchId)) {
            throw new InvalidMatchException("Match with the given ID does not exist.");
        }
    }

    @Override
    public MatchEntity validateAndGetMatch(Long matchId) {
        return matchRepository.findById(matchId)
                .orElseThrow(() -> new InvalidMatchException("Match with the given ID does not exist."));
    }
}