package nl.fontys.s3.ticketmaster.business.impl.stadiumimpl;

import nl.fontys.s3.ticketmaster.domain.stadium.*;
import nl.fontys.s3.ticketmaster.persitence.entity.StadiumEntity;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class StadiumConverterImplTest {

    private final StadiumConverterImpl stadiumConverter = new StadiumConverterImpl();

    @Test
    void convertToEntity_shouldConvertCreateRequestToEntity() {
        CreateStadiumRequest request = CreateStadiumRequest.builder()
                .stadiumName("Test Stadium")
                .stadiumAddress("123 Test St")
                .stadiumPostalCode("1234 AB")
                .stadiumCity("Test City")
                .stadiumCountry("Test Country")
                .build();

        StadiumEntity result = stadiumConverter.convertToEntity(request);

        assertEquals(request.getStadiumName(), result.getStadiumName());
        assertEquals(request.getStadiumAddress(), result.getStadiumAddress());
        assertEquals(request.getStadiumPostalCode(), result.getStadiumPostalCode());
        assertEquals(request.getStadiumCity(), result.getStadiumCity());
        assertEquals(request.getStadiumCountry(), result.getStadiumCountry());
    }

    @Test
    void convertToCreateResponse_shouldConvertEntityToCreateResponse() {
        StadiumEntity entity = StadiumEntity.builder()
                .id(1L)
                .stadiumName("Test Stadium")
                .stadiumAddress("123 Test St")
                .stadiumPostalCode("1234 AB")
                .stadiumCity("Test City")
                .stadiumCountry("Test Country")
                .build();

        CreateStadiumResponse result = stadiumConverter.convertToCreateResponse(entity);

        assertEquals(entity.getId(), result.getStadiumId());
        assertEquals(entity.getStadiumName(), result.getStadiumName());
        assertEquals(entity.getStadiumAddress(), result.getStadiumAddress());
        assertEquals(entity.getStadiumPostalCode(), result.getStadiumPostalCode());
        assertEquals(entity.getStadiumCity(), result.getStadiumCity());
        assertEquals(entity.getStadiumCountry(), result.getStadiumCountry());
    }

    @Test
    void convertToGetResponse_shouldConvertEntityToGetResponse() {
        StadiumEntity entity = StadiumEntity.builder()
                .id(1L)
                .stadiumName("Test Stadium")
                .stadiumAddress("123 Test St")
                .stadiumPostalCode("1234 AB")
                .stadiumCity("Test City")
                .stadiumCountry("Test Country")
                .build();

        GetStadiumResponse result = stadiumConverter.convertToGetResponse(entity);

        assertEquals(entity.getId(), result.getStadiumId());
        assertEquals(entity.getStadiumName(), result.getStadiumName());
        assertEquals(entity.getStadiumAddress(), result.getStadiumAddress());
        assertEquals(entity.getStadiumPostalCode(), result.getStadiumPostalCode());
        assertEquals(entity.getStadiumCity(), result.getStadiumCity());
        assertEquals(entity.getStadiumCountry(), result.getStadiumCountry());
    }

    @Test
    void convertToUpdateResponse_shouldConvertEntityToUpdateResponse() {
        StadiumEntity entity = StadiumEntity.builder()
                .id(1L)
                .stadiumName("Test Stadium")
                .stadiumAddress("123 Test St")
                .stadiumPostalCode("1234 AB")
                .stadiumCity("Test City")
                .stadiumCountry("Test Country")
                .build();

        UpdateStadiumResponse result = stadiumConverter.convertToUpdateResponse(entity);

        assertEquals(entity.getId(), result.getStadiumId());
        assertEquals(entity.getStadiumName(), result.getStadiumName());
        assertEquals(entity.getStadiumAddress(), result.getStadiumAddress());
        assertEquals(entity.getStadiumPostalCode(), result.getStadiumPostalCode());
        assertEquals(entity.getStadiumCity(), result.getStadiumCity());
        assertEquals(entity.getStadiumCountry(), result.getStadiumCountry());
    }

    @Test
    void convertToDomain_shouldConvertEntityToDomain() {
        StadiumEntity entity = StadiumEntity.builder()
                .id(1L)
                .stadiumName("Test Stadium")
                .stadiumAddress("123 Test St")
                .stadiumPostalCode("1234 AB")
                .stadiumCity("Test City")
                .stadiumCountry("Test Country")
                .build();

        Stadium result = stadiumConverter.convertToDomain(entity);

        assertEquals(entity.getId(), result.getStadiumId());
        assertEquals(entity.getStadiumName(), result.getStadiumName());
        assertEquals(entity.getStadiumAddress(), result.getStadiumAddress());
        assertEquals(entity.getStadiumPostalCode(), result.getStadiumPostalCode());
        assertEquals(entity.getStadiumCity(), result.getStadiumCity());
        assertEquals(entity.getStadiumCountry(), result.getStadiumCountry());
    }
}