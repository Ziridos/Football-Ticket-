package nl.fontys.s3.ticketmaster.business.impl.clubimpl;

import nl.fontys.s3.ticketmaster.business.exception.InvalidClubException;
import nl.fontys.s3.ticketmaster.business.interfaces.clubinterfaces.ClubService;
import nl.fontys.s3.ticketmaster.business.interfaces.clubinterfaces.ClubValidator;
import nl.fontys.s3.ticketmaster.persitence.entity.ClubEntity;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class DeleteLogoUseCaseImplTest {
    @Mock
    private ClubService clubService;

    @Mock
    private ClubValidator clubValidator;

    @InjectMocks
    private DeleteLogoUseCaseImpl deleteLogoUseCase;

    private ClubEntity testClub;
    private static final Long CLUB_ID = 1L;

    @BeforeEach
    void setUp() {
        testClub = ClubEntity.builder()
                .id(CLUB_ID)
                .clubName("Test Club")
                .logo(new byte[]{1, 2, 3}) // Sample logo data
                .logoContentType("image/png")
                .build();
    }

    @Test
    void deleteLogo_Success() {
        // Arrange
        when(clubService.findById(CLUB_ID)).thenReturn(Optional.of(testClub));

        // Act
        deleteLogoUseCase.deleteLogo(CLUB_ID);

        // Assert
        assertNull(testClub.getLogo());
        assertNull(testClub.getLogoContentType());
        verify(clubService).save(testClub);
        verify(clubValidator).validateClubExists(CLUB_ID);
        verify(clubService).findById(CLUB_ID);
    }

    @Test
    void deleteLogo_ClubNotFound() {
        // Arrange
        when(clubService.findById(CLUB_ID)).thenReturn(Optional.empty());

        // Act & Assert
        InvalidClubException exception = assertThrows(InvalidClubException.class,
                () -> deleteLogoUseCase.deleteLogo(CLUB_ID));
        assertEquals("Club not found", exception.getMessage());
        verify(clubValidator).validateClubExists(CLUB_ID);
        verify(clubService).findById(CLUB_ID);
        verify(clubService, never()).save(any(ClubEntity.class));
    }

    @Test
    void deleteLogo_ValidationFails() {
        // Arrange
        doThrow(new InvalidClubException("Invalid club"))
                .when(clubValidator)
                .validateClubExists(CLUB_ID);

        // Act & Assert
        InvalidClubException exception = assertThrows(InvalidClubException.class,
                () -> deleteLogoUseCase.deleteLogo(CLUB_ID));
        assertEquals("Invalid club", exception.getMessage());
        verify(clubValidator).validateClubExists(CLUB_ID);
        verify(clubService, never()).findById(CLUB_ID);
        verify(clubService, never()).save(any(ClubEntity.class));
    }
}