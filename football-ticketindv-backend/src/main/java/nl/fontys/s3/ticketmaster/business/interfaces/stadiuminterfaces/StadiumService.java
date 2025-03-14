package nl.fontys.s3.ticketmaster.business.interfaces.stadiuminterfaces;

import nl.fontys.s3.ticketmaster.persitence.entity.StadiumEntity;
import java.util.List;
import java.util.Optional;

public interface StadiumService {
    boolean existsByName(String name);
    boolean existsById(long stadiumId);
    Optional<StadiumEntity> findById(long stadiumId);
    StadiumEntity save(StadiumEntity stadium);
    List<StadiumEntity> findAll();
    int count();
    void deleteById(long stadiumId);
}