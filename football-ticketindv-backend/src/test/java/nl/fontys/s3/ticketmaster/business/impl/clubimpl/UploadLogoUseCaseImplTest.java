package nl.fontys.s3.ticketmaster.business.impl.clubimpl;

import nl.fontys.s3.ticketmaster.business.exception.InvalidClubException;
import nl.fontys.s3.ticketmaster.business.exception.LogoUploadException;
import nl.fontys.s3.ticketmaster.business.interfaces.clubinterfaces.ClubService;
import nl.fontys.s3.ticketmaster.business.interfaces.clubinterfaces.ClubValidator;
import nl.fontys.s3.ticketmaster.domain.club.UploadLogoRequest;
import nl.fontys.s3.ticketmaster.persitence.entity.ClubEntity;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class UploadLogoUseCaseImplTest {
    @Mock
    private ClubService clubService;

    @Mock
    private ClubValidator clubValidator;

    @InjectMocks
    private UploadLogoUseCaseImpl uploadLogoUseCase;

    private ClubEntity testClub;
    private MultipartFile testFile;
    private UploadLogoRequest request;
    private static final Long CLUB_ID = 1L;
    private static final byte[] FILE_CONTENT = "test image content".getBytes();

    @BeforeEach
    void setUp() {
        testClub = ClubEntity.builder()
                .id(CLUB_ID)
                .clubName("Test Club")
                .build();

        testFile = new MockMultipartFile(
                "file",
                "test-image.png",
                "image/png",
                FILE_CONTENT
        );

        request = new UploadLogoRequest(testFile);
    }

    @Test
    void uploadLogo_Success() {
        // Arrange
        when(clubService.findById(CLUB_ID)).thenReturn(Optional.of(testClub));

        // Act
        uploadLogoUseCase.uploadLogo(CLUB_ID, request);

        // Assert
        verify(clubValidator).validateClubExists(CLUB_ID);
        verify(clubService).findById(CLUB_ID);
        verify(clubService).save(testClub);

        assertEquals("image/png", testClub.getLogoContentType());
        assertArrayEquals(FILE_CONTENT, testClub.getLogo());
    }

    @Test
    void uploadLogo_ClubNotFound() {
        // Arrange
        when(clubService.findById(CLUB_ID)).thenReturn(Optional.empty());

        // Act & Assert
        InvalidClubException exception = assertThrows(InvalidClubException.class,
                () -> uploadLogoUseCase.uploadLogo(CLUB_ID, request));

        assertEquals("Club not found", exception.getMessage());
        verify(clubValidator).validateClubExists(CLUB_ID);
        verify(clubService).findById(CLUB_ID);
        verify(clubService, never()).save(any(ClubEntity.class));
    }

    @Test
    void uploadLogo_ValidationFails() {
        // Arrange
        doThrow(new InvalidClubException("Invalid club"))
                .when(clubValidator)
                .validateClubExists(CLUB_ID);

        // Act & Assert
        InvalidClubException exception = assertThrows(InvalidClubException.class,
                () -> uploadLogoUseCase.uploadLogo(CLUB_ID, request));

        assertEquals("Invalid club", exception.getMessage());
        verify(clubValidator).validateClubExists(CLUB_ID);
        verify(clubService, never()).findById(CLUB_ID);
        verify(clubService, never()).save(any(ClubEntity.class));
    }

    @Test
    void uploadLogo_IOExceptionThrown() throws IOException {
        // Arrange
        MultipartFile mockFile = mock(MultipartFile.class);
        when(mockFile.getBytes()).thenThrow(new IOException("Failed to read file"));
        UploadLogoRequest badRequest = new UploadLogoRequest(mockFile);
        when(clubService.findById(CLUB_ID)).thenReturn(Optional.of(testClub));

        // Act & Assert
        LogoUploadException exception = assertThrows(LogoUploadException.class,
                () -> uploadLogoUseCase.uploadLogo(CLUB_ID, badRequest));

        assertEquals("Failed to process and store club logo", exception.getMessage());
        verify(clubValidator).validateClubExists(CLUB_ID);
        verify(clubService).findById(CLUB_ID);
        verify(clubService, never()).save(any(ClubEntity.class));
    }
}