package nl.fontys.s3.ticketmaster.business.interfaces.stadiuminterfaces;

import nl.fontys.s3.ticketmaster.business.exception.InvalidStadiumException;
import nl.fontys.s3.ticketmaster.domain.stadium.CreateStadiumRequest;
import nl.fontys.s3.ticketmaster.domain.stadium.UpdateStadiumRequest;

public interface StadiumValidator {
    void validateCreateStadiumRequest(CreateStadiumRequest request) throws InvalidStadiumException;
    void validateUpdateStadiumRequest(Long id, UpdateStadiumRequest request) throws InvalidStadiumException;
    void validateStadiumExists(Long id) throws InvalidStadiumException;
}