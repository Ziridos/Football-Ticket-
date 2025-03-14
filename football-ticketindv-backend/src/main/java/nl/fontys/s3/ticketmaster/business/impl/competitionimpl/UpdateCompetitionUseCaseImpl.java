package nl.fontys.s3.ticketmaster.business.impl.competitionimpl;

import lombok.AllArgsConstructor;
import nl.fontys.s3.ticketmaster.business.interfaces.competitioninterfaces.UpdateCompetitionUseCase;
import nl.fontys.s3.ticketmaster.business.interfaces.competitioninterfaces.CompetitionConverter;
import nl.fontys.s3.ticketmaster.business.interfaces.competitioninterfaces.CompetitionValidator;
import nl.fontys.s3.ticketmaster.domain.competition.UpdateCompetitionRequest;
import nl.fontys.s3.ticketmaster.domain.competition.UpdateCompetitionResponse;
import nl.fontys.s3.ticketmaster.persitence.CompetitionRepository;
import nl.fontys.s3.ticketmaster.persitence.entity.CompetitionEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


@Service
@AllArgsConstructor
public class UpdateCompetitionUseCaseImpl implements UpdateCompetitionUseCase {
    private final CompetitionRepository competitionRepository;
    private final CompetitionConverter competitionConverter;
    private final CompetitionValidator competitionValidator;

    @Override
    @Transactional
    public UpdateCompetitionResponse updateCompetition(UpdateCompetitionRequest request) {
        competitionValidator.validateUpdateCompetitionRequest(request, request.getCompetitionId());

        CompetitionEntity existingCompetition = competitionRepository.findById(request.getCompetitionId())
                .orElseThrow(() -> new RuntimeException("Competition not found with id: " + request.getCompetitionId()));

        CompetitionEntity updatedCompetition = competitionConverter.updateEntityFromRequest(existingCompetition, request);
        CompetitionEntity savedCompetition = competitionRepository.save(updatedCompetition);

        return competitionConverter.convertToUpdateCompetitionResponse(savedCompetition);
    }
}