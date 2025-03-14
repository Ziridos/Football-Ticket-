package nl.fontys.s3.ticketmaster.business.impl.seatimpl;

import lombok.AllArgsConstructor;
import nl.fontys.s3.ticketmaster.business.interfaces.seatinterfaces.SeatService;
import nl.fontys.s3.ticketmaster.business.interfaces.boxinterfaces.BoxService;
import nl.fontys.s3.ticketmaster.business.interfaces.blockinterfaces.BlockService;
import nl.fontys.s3.ticketmaster.persitence.SeatRepository;
import nl.fontys.s3.ticketmaster.persitence.entity.SeatEntity;
import nl.fontys.s3.ticketmaster.persitence.entity.BoxEntity;
import nl.fontys.s3.ticketmaster.persitence.entity.BlockEntity;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@AllArgsConstructor
public class SeatServiceImpl implements SeatService {
    private final SeatRepository seatRepository;
    private final BoxService boxService;
    private final BlockService blockService;

    @Override
    public List<SeatEntity> getSeatsByStadium(Long stadiumId) {
        List<BoxEntity> stadiumBoxes = boxService.findByStadiumId(stadiumId);

        return stadiumBoxes.stream()
                .flatMap(box -> {
                    List<BlockEntity> boxBlocks = blockService.findByBoxId(box.getId());
                    return boxBlocks.stream();
                })
                .flatMap(block -> block.getSeats().stream())
                .toList();
    }

    @Override
    public void saveAllSeats(List<SeatEntity> seats) {
        seatRepository.saveAll(seats);
    }
}