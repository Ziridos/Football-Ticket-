package nl.fontys.s3.ticketmaster.persitence;

import nl.fontys.s3.ticketmaster.persitence.entity.ClubEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;


import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;


import java.util.Optional;

@Repository
public interface ClubRepository extends JpaRepository<ClubEntity, Long> {

    boolean existsByClubName(String clubName);

    Optional<ClubEntity> findByClubName(String clubName);

    @Query("SELECT c FROM ClubEntity c LEFT JOIN c.stadium s " +
            "WHERE (:name IS NULL OR LOWER(c.clubName) LIKE LOWER(CONCAT('%', :name, '%'))) " +
            "AND (:stadiumName IS NULL OR LOWER(s.stadiumName) LIKE LOWER(CONCAT('%', :stadiumName, '%'))) " +
            "AND (:stadiumCity IS NULL OR LOWER(s.stadiumCity) LIKE LOWER(CONCAT('%', :stadiumCity, '%'))) " +
            "AND (:stadiumCountry IS NULL OR LOWER(s.stadiumCountry) LIKE LOWER(CONCAT('%', :stadiumCountry, '%')))")
    Page<ClubEntity> findByFilters(
            @Param("name") String name,
            @Param("stadiumName") String stadiumName,
            @Param("stadiumCity") String stadiumCity,
            @Param("stadiumCountry") String stadiumCountry,
            Pageable pageable
    );


}