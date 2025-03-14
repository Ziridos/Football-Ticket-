package nl.fontys.s3.ticketmaster.business.interfaces.clubinterfaces;

import nl.fontys.s3.ticketmaster.domain.club.CreateClubRequest;
import nl.fontys.s3.ticketmaster.domain.club.CreateClubResponse;

public interface CreateClubUseCase {
    CreateClubResponse createClub(CreateClubRequest request);
}