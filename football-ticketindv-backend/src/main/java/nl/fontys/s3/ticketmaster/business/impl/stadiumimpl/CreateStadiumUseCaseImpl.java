package nl.fontys.s3.ticketmaster.business.impl.stadiumimpl;

import lombok.AllArgsConstructor;
import nl.fontys.s3.ticketmaster.business.interfaces.stadiuminterfaces.CreateStadiumUseCase;
import nl.fontys.s3.ticketmaster.business.interfaces.stadiuminterfaces.StadiumValidator;
import nl.fontys.s3.ticketmaster.business.interfaces.stadiuminterfaces.StadiumConverter;
import nl.fontys.s3.ticketmaster.domain.stadium.CreateStadiumRequest;
import nl.fontys.s3.ticketmaster.domain.stadium.CreateStadiumResponse;
import nl.fontys.s3.ticketmaster.persitence.StadiumRepository;
import nl.fontys.s3.ticketmaster.persitence.entity.StadiumEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@AllArgsConstructor
public class CreateStadiumUseCaseImpl implements CreateStadiumUseCase {
    private final StadiumRepository stadiumRepository;
    private final StadiumValidator stadiumValidator;
    private final StadiumConverter stadiumConverter;

    @Override
    @Transactional
    public CreateStadiumResponse createStadium(CreateStadiumRequest request) {
        stadiumValidator.validateCreateStadiumRequest(request);

        StadiumEntity newStadium = stadiumConverter.convertToEntity(request);
        StadiumEntity savedStadium = stadiumRepository.save(newStadium);

        return stadiumConverter.convertToCreateResponse(savedStadium);
    }
}