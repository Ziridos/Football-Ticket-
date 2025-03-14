package nl.fontys.s3.ticketmaster.business.impl.seatimpl;

import lombok.AllArgsConstructor;
import nl.fontys.s3.ticketmaster.business.interfaces.blockinterfaces.BlockConverter;
import nl.fontys.s3.ticketmaster.business.interfaces.boxinterfaces.BoxConverter;
import nl.fontys.s3.ticketmaster.business.interfaces.seatinterfaces.SeatConverter;
import nl.fontys.s3.ticketmaster.domain.seat.SeatAvailabilityResponse;
import nl.fontys.s3.ticketmaster.domain.seat.SeatDTO;
import nl.fontys.s3.ticketmaster.persitence.entity.SeatEntity;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;

@Component
@AllArgsConstructor
public class SeatConverterImpl implements SeatConverter {
    private final BlockConverter blockConverter;
    private final BoxConverter boxConverter;

    @Override
    public SeatAvailabilityResponse convertToSeatAvailabilityResponse(SeatEntity seat, boolean isAvailable) {
        return SeatAvailabilityResponse.builder()
                .seatId(seat.getId())
                .seatNumber(seat.getSeatNumber())
                .xPosition(seat.getXPosition())
                .yPosition(seat.getYPosition())
                .isAvailable(isAvailable)
                .blockId(seat.getBlock().getId())
                .price(seat.getPrice())
                .build();
    }

    @Override
    public List<SeatAvailabilityResponse> convertToSeatAvailabilityResponses(List<SeatEntity> seats, Map<Long, Boolean> availabilityMap) {
        return seats.stream()
                .map(seat -> convertToSeatAvailabilityResponse(seat, availabilityMap.getOrDefault(seat.getId(), false)))
                .toList();
    }


    @Override
    public SeatDTO toSeatDTO(SeatEntity entity) {
        return SeatDTO.builder()
                .seatId(entity.getId())
                .seatNumber(entity.getSeatNumber())
                .block(blockConverter.toBlockDTO(entity.getBlock()))
                .box(boxConverter.toBoxDTO(entity.getBlock().getBox()))
                .price(entity.getPrice())
                .build();
    }
}