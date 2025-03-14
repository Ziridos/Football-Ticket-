package nl.fontys.s3.ticketmaster.business.interfaces.competitioninterfaces;

import nl.fontys.s3.ticketmaster.domain.competition.GetAllCompetitionsRequest;
import nl.fontys.s3.ticketmaster.domain.competition.GetAllCompetitionsResponse;

public interface GetAllCompetitionsUseCase {
    GetAllCompetitionsResponse getAllCompetitions(GetAllCompetitionsRequest request);
}
