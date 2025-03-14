package nl.fontys.s3.ticketmaster.business.interfaces.clubinterfaces;

import nl.fontys.s3.ticketmaster.persitence.entity.ClubEntity;
import java.util.List;
import java.util.Optional;

public interface ClubService {
    boolean existsByName(String name);
    Optional<ClubEntity> findByClubName(String clubName);
    boolean existsById(long clubId);
    Optional<ClubEntity> findById(long clubId);
    ClubEntity save(ClubEntity club);
    List<ClubEntity> findAll();
    int count();
    void deleteById(long clubId);
}