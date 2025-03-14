package nl.fontys.s3.ticketmaster.business.impl.stadiumimpl;

import lombok.AllArgsConstructor;
import nl.fontys.s3.ticketmaster.business.interfaces.stadiuminterfaces.GetStadiumUseCase;
import nl.fontys.s3.ticketmaster.business.interfaces.stadiuminterfaces.StadiumValidator;
import nl.fontys.s3.ticketmaster.business.interfaces.stadiuminterfaces.StadiumConverter;
import nl.fontys.s3.ticketmaster.domain.stadium.GetStadiumRequest;
import nl.fontys.s3.ticketmaster.domain.stadium.GetStadiumResponse;
import nl.fontys.s3.ticketmaster.persitence.StadiumRepository;
import nl.fontys.s3.ticketmaster.persitence.entity.StadiumEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@AllArgsConstructor
public class GetStadiumUseCaseImpl implements GetStadiumUseCase {
    private final StadiumRepository stadiumRepository;
    private final StadiumValidator stadiumValidator;
    private final StadiumConverter stadiumConverter;

    @Override
    @Transactional
    public GetStadiumResponse getStadiumById(GetStadiumRequest request) {
        stadiumValidator.validateStadiumExists(request.getStadiumId());

        StadiumEntity stadiumEntity = stadiumRepository.findById(request.getStadiumId())
                .orElseThrow(() -> new RuntimeException("Stadium not found"));

        return stadiumConverter.convertToGetResponse(stadiumEntity);
    }
}