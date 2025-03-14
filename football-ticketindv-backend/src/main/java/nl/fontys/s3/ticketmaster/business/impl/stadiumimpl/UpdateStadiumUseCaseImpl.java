package nl.fontys.s3.ticketmaster.business.impl.stadiumimpl;

import lombok.AllArgsConstructor;
import nl.fontys.s3.ticketmaster.business.interfaces.stadiuminterfaces.UpdateStadiumUseCase;
import nl.fontys.s3.ticketmaster.business.interfaces.stadiuminterfaces.StadiumValidator;
import nl.fontys.s3.ticketmaster.business.interfaces.stadiuminterfaces.StadiumConverter;
import nl.fontys.s3.ticketmaster.domain.stadium.UpdateStadiumRequest;
import nl.fontys.s3.ticketmaster.domain.stadium.UpdateStadiumResponse;
import nl.fontys.s3.ticketmaster.persitence.StadiumRepository;
import nl.fontys.s3.ticketmaster.persitence.entity.StadiumEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


@Service
@AllArgsConstructor
public class UpdateStadiumUseCaseImpl implements UpdateStadiumUseCase {
    private final StadiumRepository stadiumRepository;
    private final StadiumValidator stadiumValidator;
    private final StadiumConverter stadiumConverter;

    @Override
    @Transactional
    public UpdateStadiumResponse updateStadium(Long id, UpdateStadiumRequest request) {
        stadiumValidator.validateUpdateStadiumRequest(id, request);

        return stadiumRepository.findById(id)
                .map(existingStadium -> {
                    existingStadium.setStadiumName(request.getStadiumName());
                    existingStadium.setStadiumAddress(request.getStadiumAddress());
                    existingStadium.setStadiumPostalCode(request.getStadiumPostalCode());
                    existingStadium.setStadiumCity(request.getStadiumCity());
                    existingStadium.setStadiumCountry(request.getStadiumCountry());

                    StadiumEntity updatedStadium = stadiumRepository.save(existingStadium);
                    return stadiumConverter.convertToUpdateResponse(updatedStadium);
                })
                .orElseThrow(() -> new RuntimeException("Stadium not found with id: " + id));
    }
}