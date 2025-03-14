package nl.fontys.s3.ticketmaster.business.impl.competitionimpl;

import lombok.AllArgsConstructor;
import nl.fontys.s3.ticketmaster.business.interfaces.competitioninterfaces.CompetitionConverter;
import nl.fontys.s3.ticketmaster.domain.competition.*;
import nl.fontys.s3.ticketmaster.persitence.entity.CompetitionEntity;
import org.springframework.stereotype.Component;

@Component
@AllArgsConstructor
public class CompetitionConverterImpl implements CompetitionConverter {

    @Override
    public CompetitionEntity convertToEntity(CreateCompetitionRequest request) {
        return CompetitionEntity.builder()
                .competitionName(request.getCompetitionName())
                .build();
    }

    @Override
    public CreateCompetitionResponse convertToCreateCompetitionResponse(CompetitionEntity competitionEntity) {
        return CreateCompetitionResponse.builder()
                .competitionId(competitionEntity.getId())
                .competitionName(competitionEntity.getCompetitionName())
                .build();
    }

    @Override
    public GetCompetitionResponse convertToGetCompetitionResponse(CompetitionEntity competitionEntity) {
        return GetCompetitionResponse.builder()
                .competitionId(competitionEntity.getId())
                .competitionName(competitionEntity.getCompetitionName())
                .build();
    }

    @Override
    public UpdateCompetitionResponse convertToUpdateCompetitionResponse(CompetitionEntity competitionEntity) {
        return UpdateCompetitionResponse.builder()
                .competitionId(competitionEntity.getId())
                .competitionName(competitionEntity.getCompetitionName())
                .build();
    }

    @Override
    public Competition convertToCompetition(CompetitionEntity competitionEntity) {
        return Competition.builder()
                .competitionId(competitionEntity.getId())
                .competitionName(competitionEntity.getCompetitionName())
                .build();
    }

    @Override
    public CompetitionEntity updateEntityFromRequest(CompetitionEntity competition, UpdateCompetitionRequest request) {
        competition.setCompetitionName(request.getCompetitionName());
        return competition;
    }
}

