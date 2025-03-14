package nl.fontys.s3.ticketmaster.business.impl.stadiumimpl;

import lombok.AllArgsConstructor;
import nl.fontys.s3.ticketmaster.business.interfaces.stadiuminterfaces.DeleteStadiumUseCase;
import nl.fontys.s3.ticketmaster.business.interfaces.stadiuminterfaces.StadiumValidator;
import nl.fontys.s3.ticketmaster.persitence.StadiumRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@AllArgsConstructor
public class DeleteStadiumUseCaseImpl implements DeleteStadiumUseCase {
    private final StadiumRepository stadiumRepository;
    private final StadiumValidator stadiumValidator;

    @Override
    @Transactional
    public void deleteStadiumById(Long stadiumId) {
        stadiumValidator.validateStadiumExists(stadiumId);
        stadiumRepository.deleteById(stadiumId);
    }
}