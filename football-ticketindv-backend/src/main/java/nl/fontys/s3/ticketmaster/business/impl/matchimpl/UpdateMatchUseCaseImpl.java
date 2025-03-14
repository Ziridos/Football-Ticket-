package nl.fontys.s3.ticketmaster.business.impl.matchimpl;

import lombok.AllArgsConstructor;
import nl.fontys.s3.ticketmaster.business.interfaces.matchinterfaces.MatchConverter;
import nl.fontys.s3.ticketmaster.business.interfaces.matchinterfaces.MatchValidator;
import nl.fontys.s3.ticketmaster.business.interfaces.matchinterfaces.UpdateMatchUseCase;
import nl.fontys.s3.ticketmaster.business.exception.InvalidMatchException;
import nl.fontys.s3.ticketmaster.domain.match.UpdateMatchRequest;
import nl.fontys.s3.ticketmaster.domain.match.UpdateMatchResponse;
import nl.fontys.s3.ticketmaster.persitence.MatchRepository;
import nl.fontys.s3.ticketmaster.persitence.entity.MatchEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@AllArgsConstructor
public class UpdateMatchUseCaseImpl implements UpdateMatchUseCase {
    private final MatchRepository matchRepository;
    private final MatchValidator matchValidator;
    private final MatchConverter matchConverter;

    @Override
    @Transactional
    public UpdateMatchResponse updateMatch(UpdateMatchRequest request, Long matchId) {
        matchValidator.validateUpdateMatchRequest(request, matchId);

        MatchEntity existingMatch = matchRepository.findById(matchId)
                .orElseThrow(() -> new InvalidMatchException("Match not found"));

        MatchEntity updatedMatch = matchConverter.updateEntityFromRequest(existingMatch, request);
        MatchEntity savedMatch = matchRepository.save(updatedMatch);

        return matchConverter.convertToUpdateMatchResponse(savedMatch);
    }
}