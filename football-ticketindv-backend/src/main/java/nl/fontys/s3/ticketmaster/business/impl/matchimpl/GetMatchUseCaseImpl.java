package nl.fontys.s3.ticketmaster.business.impl.matchimpl;

import lombok.AllArgsConstructor;
import nl.fontys.s3.ticketmaster.business.interfaces.matchinterfaces.GetMatchUseCase;
import nl.fontys.s3.ticketmaster.business.interfaces.matchinterfaces.MatchConverter;
import nl.fontys.s3.ticketmaster.business.interfaces.matchinterfaces.MatchValidator;
import nl.fontys.s3.ticketmaster.domain.match.GetMatchRequest;
import nl.fontys.s3.ticketmaster.domain.match.GetMatchResponse;
import nl.fontys.s3.ticketmaster.persitence.MatchRepository;
import nl.fontys.s3.ticketmaster.persitence.entity.MatchEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


@Service
@AllArgsConstructor
public class GetMatchUseCaseImpl implements GetMatchUseCase {
    private final MatchRepository matchRepository;
    private final MatchValidator matchValidator;
    private final MatchConverter matchConverter;

    @Override
    @Transactional
    public GetMatchResponse getMatch(GetMatchRequest request) {
        matchValidator.validateMatchExists(request.getMatchId());
        MatchEntity matchEntity = matchRepository.findById(request.getMatchId())
                .orElseThrow(() -> new RuntimeException("Match not found with id: " + request.getMatchId()));
        return matchConverter.convertToGetMatchResponse(matchEntity);
    }
}