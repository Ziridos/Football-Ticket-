package nl.fontys.s3.ticketmaster.business.impl.clubimpl;

import nl.fontys.s3.ticketmaster.business.interfaces.clubinterfaces.ClubConverter;
import nl.fontys.s3.ticketmaster.business.interfaces.clubinterfaces.ClubValidator;
import nl.fontys.s3.ticketmaster.domain.club.CreateClubRequest;
import nl.fontys.s3.ticketmaster.domain.club.CreateClubResponse;
import nl.fontys.s3.ticketmaster.persitence.ClubRepository;
import nl.fontys.s3.ticketmaster.persitence.entity.ClubEntity;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class CreateClubUseCaseImplTest {

    @Mock
    private ClubRepository clubRepositoryMock;

    @Mock
    private ClubConverter clubConverterMock;

    @Mock
    private ClubValidator clubValidatorMock;

    @InjectMocks
    private CreateClubUseCaseImpl createClubUseCase;

    @Test
    void createClub_shouldCreateClubSuccessfully() {
        // Arrange
        CreateClubRequest request = CreateClubRequest.builder()
                .clubName("New Club")
                .stadiumId(1L)
                .build();

        ClubEntity newClub = ClubEntity.builder()
                .clubName("New Club")
                .build();

        ClubEntity savedClub = ClubEntity.builder()
                .id(1L)
                .clubName("New Club")
                .build();

        CreateClubResponse expectedResponse = CreateClubResponse.builder()
                .clubId(1L)
                .clubName("New Club")
                .build();

        when(clubConverterMock.convertToEntity(request)).thenReturn(newClub);
        when(clubRepositoryMock.save(newClub)).thenReturn(savedClub);
        when(clubConverterMock.convertToCreateClubResponse(savedClub)).thenReturn(expectedResponse);

        // Act
        CreateClubResponse actualResponse = createClubUseCase.createClub(request);

        // Assert
        assertEquals(expectedResponse, actualResponse);
        verify(clubValidatorMock).validateCreateClubRequest(request);
        verify(clubConverterMock).convertToEntity(request);
        verify(clubRepositoryMock).save(newClub);
        verify(clubConverterMock).convertToCreateClubResponse(savedClub);
    }
}