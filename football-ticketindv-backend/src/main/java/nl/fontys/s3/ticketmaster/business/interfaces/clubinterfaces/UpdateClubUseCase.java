package nl.fontys.s3.ticketmaster.business.interfaces.clubinterfaces;

import nl.fontys.s3.ticketmaster.domain.club.UpdateClubRequest;
import nl.fontys.s3.ticketmaster.domain.club.UpdateClubResponse;

public interface UpdateClubUseCase {
    UpdateClubResponse updateClub(Long clubId, UpdateClubRequest request);
}
