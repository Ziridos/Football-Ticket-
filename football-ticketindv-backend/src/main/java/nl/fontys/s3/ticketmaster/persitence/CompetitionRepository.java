package nl.fontys.s3.ticketmaster.persitence;

import nl.fontys.s3.ticketmaster.persitence.entity.CompetitionEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;


import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;


import java.util.Optional;

@Repository
public interface CompetitionRepository extends JpaRepository<CompetitionEntity, Long> {

    boolean existsByCompetitionName(String competitionName);

    Optional<CompetitionEntity> findByCompetitionName(String competitionName);

    @Query("SELECT c FROM CompetitionEntity c " +
            "WHERE (:name IS NULL OR :name = '' OR LOWER(c.competitionName) LIKE LOWER(CONCAT('%', :name, '%')))")
    Page<CompetitionEntity> findByFilters(
            @Param("name") String name,
            Pageable pageable
    );


}