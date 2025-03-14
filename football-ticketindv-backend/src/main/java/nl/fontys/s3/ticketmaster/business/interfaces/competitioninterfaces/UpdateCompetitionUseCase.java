package nl.fontys.s3.ticketmaster.business.interfaces.competitioninterfaces;

import nl.fontys.s3.ticketmaster.domain.competition.UpdateCompetitionRequest;
import nl.fontys.s3.ticketmaster.domain.competition.UpdateCompetitionResponse;

public interface UpdateCompetitionUseCase {
    UpdateCompetitionResponse updateCompetition(UpdateCompetitionRequest request);
}
