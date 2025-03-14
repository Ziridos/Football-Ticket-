package nl.fontys.s3.ticketmaster.business.impl.boximpl;

import lombok.AllArgsConstructor;
import nl.fontys.s3.ticketmaster.business.interfaces.boxinterfaces.BoxService;
import nl.fontys.s3.ticketmaster.persitence.BoxRepository;
import nl.fontys.s3.ticketmaster.persitence.entity.BoxEntity;
import nl.fontys.s3.ticketmaster.persitence.entity.BlockEntity;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@AllArgsConstructor
public class BoxServiceImpl implements BoxService {
    private final BoxRepository boxRepository;

    @Override
    public BoxEntity save(BoxEntity box) {
        return boxRepository.save(box);
    }

    @Override
    public BoxEntity updatePrice(Long boxId, Double newPrice) {
        return boxRepository.updatePriceWithSeats(boxId, newPrice);
    }

    @Override
    public Optional<BoxEntity> findById(Long boxId) {
        return boxRepository.findById(boxId);
    }

    @Override
    public BoxEntity addBlockToBox(Long boxId, BlockEntity block) {
        return boxRepository.addBlockToBox(boxId, block);
    }

    @Override
    public List<BoxEntity> findByStadiumId(Long stadiumId) {
        return boxRepository.findByStadiumId(stadiumId);
    }

    @Override
    public boolean existsByBoxNameAndStadiumId(String boxName, Long stadiumId) {
        return boxRepository.existsByBoxNameAndStadiumId(boxName, stadiumId);
    }

    @Override
    public void deleteById(Long boxId) {
        boxRepository.deleteById(boxId);
    }

    @Override
    public boolean existsById(Long id) {
        return boxRepository.existsById(id);
    }
}