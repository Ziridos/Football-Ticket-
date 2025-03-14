package nl.fontys.s3.ticketmaster.business.impl.clubimpl;

import nl.fontys.s3.ticketmaster.business.interfaces.clubinterfaces.ClubConverter;
import nl.fontys.s3.ticketmaster.business.interfaces.clubinterfaces.ClubValidator;
import nl.fontys.s3.ticketmaster.domain.club.UpdateClubRequest;
import nl.fontys.s3.ticketmaster.domain.club.UpdateClubResponse;
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
class UpdateClubUseCaseImplTest {

    @Mock
    private ClubRepository clubRepositoryMock;

    @Mock
    private ClubConverter clubConverterMock;

    @Mock
    private ClubValidator clubValidatorMock;

    @InjectMocks
    private UpdateClubUseCaseImpl updateClubUseCase;

    @Test
    void updateClub_shouldUpdateClubSuccessfully() {
        // Arrange
        Long clubId = 1L;
        UpdateClubRequest request = UpdateClubRequest.builder()
                .clubName("Updated Club")
                .stadiumId(2L)
                .build();
        ClubEntity existingClub = ClubEntity.builder()
                .id(clubId)
                .clubName("Old Club")
                .build();
        ClubEntity updatedClub = ClubEntity.builder()
                .id(clubId)
                .clubName("Updated Club")
                .build();
        UpdateClubResponse expectedResponse = UpdateClubResponse.builder()
                .clubId(clubId)
                .clubName("Updated Club")
                .build();

        when(clubRepositoryMock.findById(clubId)).thenReturn(Optional.ofNullable(existingClub));
        when(clubConverterMock.updateEntityFromRequest(existingClub, request)).thenReturn(updatedClub);
        when(clubRepositoryMock.save(updatedClub)).thenReturn(updatedClub);
        when(clubConverterMock.convertToUpdateClubResponse(updatedClub)).thenReturn(expectedResponse);

        // Act
        UpdateClubResponse response = updateClubUseCase.updateClub(clubId, request);

        // Assert
        assertNotNull(response);
        assertEquals(expectedResponse, response);
        verify(clubValidatorMock).validateUpdateClubRequest(request, clubId);
        verify(clubRepositoryMock).findById(clubId);
        verify(clubConverterMock).updateEntityFromRequest(existingClub, request);
        verify(clubRepositoryMock).save(updatedClub);
        verify(clubConverterMock).convertToUpdateClubResponse(updatedClub);
    }
}