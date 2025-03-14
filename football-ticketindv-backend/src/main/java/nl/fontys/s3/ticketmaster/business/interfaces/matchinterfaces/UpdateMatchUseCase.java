package nl.fontys.s3.ticketmaster.business.interfaces.matchinterfaces;

import nl.fontys.s3.ticketmaster.domain.match.UpdateMatchRequest;
import nl.fontys.s3.ticketmaster.domain.match.UpdateMatchResponse;

public interface UpdateMatchUseCase {
    UpdateMatchResponse updateMatch(UpdateMatchRequest request, Long matchId);
}
