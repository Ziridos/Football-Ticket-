package nl.fontys.s3.ticketmaster.business.impl.matchimpl;

import lombok.AllArgsConstructor;
import nl.fontys.s3.ticketmaster.business.interfaces.matchinterfaces.CreateMatchUseCase;
import nl.fontys.s3.ticketmaster.business.interfaces.matchinterfaces.MatchConverter;
import nl.fontys.s3.ticketmaster.business.interfaces.matchinterfaces.MatchValidator;
import nl.fontys.s3.ticketmaster.domain.match.CreateMatchRequest;
import nl.fontys.s3.ticketmaster.domain.match.CreateMatchResponse;
import nl.fontys.s3.ticketmaster.persitence.MatchRepository;
import nl.fontys.s3.ticketmaster.persitence.SeatRepository;
import nl.fontys.s3.ticketmaster.persitence.entity.MatchEntity;
import nl.fontys.s3.ticketmaster.persitence.entity.SeatEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
@AllArgsConstructor
public class CreateMatchUseCaseImpl implements CreateMatchUseCase {
    private final MatchRepository matchRepository;
    private final SeatRepository seatRepository;
    private final MatchValidator matchValidator;
    private final MatchConverter matchConverter;

    @Override
    @Transactional
    public CreateMatchResponse createMatch(CreateMatchRequest request) {
        matchValidator.validateCreateMatchRequest(request);

        MatchEntity matchEntity = matchConverter.convertToEntity(request);

        // Initialize availableSeats map
        Long stadiumId = matchEntity.getHomeClub().getStadium().getId();
        List<SeatEntity> allSeats = seatRepository.getSeatsByStadium(stadiumId);
        Map<SeatEntity, Boolean> availableSeats = new HashMap<>();
        for (SeatEntity seat : allSeats) {
            availableSeats.put(seat, true);
        }
        matchEntity.setAvailableSeats(availableSeats);

        // Initialize matchSpecificBoxPrices and matchSpecificSeatPrices if needed
        // This depends on your business logic, you might want to set default prices or leave them empty
        matchEntity.setMatchSpecificBoxPrices(new HashMap<>());
        matchEntity.setMatchSpecificSeatPrices(new HashMap<>());

        MatchEntity savedMatch = matchRepository.save(matchEntity);

        return matchConverter.convertToCreateMatchResponse(savedMatch);
    }
}