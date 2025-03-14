package nl.fontys.s3.ticketmaster.business.impl.competitionimpl;

import lombok.AllArgsConstructor;
import nl.fontys.s3.ticketmaster.business.interfaces.competitioninterfaces.CompetitionService;
import nl.fontys.s3.ticketmaster.persitence.CompetitionRepository;
import nl.fontys.s3.ticketmaster.persitence.entity.CompetitionEntity;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@AllArgsConstructor
public class CompetitionServiceImpl implements CompetitionService {
    private final CompetitionRepository competitionRepository;

    @Override
    public boolean existsByName(String name) {
        return competitionRepository.existsByCompetitionName(name);
    }

    @Override
    public Optional<CompetitionEntity> findByCompetitionName(String competitionName) {
        return competitionRepository.findByCompetitionName(competitionName);
    }

    @Override
    public boolean existsById(long competitionId) {
        return competitionRepository.existsById(competitionId);
    }

    @Override
    public Optional<CompetitionEntity> findById(long competitionId) {
        return competitionRepository.findById(competitionId);
    }

    @Override
    public CompetitionEntity save(CompetitionEntity competition) {
        return competitionRepository.save(competition);
    }

    @Override
    public List<CompetitionEntity> findAll() {
        return competitionRepository.findAll();
    }

    @Override
    public int count() {
        return (int) competitionRepository.count();
    }

    @Override
    public void deleteById(long competitionId) {
        competitionRepository.deleteById(competitionId);
    }
}