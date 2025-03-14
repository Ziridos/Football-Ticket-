package nl.fontys.s3.ticketmaster.business.impl.clubimpl;

import nl.fontys.s3.ticketmaster.business.interfaces.clubinterfaces.ClubConverter;
import nl.fontys.s3.ticketmaster.business.interfaces.clubinterfaces.ClubValidator;
import nl.fontys.s3.ticketmaster.domain.club.GetClubRequest;
import nl.fontys.s3.ticketmaster.domain.club.GetClubResponse;
import nl.fontys.s3.ticketmaster.persitence.ClubRepository;
import nl.fontys.s3.ticketmaster.persitence.entity.ClubEntity;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class GetClubUseCaseImplTest {

    @Mock
    private ClubRepository clubRepositoryMock;

    @Mock
    private ClubConverter clubConverterMock;

    @Mock
    private ClubValidator clubValidatorMock;

    @InjectMocks
    private GetClubUseCaseImpl getClubUseCase;

    @Test
    void getClub_shouldReturnClub() {
        // Arrange
        Long clubId = 1L;
        GetClubRequest request = new GetClubRequest(clubId);
        ClubEntity clubEntity = ClubEntity.builder()
                .id(clubId)
                .clubName("Test Club")
                .build();
        GetClubResponse expectedResponse = GetClubResponse.builder()
                .clubId(clubId)
                .clubName("Test Club")
                .build();

        when(clubRepositoryMock.findById(clubId)).thenReturn(Optional.ofNullable(clubEntity));
        when(clubConverterMock.convertToGetClubResponse(clubEntity)).thenReturn(expectedResponse);

        // Act
        GetClubResponse response = getClubUseCase.getClub(request);

        // Assert
        assertNotNull(response);
        assertEquals(expectedResponse, response);
        verify(clubValidatorMock).validateClubExists(clubId);
        verify(clubRepositoryMock).findById(clubId);
        verify(clubConverterMock).convertToGetClubResponse(clubEntity);
    }
}