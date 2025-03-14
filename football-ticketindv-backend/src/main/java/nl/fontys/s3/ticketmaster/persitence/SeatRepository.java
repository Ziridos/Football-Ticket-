package nl.fontys.s3.ticketmaster.persitence;

import nl.fontys.s3.ticketmaster.persitence.entity.SeatEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface SeatRepository extends JpaRepository<SeatEntity, Long> {
    @Query("SELECT s FROM SeatEntity s JOIN s.block b JOIN b.box bx JOIN bx.stadium st WHERE st.id = :stadiumId")
    List<SeatEntity> getSeatsByStadium(@Param("stadiumId") Long stadiumId);

}