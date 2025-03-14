package nl.fontys.s3.ticketmaster.business.interfaces.competitioninterfaces;

import nl.fontys.s3.ticketmaster.domain.competition.CreateCompetitionRequest;
import nl.fontys.s3.ticketmaster.domain.competition.UpdateCompetitionRequest;

public interface CompetitionValidator {
    void validateCreateCompetitionRequest(CreateCompetitionRequest request);
    void validateUpdateCompetitionRequest(UpdateCompetitionRequest request, Long competitionId);
    void validateCompetitionExists(Long competitionId);
    void validateCompetition(String competitionName);
}
