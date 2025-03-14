package nl.fontys.s3.ticketmaster.business.impl.matchimpl;

import lombok.AllArgsConstructor;
import nl.fontys.s3.ticketmaster.business.interfaces.matchinterfaces.MatchService;
import nl.fontys.s3.ticketmaster.persitence.MatchRepository;
import nl.fontys.s3.ticketmaster.persitence.entity.MatchEntity;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@AllArgsConstructor
public class MatchServiceImpl implements MatchService {
    private final MatchRepository matchRepository;

    @Override
    public Optional<MatchEntity> getMatchById(Long matchId) {
        return matchRepository.findById(matchId);
    }
}