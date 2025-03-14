package nl.fontys.s3.ticketmaster.business.impl.competitionimpl;

import lombok.AllArgsConstructor;
import nl.fontys.s3.ticketmaster.business.interfaces.competitioninterfaces.DeleteCompetitionUseCase;
import nl.fontys.s3.ticketmaster.business.interfaces.competitioninterfaces.CompetitionValidator;
import nl.fontys.s3.ticketmaster.persitence.CompetitionRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@AllArgsConstructor
public class DeleteCompetitionUseCaseImpl implements DeleteCompetitionUseCase {
    private final CompetitionRepository competitionRepository;
    private final CompetitionValidator competitionValidator;

    @Override
    @Transactional
    public void deleteCompetitionById(Long competitionId) {
        competitionValidator.validateCompetitionExists(competitionId);
        competitionRepository.deleteById(competitionId);
    }
}