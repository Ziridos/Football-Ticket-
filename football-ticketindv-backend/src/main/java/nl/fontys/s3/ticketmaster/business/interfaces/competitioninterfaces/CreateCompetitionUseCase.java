package nl.fontys.s3.ticketmaster.business.interfaces.competitioninterfaces;

import nl.fontys.s3.ticketmaster.domain.competition.CreateCompetitionRequest;
import nl.fontys.s3.ticketmaster.domain.competition.CreateCompetitionResponse;

public interface CreateCompetitionUseCase {
    CreateCompetitionResponse createCompetition(CreateCompetitionRequest request);
}