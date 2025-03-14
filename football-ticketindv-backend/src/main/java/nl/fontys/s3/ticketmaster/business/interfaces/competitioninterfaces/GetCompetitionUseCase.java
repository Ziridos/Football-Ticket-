package nl.fontys.s3.ticketmaster.business.interfaces.competitioninterfaces;

import nl.fontys.s3.ticketmaster.domain.competition.GetCompetitionRequest;
import nl.fontys.s3.ticketmaster.domain.competition.GetCompetitionResponse;

public interface GetCompetitionUseCase {
    GetCompetitionResponse getCompetition(GetCompetitionRequest request);
}
