package nl.fontys.s3.ticketmaster.persitence;

import nl.fontys.s3.ticketmaster.persitence.entity.MatchEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;



@Repository
public interface MatchRepository extends JpaRepository<MatchEntity, Long> {

    @Query("SELECT DISTINCT m FROM MatchEntity m " +
            "LEFT JOIN m.homeClub hc " +
            "LEFT JOIN m.awayClub ac " +
            "LEFT JOIN m.competition c " +
            "WHERE (:homeClubName IS NULL OR :homeClubName = '' OR LOWER(hc.clubName) LIKE LOWER(CONCAT('%', :homeClubName, '%'))) " +
            "AND (:awayClubName IS NULL OR :awayClubName = '' OR LOWER(ac.clubName) LIKE LOWER(CONCAT('%', :awayClubName, '%'))) " +
            "AND (:competitionName IS NULL OR :competitionName = '' OR LOWER(c.competitionName) LIKE LOWER(CONCAT('%', :competitionName, '%'))) " +
            "AND (:date IS NULL OR CAST(m.matchDateTime as LocalDate) = :date)")
    Page<MatchEntity> findByFilters(
            @Param("homeClubName") String homeClubName,
            @Param("awayClubName") String awayClubName,
            @Param("competitionName") String competitionName,
            @Param("date") LocalDate date,
            Pageable pageable
    );

    @Query("SELECT CASE WHEN COUNT(m) > 0 THEN true ELSE false END FROM MatchEntity m " +
            "JOIN m.availableSeats a " +
            "WHERE m.id = :matchId AND KEY(a).id = :seatId AND VALUE(a) = true")
    boolean isSeatAvailable(@Param("matchId") Long matchId, @Param("seatId") Long seatId);

    @Modifying
    @Transactional
    @Query(value = "UPDATE match_available_seat " +
            "SET is_available = :isAvailable " +
            "WHERE match_id = :matchId AND seat_id = :seatId",
            nativeQuery = true)
    void updateSeatAvailability(
            @Param("matchId") Long matchId,
            @Param("seatId") String seatId,
            @Param("isAvailable") boolean isAvailable);
}