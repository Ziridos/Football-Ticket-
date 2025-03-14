package nl.fontys.s3.ticketmaster.business.interfaces.matchinterfaces;

import nl.fontys.s3.ticketmaster.domain.match.CreateMatchRequest;
import nl.fontys.s3.ticketmaster.domain.match.CreateMatchResponse;

public interface CreateMatchUseCase {
    CreateMatchResponse createMatch(CreateMatchRequest request);
}
