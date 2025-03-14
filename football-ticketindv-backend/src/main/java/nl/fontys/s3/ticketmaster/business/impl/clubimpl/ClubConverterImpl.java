package nl.fontys.s3.ticketmaster.business.impl.clubimpl;

import lombok.AllArgsConstructor;
import nl.fontys.s3.ticketmaster.business.interfaces.clubinterfaces.ClubConverter;
import nl.fontys.s3.ticketmaster.business.interfaces.stadiuminterfaces.StadiumService;
import nl.fontys.s3.ticketmaster.domain.club.*;
import nl.fontys.s3.ticketmaster.persitence.entity.ClubEntity;
import nl.fontys.s3.ticketmaster.persitence.entity.StadiumEntity;
import org.springframework.stereotype.Component;


@Component
@AllArgsConstructor
public class ClubConverterImpl implements ClubConverter {
    private final StadiumService stadiumService;

    @Override
    public ClubEntity convertToEntity(CreateClubRequest request) {
        StadiumEntity stadiumEntity = stadiumService.findById(request.getStadiumId())
                .orElseThrow(() -> new RuntimeException("Stadium not found with id: " + request.getStadiumId()));
        return ClubEntity.builder()
                .clubName(request.getClubName())
                .stadium(stadiumEntity)
                .build();
    }

    @Override
    public CreateClubResponse convertToCreateClubResponse(ClubEntity clubEntity) {
        return CreateClubResponse.builder()
                .clubId(clubEntity.getId())
                .clubName(clubEntity.getClubName())
                .build();
    }

    @Override
    public GetClubResponse convertToGetClubResponse(ClubEntity clubEntity) {
        return GetClubResponse.builder()
                .clubId(clubEntity.getId())
                .clubName(clubEntity.getClubName())
                .logo(clubEntity.getLogo())
                .logoContentType(clubEntity.getLogoContentType())
                .stadium(clubEntity.getStadium())
                .build();
    }

    @Override
    public UpdateClubResponse convertToUpdateClubResponse(ClubEntity clubEntity) {
        return UpdateClubResponse.builder()
                .clubId(clubEntity.getId())
                .clubName(clubEntity.getClubName())
                .stadium(clubEntity.getStadium())
                .build();
    }

    @Override
    public Club convertToClub(ClubEntity clubEntity) {
        return Club.builder()
                .clubId(clubEntity.getId())
                .clubName(clubEntity.getClubName())
                .logo(clubEntity.getLogo())
                .logoContentType(clubEntity.getLogoContentType())
                .stadium(clubEntity.getStadium())
                .build();
    }

    @Override
    public ClubEntity updateEntityFromRequest(ClubEntity club, UpdateClubRequest request) {
        StadiumEntity stadiumEntity = stadiumService.findById(request.getStadiumId())
                .orElseThrow(() -> new RuntimeException("Stadium not found with id: " + request.getStadiumId()));
        club.setClubName(request.getClubName());
        club.setStadium(stadiumEntity);
        return club;
    }
}