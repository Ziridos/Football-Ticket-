package nl.fontys.s3.ticketmaster.business.impl.stadiumimpl;

import lombok.AllArgsConstructor;
import nl.fontys.s3.ticketmaster.business.interfaces.stadiuminterfaces.StadiumService;
import nl.fontys.s3.ticketmaster.persitence.StadiumRepository;
import nl.fontys.s3.ticketmaster.persitence.entity.StadiumEntity;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@AllArgsConstructor
public class StadiumServiceImpl implements StadiumService {
    private final StadiumRepository stadiumRepository;

    @Override
    public boolean existsByName(String name) {
        return stadiumRepository.existsByStadiumName(name);
    }

    @Override
    public boolean existsById(long stadiumId) {
        return stadiumRepository.existsById(stadiumId);
    }

    @Override
    public Optional<StadiumEntity> findById(long stadiumId) {
        return stadiumRepository.findById(stadiumId);
    }

    @Override
    public StadiumEntity save(StadiumEntity stadium) {
        return stadiumRepository.save(stadium);
    }

    @Override
    public List<StadiumEntity> findAll() {
        return stadiumRepository.findAll();
    }

    @Override
    public int count() {
        return (int) stadiumRepository.count();
    }

    @Override
    public void deleteById(long stadiumId) {
        stadiumRepository.deleteById(stadiumId);
    }
}
