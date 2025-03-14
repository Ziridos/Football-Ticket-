package nl.fontys.s3.ticketmaster.business.impl.competitionimpl;

import lombok.AllArgsConstructor;
import nl.fontys.s3.ticketmaster.business.interfaces.competitioninterfaces.GetCompetitionUseCase;
import nl.fontys.s3.ticketmaster.business.interfaces.competitioninterfaces.CompetitionConverter;
import nl.fontys.s3.ticketmaster.business.interfaces.competitioninterfaces.CompetitionValidator;
import nl.fontys.s3.ticketmaster.domain.competition.GetCompetitionRequest;
import nl.fontys.s3.ticketmaster.domain.competition.GetCompetitionResponse;
import nl.fontys.s3.ticketmaster.persitence.CompetitionRepository;
import nl.fontys.s3.ticketmaster.persitence.entity.CompetitionEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


@Service
@AllArgsConstructor
public class GetCompetitionUseCaseImpl implements GetCompetitionUseCase {
    private final CompetitionRepository competitionRepository;
    private final CompetitionConverter competitionConverter;
    private final CompetitionValidator competitionValidator;

    @Override
    @Transactional
    public GetCompetitionResponse getCompetition(GetCompetitionRequest request) {
        competitionValidator.validateCompetitionExists(request.getCompetitionId());
        CompetitionEntity competitionEntity = competitionRepository.findById(request.getCompetitionId())
                .orElseThrow(() -> new RuntimeException("Competition not found with id: " + request.getCompetitionId()));
        return competitionConverter.convertToGetCompetitionResponse(competitionEntity);
    }
}