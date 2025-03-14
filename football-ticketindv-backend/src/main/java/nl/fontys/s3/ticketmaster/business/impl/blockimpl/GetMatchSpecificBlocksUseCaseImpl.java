package nl.fontys.s3.ticketmaster.business.impl.blockimpl;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import nl.fontys.s3.ticketmaster.business.interfaces.blockinterfaces.GetMatchSpecificBlocksUseCase;
import nl.fontys.s3.ticketmaster.business.interfaces.blockinterfaces.BlockConverter;
import nl.fontys.s3.ticketmaster.business.interfaces.matchinterfaces.MatchService;
import nl.fontys.s3.ticketmaster.domain.block.GetBlockResponse;
import nl.fontys.s3.ticketmaster.domain.seat.SeatResponse;
import nl.fontys.s3.ticketmaster.persitence.BlockRepository;
import nl.fontys.s3.ticketmaster.persitence.entity.BlockEntity;
import nl.fontys.s3.ticketmaster.persitence.entity.MatchEntity;
import nl.fontys.s3.ticketmaster.persitence.entity.SeatEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;
import java.util.Optional;

@Slf4j
@Service
@AllArgsConstructor
public class GetMatchSpecificBlocksUseCaseImpl implements GetMatchSpecificBlocksUseCase {
    private final BlockRepository blockRepository;
    private final BlockConverter blockConverter;
    private final MatchService matchService;

    @Override
    @Transactional
    public List<GetBlockResponse> getBlocksByStadiumForMatch(Long boxId, Long matchId) {
        List<BlockEntity> blockEntities = blockRepository.findByBoxId(boxId);
        Optional<MatchEntity> matchOptional = matchService.getMatchById(matchId);

        if (matchOptional.isEmpty()) {
            throw new IllegalArgumentException("Match not found for id: " + matchId);
        }

        MatchEntity match = matchOptional.get();
        Map<SeatEntity, Double> matchSpecificSeatPrices = match.getMatchSpecificSeatPrices();

        return blockEntities.stream()
                .map(block -> {
                    GetBlockResponse response = blockConverter.convertToGetBlockResponse(block);
                    List<SeatResponse> updatedSeats = response.getSeats().stream()
                            .map(seat -> {
                                Optional<Map.Entry<SeatEntity, Double>> matchingEntry = matchSpecificSeatPrices.entrySet().stream()
                                        .filter(entry -> entry.getKey().getId().equals(seat.getSeatId()))
                                        .findFirst();

                                double seatPrice = matchingEntry.map(Map.Entry::getValue)
                                        .orElse(seat.getPrice());

                                return SeatResponse.builder()
                                        .seatId(seat.getSeatId())
                                        .seatNumber(seat.getSeatNumber())
                                        .xPosition(seat.getXPosition())
                                        .yPosition(seat.getYPosition())
                                        .price(seatPrice)
                                        .build();
                            })
                            .toList();
                    response.setSeats(updatedSeats);
                    return response;
                })
                .toList();
    }
}