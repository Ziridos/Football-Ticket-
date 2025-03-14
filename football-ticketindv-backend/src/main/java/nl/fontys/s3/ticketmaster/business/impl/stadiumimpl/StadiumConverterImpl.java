package nl.fontys.s3.ticketmaster.business.impl.stadiumimpl;

import nl.fontys.s3.ticketmaster.business.interfaces.stadiuminterfaces.StadiumConverter;
import nl.fontys.s3.ticketmaster.domain.stadium.*;
import nl.fontys.s3.ticketmaster.persitence.entity.StadiumEntity;
import org.springframework.stereotype.Component;

@Component
public class StadiumConverterImpl implements StadiumConverter {

    @Override
    public StadiumEntity convertToEntity(CreateStadiumRequest request) {
        return StadiumEntity.builder()
                .stadiumName(request.getStadiumName())
                .stadiumAddress(request.getStadiumAddress())
                .stadiumPostalCode(request.getStadiumPostalCode())
                .stadiumCity(request.getStadiumCity())
                .stadiumCountry(request.getStadiumCountry())
                .build();
    }

    @Override
    public CreateStadiumResponse convertToCreateResponse(StadiumEntity entity) {
        return CreateStadiumResponse.builder()
                .stadiumId(entity.getId())
                .stadiumName(entity.getStadiumName())
                .stadiumAddress(entity.getStadiumAddress())
                .stadiumPostalCode(entity.getStadiumPostalCode())
                .stadiumCity(entity.getStadiumCity())
                .stadiumCountry(entity.getStadiumCountry())
                .build();
    }

    @Override
    public GetStadiumResponse convertToGetResponse(StadiumEntity entity) {
        return GetStadiumResponse.builder()
                .stadiumId(entity.getId())
                .stadiumName(entity.getStadiumName())
                .stadiumAddress(entity.getStadiumAddress())
                .stadiumPostalCode(entity.getStadiumPostalCode())
                .stadiumCity(entity.getStadiumCity())
                .stadiumCountry(entity.getStadiumCountry())
                .build();
    }

    @Override
    public UpdateStadiumResponse convertToUpdateResponse(StadiumEntity entity) {
        return UpdateStadiumResponse.builder()
                .stadiumId(entity.getId())
                .stadiumName(entity.getStadiumName())
                .stadiumAddress(entity.getStadiumAddress())
                .stadiumPostalCode(entity.getStadiumPostalCode())
                .stadiumCity(entity.getStadiumCity())
                .stadiumCountry(entity.getStadiumCountry())
                .build();
    }

    @Override
    public Stadium convertToDomain(StadiumEntity entity) {
        return Stadium.builder()
                .stadiumId(entity.getId())
                .stadiumName(entity.getStadiumName())
                .stadiumAddress(entity.getStadiumAddress())
                .stadiumPostalCode(entity.getStadiumPostalCode())
                .stadiumCity(entity.getStadiumCity())
                .stadiumCountry(entity.getStadiumCountry())
                .build();
    }
}