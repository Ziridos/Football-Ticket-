package nl.fontys.s3.ticketmaster.business.interfaces.matchinterfaces;

import nl.fontys.s3.ticketmaster.domain.match.CreateMatchRequest;
import nl.fontys.s3.ticketmaster.domain.match.UpdateMatchRequest;
import nl.fontys.s3.ticketmaster.persitence.entity.MatchEntity;

public interface MatchValidator {
    void validateCreateMatchRequest(CreateMatchRequest request);
    void validateUpdateMatchRequest(UpdateMatchRequest request, Long matchId);
    void validateMatchExists(Long matchId);
    MatchEntity validateAndGetMatch(Long matchId);
}