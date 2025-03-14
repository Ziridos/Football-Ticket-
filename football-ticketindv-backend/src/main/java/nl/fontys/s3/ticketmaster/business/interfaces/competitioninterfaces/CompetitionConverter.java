package nl.fontys.s3.ticketmaster.business.interfaces.competitioninterfaces;

import nl.fontys.s3.ticketmaster.domain.competition.*;
import nl.fontys.s3.ticketmaster.persitence.entity.CompetitionEntity;

public interface CompetitionConverter {
    CompetitionEntity convertToEntity(CreateCompetitionRequest request);
    CreateCompetitionResponse convertToCreateCompetitionResponse(CompetitionEntity competitionEntity);
    GetCompetitionResponse convertToGetCompetitionResponse(CompetitionEntity competitionEntity);
    UpdateCompetitionResponse convertToUpdateCompetitionResponse(CompetitionEntity competitionEntity);
    Competition convertToCompetition(CompetitionEntity competitionEntity);
    CompetitionEntity updateEntityFromRequest(CompetitionEntity competition, UpdateCompetitionRequest request);
}
