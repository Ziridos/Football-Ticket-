package nl.fontys.s3.ticketmaster.business.impl.competitionimpl;

import lombok.AllArgsConstructor;
import nl.fontys.s3.ticketmaster.business.exception.InvalidCompetitionException;
import nl.fontys.s3.ticketmaster.business.interfaces.competitioninterfaces.CompetitionValidator;
import nl.fontys.s3.ticketmaster.domain.competition.CreateCompetitionRequest;
import nl.fontys.s3.ticketmaster.domain.competition.UpdateCompetitionRequest;
import nl.fontys.s3.ticketmaster.persitence.CompetitionRepository;
import nl.fontys.s3.ticketmaster.persitence.entity.CompetitionEntity;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
@AllArgsConstructor
public class CompetitionValidatorImpl implements CompetitionValidator {
    private final CompetitionRepository competitionRepository;



    @Override
    public void validateCreateCompetitionRequest(CreateCompetitionRequest request) {
        if (request.getCompetitionName() == null || request.getCompetitionName().trim().isEmpty()) {
            throw new InvalidCompetitionException("Competition name cannot be null or empty.");
        }
        if (competitionRepository.existsByCompetitionName(request.getCompetitionName())) {
            throw new InvalidCompetitionException("Competition with the given name already exists.");
        }
    }

    @Override
    public void validateUpdateCompetitionRequest(UpdateCompetitionRequest request, Long competitionId) {
        validateCompetitionExists(competitionId);
        if (request.getCompetitionName() == null || request.getCompetitionName().trim().isEmpty()) {
            throw new InvalidCompetitionException("Competition name cannot be null or empty.");
        }
        CompetitionEntity existingCompetition = competitionRepository.findById(competitionId)
                .orElseThrow(() -> new InvalidCompetitionException("Competition with the given ID does not exist."));

        if (competitionRepository.existsByCompetitionName(request.getCompetitionName()) &&
                !existingCompetition.getCompetitionName().equals(request.getCompetitionName())) {
            throw new InvalidCompetitionException("Competition with the given name already exists.");
        }
    }

    @Override
    public void validateCompetitionExists(Long competitionId) {
        if (!competitionRepository.existsById(competitionId)) {
            throw new InvalidCompetitionException("Competition with the given ID does not exist.");
        }
    }

    @Override
    public void validateCompetition(String competitionName) {
        Optional<CompetitionEntity> competition = Optional.ofNullable(competitionRepository.findByCompetitionName(competitionName))
                .orElse(Optional.empty());
        if (competition.isEmpty()) {
            throw new InvalidCompetitionException("Competition with the given name does not exist.");
        }
    }
}
