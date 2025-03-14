package nl.fontys.s3.ticketmaster.business.interfaces.clubinterfaces;

import nl.fontys.s3.ticketmaster.domain.club.CreateClubRequest;
import nl.fontys.s3.ticketmaster.domain.club.UpdateClubRequest;

public interface ClubValidator {
    void validateCreateClubRequest(CreateClubRequest request);
    void validateUpdateClubRequest(UpdateClubRequest request, Long clubId);
    void validateClubExists(Long clubId);
    void validateClubs(String homeClubName, String awayClubName);
}