package nl.fontys.s3.ticketmaster.business.interfaces.clubinterfaces;

import nl.fontys.s3.ticketmaster.domain.club.*;
import nl.fontys.s3.ticketmaster.persitence.entity.ClubEntity;

public interface ClubConverter {
    ClubEntity convertToEntity(CreateClubRequest request);
    CreateClubResponse convertToCreateClubResponse(ClubEntity clubEntity);
    GetClubResponse convertToGetClubResponse(ClubEntity clubEntity);
    UpdateClubResponse convertToUpdateClubResponse(ClubEntity clubEntity);
    Club convertToClub(ClubEntity clubEntity);
    ClubEntity updateEntityFromRequest(ClubEntity club, UpdateClubRequest request);
}