package nl.fontys.s3.ticketmaster.business.interfaces.clubinterfaces;

import nl.fontys.s3.ticketmaster.domain.club.GetClubRequest;
import nl.fontys.s3.ticketmaster.domain.club.GetClubResponse;

public interface GetClubUseCase {
    GetClubResponse getClub(GetClubRequest request);
}

