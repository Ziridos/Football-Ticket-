package nl.fontys.s3.ticketmaster.business.interfaces.matchinterfaces;

import nl.fontys.s3.ticketmaster.domain.match.GetMatchRequest;
import nl.fontys.s3.ticketmaster.domain.match.GetMatchResponse;

public interface GetMatchUseCase {
    GetMatchResponse getMatch(GetMatchRequest request);
}
