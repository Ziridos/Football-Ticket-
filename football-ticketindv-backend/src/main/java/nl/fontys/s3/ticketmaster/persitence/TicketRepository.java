package nl.fontys.s3.ticketmaster.persitence;

import nl.fontys.s3.ticketmaster.domain.ticketsales.TicketSalesStatistics;
import nl.fontys.s3.ticketmaster.persitence.entity.MatchEntity;
import nl.fontys.s3.ticketmaster.persitence.entity.TicketEntity;
import nl.fontys.s3.ticketmaster.persitence.entity.UserEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface TicketRepository extends JpaRepository<TicketEntity, Long> {

    List<TicketEntity> findByUser(UserEntity user);

    List<TicketEntity> findByMatch(MatchEntity match);

    @Query("SELECT DISTINCT t FROM TicketEntity t " +
            "LEFT JOIN FETCH t.match m " +
            "LEFT JOIN FETCH t.seats s " +
            "WHERE t.user.id = :userId")
    List<TicketEntity> findByUserId(@Param("userId") Long userId);

    @Query("SELECT SUM(s.price) FROM TicketEntity t JOIN t.seats s WHERE t.id = :ticketId")
    Double calculateTotalPrice(@Param("ticketId") Long ticketId);

    @Query("SELECT new nl.fontys.s3.ticketmaster.domain.ticketsales.TicketSalesStatistics(" +
            "COUNT(t), " +
            "COALESCE(SUM(t.totalPrice), 0.0), " +
            "CASE WHEN COUNT(t) > 0 THEN AVG(t.totalPrice) ELSE 0.0 END) " +
            "FROM TicketEntity t " +
            "WHERE t.purchaseDateTime BETWEEN :startDate AND :endDate")
    List<TicketSalesStatistics> getTicketSalesStatistics(
            @Param("startDate") LocalDateTime startDate,
            @Param("endDate") LocalDateTime endDate);

    //new add tests
    @Query("SELECT t FROM TicketEntity t " +
            "LEFT JOIN FETCH t.match m " +
            "LEFT JOIN FETCH t.seats s " +
            "LEFT JOIN FETCH m.homeClub hc " +
            "LEFT JOIN FETCH m.awayClub ac " +
            "LEFT JOIN FETCH m.competition " +
            "LEFT JOIN FETCH hc.stadium " +
            "WHERE t.user.id = :userId " +
            "AND (:year IS NULL OR YEAR(t.purchaseDateTime) = :year) " +
            "AND (:quarter IS NULL OR QUARTER(t.purchaseDateTime) = :quarter)")
    Page<TicketEntity> findByFilters(
            @Param("userId") Long userId,
            @Param("year") Integer year,
            @Param("quarter") Integer quarter,
            Pageable pageable
    );

}