package nl.fontys.s3.ticketmaster.business.impl.competitionimpl;

import lombok.AllArgsConstructor;
import nl.fontys.s3.ticketmaster.business.interfaces.competitioninterfaces.CreateCompetitionUseCase;
import nl.fontys.s3.ticketmaster.business.interfaces.competitioninterfaces.CompetitionConverter;
import nl.fontys.s3.ticketmaster.business.interfaces.competitioninterfaces.CompetitionValidator;
import nl.fontys.s3.ticketmaster.domain.competition.CreateCompetitionRequest;
import nl.fontys.s3.ticketmaster.domain.competition.CreateCompetitionResponse;
import nl.fontys.s3.ticketmaster.persitence.CompetitionRepository;
import nl.fontys.s3.ticketmaster.persitence.entity.CompetitionEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@AllArgsConstructor
public class CreateCompetitionUseCaseImpl implements CreateCompetitionUseCase {
    private final CompetitionRepository competitionRepository;
    private final CompetitionConverter competitionConverter;
    private final CompetitionValidator competitionValidator;

    @Override
    @Transactional
    public CreateCompetitionResponse createCompetition(CreateCompetitionRequest request) {
        competitionValidator.validateCreateCompetitionRequest(request);

        CompetitionEntity competitionEntity = competitionConverter.convertToEntity(request);
        CompetitionEntity savedCompetition = competitionRepository.save(competitionEntity);

        return competitionConverter.convertToCreateCompetitionResponse(savedCompetition);
    }
}