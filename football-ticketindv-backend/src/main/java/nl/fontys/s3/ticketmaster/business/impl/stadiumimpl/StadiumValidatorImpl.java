package nl.fontys.s3.ticketmaster.business.impl.stadiumimpl;

import nl.fontys.s3.ticketmaster.business.exception.InvalidStadiumException;
import nl.fontys.s3.ticketmaster.business.interfaces.stadiuminterfaces.StadiumValidator;
import nl.fontys.s3.ticketmaster.domain.stadium.CreateStadiumRequest;
import nl.fontys.s3.ticketmaster.domain.stadium.UpdateStadiumRequest;
import nl.fontys.s3.ticketmaster.persitence.StadiumRepository;
import nl.fontys.s3.ticketmaster.persitence.entity.StadiumEntity;
import org.springframework.stereotype.Component;

@Component
public class StadiumValidatorImpl implements StadiumValidator {
    private final StadiumRepository stadiumRepository;
    private static final String STADIUM_NOT_FOUND_MESSAGE = "Stadium not found";

    public StadiumValidatorImpl(StadiumRepository stadiumRepository) {
        this.stadiumRepository = stadiumRepository;
    }

    @Override
    public void validateCreateStadiumRequest(CreateStadiumRequest request) {
        if (request == null) {
            throw new InvalidStadiumException("Stadium request cannot be null");
        }
        validateCommonFields(request.getStadiumName(), request.getStadiumAddress(),
                request.getStadiumPostalCode(), request.getStadiumCity(),
                request.getStadiumCountry());
        validateUniqueName(request.getStadiumName());
    }

    @Override
    public void validateStadiumExists(Long id) {
        if (!stadiumRepository.existsById(id)) {
            throw new InvalidStadiumException(STADIUM_NOT_FOUND_MESSAGE);
        }
    }

    @Override
    public void validateUpdateStadiumRequest(Long id, UpdateStadiumRequest request) {
        if (request == null) {
            throw new InvalidStadiumException("Update request cannot be null");
        }

        if (!stadiumRepository.existsById(id)) {
            throw new InvalidStadiumException(STADIUM_NOT_FOUND_MESSAGE);
        }

        validateCommonFields(request.getStadiumName(), request.getStadiumAddress(),
                request.getStadiumPostalCode(), request.getStadiumCity(),
                request.getStadiumCountry());

        StadiumEntity existingStadium = stadiumRepository.findById(id)
                .orElseThrow(() -> new InvalidStadiumException(STADIUM_NOT_FOUND_MESSAGE));

        if (!existingStadium.getStadiumName().equals(request.getStadiumName())
                && stadiumRepository.existsByStadiumName(request.getStadiumName())) {
            throw new InvalidStadiumException("Stadium with this name already exists");
        }
    }

    private void validateCommonFields(String name, String address, String postalCode,
                                      String city, String country) {
        if (name == null || name.trim().isEmpty()) {
            throw new InvalidStadiumException("Stadium name cannot be empty");
        }
        if (address == null || address.trim().isEmpty()) {
            throw new InvalidStadiumException("Stadium address cannot be empty");
        }
        if (postalCode == null || !postalCode.matches("^\\d{4}\\s?[A-Z]{2}$")) {
            throw new InvalidStadiumException("Invalid postal code format");
        }
        if (city == null || city.trim().isEmpty()) {
            throw new InvalidStadiumException("Stadium city cannot be empty");
        }
        if (country == null || country.trim().isEmpty()) {
            throw new InvalidStadiumException("Stadium country cannot be empty");
        }
    }

    private void validateUniqueName(String name) {
        if (stadiumRepository.existsByStadiumName(name)) {
            throw new InvalidStadiumException("Stadium with this name already exists");
        }
    }
}
