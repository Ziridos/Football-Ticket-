package nl.fontys.s3.ticketmaster.business.interfaces.boxinterfaces;

import nl.fontys.s3.ticketmaster.persitence.entity.BlockEntity;
import nl.fontys.s3.ticketmaster.persitence.entity.BoxEntity;

import java.util.List;
import java.util.Optional;

public interface BoxService {
    BoxEntity save(BoxEntity box);
    BoxEntity updatePrice(Long boxId, Double newPrice);
    Optional<BoxEntity> findById(Long boxId);
    BoxEntity addBlockToBox(Long boxId, BlockEntity block);
    List<BoxEntity> findByStadiumId(Long stadiumId);
    boolean existsByBoxNameAndStadiumId(String boxName, Long stadiumId);
    void deleteById(Long boxId);
    boolean existsById(Long id);
}
