package nl.fontys.s3.ticketmaster.persitence;

import nl.fontys.s3.ticketmaster.persitence.entity.BlockEntity;
import nl.fontys.s3.ticketmaster.persitence.entity.BoxEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Repository
public interface BoxRepository extends JpaRepository<BoxEntity, Long> {
    List<BoxEntity> findByStadiumId(Long stadiumId);
    boolean existsByBoxNameAndStadiumId(String boxName, Long stadiumId);

    @Modifying
    @Transactional
    @Query("UPDATE BoxEntity b SET b.price = :newPrice WHERE b.id = :boxId")
    void updatePrice(@Param("boxId") Long boxId, @Param("newPrice") Double newPrice);

    @Query("SELECT b FROM BoxEntity b LEFT JOIN FETCH b.blocks WHERE b.id = :id")
    Optional<BoxEntity> findByIdWithBlocks(@Param("id") Long id);


    @Modifying
    @Transactional
    @Query("UPDATE SeatEntity s SET s.price = :newPrice " +
            "WHERE s.block.box.id = :boxId")
    void updateSeatPricesForBox(@Param("boxId") Long boxId, @Param("newPrice") Double newPrice);

    default BoxEntity updatePriceWithSeats(Long boxId, Double newPrice) {
        updatePrice(boxId, newPrice);
        updateSeatPricesForBox(boxId, newPrice);
        return findById(boxId).orElse(null);
    }

    @Transactional
    default BoxEntity addBlockToBox(Long boxId, BlockEntity block) {
        Optional<BoxEntity> boxOptional = findByIdWithBlocks(boxId);
        if (boxOptional.isPresent()) {
            BoxEntity box = boxOptional.get();
            box.getBlocks().add(block);
            return save(box);
        }
        return null;
    }
}