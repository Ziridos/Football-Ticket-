package nl.fontys.s3.ticketmaster.business.interfaces.competitioninterfaces;

import nl.fontys.s3.ticketmaster.persitence.entity.CompetitionEntity;
import java.util.List;
import java.util.Optional;

public interface CompetitionService {
    boolean existsByName(String name);
    Optional<CompetitionEntity> findByCompetitionName(String competitionName);
    boolean existsById(long competitionId);
    Optional<CompetitionEntity> findById(long competitionId);
    CompetitionEntity save(CompetitionEntity competition);
    List<CompetitionEntity> findAll();
    int count();
    void deleteById(long competitionId);
}