package nl.fontys.s3.ticketmaster.business.impl.matchimpl;

import lombok.AllArgsConstructor;
import nl.fontys.s3.ticketmaster.business.interfaces.matchinterfaces.DeleteMatchUseCase;
import nl.fontys.s3.ticketmaster.business.interfaces.matchinterfaces.MatchValidator;
import nl.fontys.s3.ticketmaster.persitence.MatchRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@AllArgsConstructor
public class DeleteMatchUseCaseImpl implements DeleteMatchUseCase {
    private final MatchRepository matchRepository;
    private final MatchValidator matchValidator;

    @Override
    @Transactional
    public void deleteMatch(Long matchId) {
        matchValidator.validateMatchExists(matchId);
        matchRepository.deleteById(matchId);
    }
}