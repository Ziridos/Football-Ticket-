package nl.fontys.s3.ticketmaster.business.impl.clubimpl;

import nl.fontys.s3.ticketmaster.business.exception.InvalidClubException;
import nl.fontys.s3.ticketmaster.business.exception.SameClubException;
import nl.fontys.s3.ticketmaster.business.interfaces.clubinterfaces.ClubService;
import nl.fontys.s3.ticketmaster.business.interfaces.stadiuminterfaces.StadiumValidator;
import nl.fontys.s3.ticketmaster.domain.club.CreateClubRequest;
import nl.fontys.s3.ticketmaster.domain.club.UpdateClubRequest;
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
class ClubValidatorImplTest {

    @Mock
    private ClubService clubServiceMock;

    @Mock
    private StadiumValidator stadiumValidatorMock;

    @InjectMocks
    private ClubValidatorImpl clubValidator;

    @Test
    void validateCreateClubRequest_shouldPassForValidRequest() {
        CreateClubRequest request = CreateClubRequest.builder()
                .clubName("New Club")
                .stadiumId(1L)
                .build();
        when(clubServiceMock.existsByName("New Club")).thenReturn(false);
        assertDoesNotThrow(() -> clubValidator.validateCreateClubRequest(request));
        verify(stadiumValidatorMock).validateStadiumExists(1L);
    }

    @Test
    void validateCreateClubRequest_shouldThrowExceptionForNullName() {
        CreateClubRequest request = CreateClubRequest.builder()
                .clubName(null)
                .stadiumId(1L)
                .build();
        assertThrows(InvalidClubException.class, () -> clubValidator.validateCreateClubRequest(request));
    }

    @Test
    void validateCreateClubRequest_shouldThrowExceptionForExistingName() {
        CreateClubRequest request = CreateClubRequest.builder()
                .clubName("Existing Club")
                .stadiumId(1L)
                .build();
        when(clubServiceMock.existsByName("Existing Club")).thenReturn(true);
        assertThrows(InvalidClubException.class, () -> clubValidator.validateCreateClubRequest(request));
    }

    @Test
    void validateUpdateClubRequest_shouldPassForValidRequest() {
        UpdateClubRequest request = UpdateClubRequest.builder()
                .clubName("Updated Club")
                .stadiumId(1L)
                .build();
        when(clubServiceMock.existsById(1L)).thenReturn(true);
        assertDoesNotThrow(() -> clubValidator.validateUpdateClubRequest(request, 1L));
        verify(stadiumValidatorMock).validateStadiumExists(1L);
    }

    @Test
    void validateClubExists_shouldPassForExistingClub() {
        when(clubServiceMock.existsById(1L)).thenReturn(true);
        assertDoesNotThrow(() -> clubValidator.validateClubExists(1L));
    }

    @Test
    void validateClubExists_shouldThrowExceptionForNonExistingClub() {
        when(clubServiceMock.existsById(1L)).thenReturn(false);
        assertThrows(InvalidClubException.class, () -> clubValidator.validateClubExists(1L));
    }

    @Test
    void validateClubs_shouldPassForDifferentClubs() {
        ClubEntity homeClub = ClubEntity.builder().id(1L).clubName("Home Club").build();
        ClubEntity awayClub = ClubEntity.builder().id(2L).clubName("Away Club").build();
        when(clubServiceMock.findByClubName("Home Club")).thenReturn(Optional.ofNullable(homeClub));
        when(clubServiceMock.findByClubName("Away Club")).thenReturn(Optional.ofNullable(awayClub));
        assertDoesNotThrow(() -> clubValidator.validateClubs("Home Club", "Away Club"));
    }

    @Test
    void validateClubs_shouldThrowExceptionForSameClubs() {
        ClubEntity club = ClubEntity.builder().id(1L).clubName("Same Club").build();
        when(clubServiceMock.findByClubName("Same Club")).thenReturn(Optional.ofNullable(club));
        assertThrows(SameClubException.class, () -> clubValidator.validateClubs("Same Club", "Same Club"));
    }

    @Test
    void validateClubs_shouldThrowExceptionForNonExistingHomeClub() {
        when(clubServiceMock.findByClubName("Non-existing Club")).thenReturn(Optional.empty());
        assertThrows(InvalidClubException.class, () -> clubValidator.validateClubs("Non-existing Club", "Away Club"));
    }

    @Test
    void validateClubs_shouldThrowExceptionForNonExistingAwayClub() {
        ClubEntity homeClub = ClubEntity.builder().id(1L).clubName("Home Club").build();
        when(clubServiceMock.findByClubName("Home Club")).thenReturn(Optional.of(homeClub));
        when(clubServiceMock.findByClubName("Non-existing Club")).thenReturn(Optional.empty());
        assertThrows(InvalidClubException.class, () -> clubValidator.validateClubs("Home Club", "Non-existing Club"));
    }
}