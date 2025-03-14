package nl.fontys.s3.ticketmaster.business.impl.clubimpl;

import lombok.AllArgsConstructor;
import nl.fontys.s3.ticketmaster.business.interfaces.clubinterfaces.ClubService;
import nl.fontys.s3.ticketmaster.persitence.ClubRepository;
import nl.fontys.s3.ticketmaster.persitence.entity.ClubEntity;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@AllArgsConstructor
public class ClubServiceImpl implements ClubService {
    private final ClubRepository clubRepository;

    @Override
    public boolean existsByName(String name) {
        return clubRepository.existsByClubName(name);
    }

    @Override
    public Optional<ClubEntity> findByClubName(String clubName) {
        return clubRepository.findByClubName(clubName);
    }

    @Override
    public boolean existsById(long clubId) {
        return clubRepository.existsById(clubId);
    }

    @Override
    public Optional<ClubEntity> findById(long clubId)    {
        return clubRepository.findById(clubId);
    }

    @Override
    public ClubEntity save(ClubEntity club) {
        return clubRepository.save(club);
    }

    @Override
    public List<ClubEntity> findAll() {
        return clubRepository.findAll();
    }

    @Override
    public int count() {
        return (int) clubRepository.count();
    }

    @Override
    public void deleteById(long clubId) {
        clubRepository.deleteById(clubId);
    }
}