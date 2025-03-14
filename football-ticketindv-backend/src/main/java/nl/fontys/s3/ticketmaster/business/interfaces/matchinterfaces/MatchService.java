package nl.fontys.s3.ticketmaster.business.interfaces.matchinterfaces;

import nl.fontys.s3.ticketmaster.persitence.entity.MatchEntity;

import java.util.Optional;

public interface MatchService {
    Optional<MatchEntity> getMatchById(Long id);
}
