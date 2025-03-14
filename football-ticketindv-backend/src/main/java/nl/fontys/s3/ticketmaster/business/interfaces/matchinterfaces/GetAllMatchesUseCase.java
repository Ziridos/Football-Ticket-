package nl.fontys.s3.ticketmaster.business.interfaces.matchinterfaces;

import nl.fontys.s3.ticketmaster.domain.match.GetAllMatchesRequest;
import nl.fontys.s3.ticketmaster.domain.match.GetAllMatchesResponse;

public interface GetAllMatchesUseCase {
    GetAllMatchesResponse getAllMatches(GetAllMatchesRequest request);
}
