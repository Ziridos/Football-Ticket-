package nl.fontys.s3.ticketmaster.business.impl.clubimpl;

import nl.fontys.s3.ticketmaster.business.interfaces.stadiuminterfaces.StadiumService;
import nl.fontys.s3.ticketmaster.domain.club.*;
import nl.fontys.s3.ticketmaster.persitence.entity.ClubEntity;
import nl.fontys.s3.ticketmaster.persitence.entity.StadiumEntity;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ClubConverterImplTest {

    @Mock
    private StadiumService stadiumServiceMock;

    @InjectMocks
    private ClubConverterImpl clubConverter;

    @Test
    void convertToEntity_shouldConvertCreateRequestToEntity() {
        CreateClubRequest request = CreateClubRequest.builder()
                .clubName("Test Club")
                .stadiumId(1L)
                .build();
        StadiumEntity stadiumEntity = StadiumEntity.builder().id(1L).build();
        when(stadiumServiceMock.findById(1L)).thenReturn(Optional.ofNullable(stadiumEntity));

        ClubEntity result = clubConverter.convertToEntity(request);

        assertEquals("Test Club", result.getClubName());
        assertEquals(stadiumEntity, result.getStadium());
    }

    @Test
    void convertToCreateClubResponse_shouldConvertEntityToCreateResponse() {
        ClubEntity clubEntity = ClubEntity.builder()
                .id(1L)
                .clubName("Test Club")
                .build();

        CreateClubResponse result = clubConverter.convertToCreateClubResponse(clubEntity);

        assertEquals(1L, result.getClubId());
        assertEquals("Test Club", result.getClubName());
    }

    @Test
    void convertToGetClubResponse_shouldConvertEntityToGetResponse() {
        StadiumEntity stadiumEntity = StadiumEntity.builder().id(1L).build();
        ClubEntity clubEntity = ClubEntity.builder()
                .id(1L)
                .clubName("Test Club")
                .stadium(stadiumEntity)
                .build();

        GetClubResponse result = clubConverter.convertToGetClubResponse(clubEntity);

        assertEquals(1L, result.getClubId());
        assertEquals("Test Club", result.getClubName());
        assertEquals(stadiumEntity, result.getStadium());
    }

    @Test
    void convertToUpdateClubResponse_shouldConvertEntityToUpdateResponse() {
        StadiumEntity stadiumEntity = StadiumEntity.builder().id(1L).build();
        ClubEntity clubEntity = ClubEntity.builder()
                .id(1L)
                .clubName("Test Club")
                .stadium(stadiumEntity)
                .build();

        UpdateClubResponse result = clubConverter.convertToUpdateClubResponse(clubEntity);

        assertEquals(1L, result.getClubId());
        assertEquals("Test Club", result.getClubName());
        assertEquals(stadiumEntity, result.getStadium());
    }

    @Test
    void convertToClub_shouldConvertEntityToClub() {
        StadiumEntity stadiumEntity = StadiumEntity.builder().id(1L).build();
        ClubEntity clubEntity = ClubEntity.builder()
                .id(1L)
                .clubName("Test Club")
                .stadium(stadiumEntity)
                .build();

        Club result = clubConverter.convertToClub(clubEntity);

        assertEquals(1L, result.getClubId());
        assertEquals("Test Club", result.getClubName());
        assertEquals(stadiumEntity, result.getStadium());
    }

    @Test
    void updateEntityFromRequest_shouldUpdateEntityWithRequestData() {
        ClubEntity club = ClubEntity.builder()
                .id(1L)
                .clubName("Old Club")
                .build();
        UpdateClubRequest request = UpdateClubRequest.builder()
                .clubName("Updated Club")
                .stadiumId(2L)
                .build();
        StadiumEntity newStadium = StadiumEntity.builder().id(2L).build();
        when(stadiumServiceMock.findById(2L)).thenReturn(Optional.ofNullable(newStadium));

        ClubEntity result = clubConverter.updateEntityFromRequest(club, request);

        assertEquals(1L, result.getId());
        assertEquals("Updated Club", result.getClubName());
        assertEquals(newStadium, result.getStadium());
    }
}