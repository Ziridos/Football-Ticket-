package nl.fontys.s3.ticketmaster.business.impl.boximpl;

import lombok.AllArgsConstructor;
import nl.fontys.s3.ticketmaster.business.interfaces.boxinterfaces.GetMatchSpecificBoxesUseCase;
import nl.fontys.s3.ticketmaster.business.interfaces.boxinterfaces.BoxConverter;
import nl.fontys.s3.ticketmaster.business.interfaces.matchinterfaces.MatchService;
import nl.fontys.s3.ticketmaster.domain.box.GetBoxResponse;
import nl.fontys.s3.ticketmaster.persitence.BoxRepository;
import nl.fontys.s3.ticketmaster.persitence.entity.BoxEntity;
import nl.fontys.s3.ticketmaster.persitence.entity.MatchEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;
import java.util.Optional;

@Service
@AllArgsConstructor
public class GetMatchSpecificBoxesUseCaseImpl implements GetMatchSpecificBoxesUseCase {
    private final BoxRepository boxRepository;
    private final BoxConverter boxConverter;
    private final MatchService matchService;

    @Override
    @Transactional
    public List<GetBoxResponse> getBoxesByStadiumForMatch(Long stadiumId, Long matchId) {
        List<BoxEntity> boxEntities = boxRepository.findByStadiumId(stadiumId);
        Optional<MatchEntity> matchOptional = matchService.getMatchById(matchId);

        if (matchOptional.isEmpty()) {
            throw new IllegalArgumentException("Match not found for id: " + matchId);
        }

        MatchEntity match = matchOptional.get();
        Map<BoxEntity, Double> matchSpecificBoxPrices = match.getMatchSpecificBoxPrices();

        return boxEntities.stream()
                .map(box -> {
                    GetBoxResponse response = boxConverter.convertToGetBoxResponse(box);
                    Double matchPrice = matchSpecificBoxPrices.get(box);
                    double boxPrice = (matchPrice != null) ? matchPrice : box.getPrice();
                    response.setPrice(boxPrice);
                    return response;
                })
                .toList();
    }
}