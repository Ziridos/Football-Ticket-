package nl.fontys.s3.ticketmaster.business.interfaces.clubinterfaces;

import nl.fontys.s3.ticketmaster.domain.club.GetAllClubsRequest;
import nl.fontys.s3.ticketmaster.domain.club.GetAllClubsResponse;

public interface GetAllClubsUseCase {
    GetAllClubsResponse getAllClubs(GetAllClubsRequest request);
}
