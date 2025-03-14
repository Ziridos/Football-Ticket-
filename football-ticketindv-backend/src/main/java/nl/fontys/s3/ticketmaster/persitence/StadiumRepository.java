package nl.fontys.s3.ticketmaster.persitence;


import nl.fontys.s3.ticketmaster.persitence.entity.StadiumEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface StadiumRepository extends JpaRepository<StadiumEntity, Long>, JpaSpecificationExecutor<StadiumEntity> {
    boolean existsByStadiumName(String stadiumName);
    Optional<StadiumEntity> findByStadiumName(String stadiumName);
    @Query("SELECT s FROM StadiumEntity s " +
            "WHERE (:name IS NULL OR :name = '' OR LOWER(s.stadiumName) LIKE LOWER(CONCAT('%', :name, '%'))) " +
            "AND (:city IS NULL OR :city = '' OR LOWER(s.stadiumCity) LIKE LOWER(CONCAT('%', :city, '%'))) " +
            "AND (:country IS NULL OR :country = '' OR LOWER(s.stadiumCountry) LIKE LOWER(CONCAT('%', :country, '%')))")
    Page<StadiumEntity> findByFilters(
            @Param("name") String name,
            @Param("city") String city,
            @Param("country") String country,
            Pageable pageable
    );
}