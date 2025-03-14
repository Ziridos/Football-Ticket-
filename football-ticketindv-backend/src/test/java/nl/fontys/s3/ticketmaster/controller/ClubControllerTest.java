package nl.fontys.s3.ticketmaster.controller;

import nl.fontys.s3.ticketmaster.business.interfaces.clubinterfaces.*;
import nl.fontys.s3.ticketmaster.domain.club.*;
import nl.fontys.s3.ticketmaster.persitence.entity.StadiumEntity;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.Arrays;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ClubControllerTest {

    @Mock
    private CreateClubUseCase createClubUseCase;
    @Mock
    private GetAllClubsUseCase getAllClubsUseCase;
    @Mock
    private GetClubUseCase getClubUseCase;
    @Mock
    private UpdateClubUseCase updateClubUseCase;
    @Mock
    private DeleteClubUseCase deleteClubUseCase;
    @Mock
    private UploadLogoUseCase uploadLogoUseCase;
    @Mock
    private DeleteLogoUseCase deleteLogoUseCase;

    @InjectMocks
    private ClubController clubController;

    private CreateClubRequest createRequest;
    private CreateClubResponse createResponse;
    private GetClubResponse getResponse;
    private UpdateClubRequest updateRequest;
    private UpdateClubResponse updateResponse;
    private GetAllClubsResponse getAllResponse;
    private StadiumEntity stadium;

    @BeforeEach
    void setUp() {
        stadium = StadiumEntity.builder()
                .id(1L)
                .stadiumName("Test Stadium")
                .stadiumAddress("123 Stadium St")
                .stadiumPostalCode("1234 AB")
                .stadiumCity("Test City")
                .stadiumCountry("Test Country")
                .build();

        createRequest = CreateClubRequest.builder()
                .clubName("Test Club")
                .stadiumId(1L)
                .build();

        createResponse = CreateClubResponse.builder()
                .clubId(1L)
                .clubName("Test Club")
                .build();

        getResponse = GetClubResponse.builder()
                .clubId(1L)
                .clubName("Test Club")
                .stadium(stadium)
                .build();

        updateRequest = UpdateClubRequest.builder()
                .clubName("Updated Club")
                .stadiumId(1L)
                .build();

        updateResponse = UpdateClubResponse.builder()
                .clubId(1L)
                .clubName("Updated Club")
                .stadium(stadium)
                .build();

        Club club = Club.builder()
                .clubId(1L)
                .clubName("Test Club")
                .stadium(stadium)
                .build();

        getAllResponse = GetAllClubsResponse.builder()
                .clubs(Arrays.asList(club))
                .totalElements(1)
                .totalPages(1)
                .currentPage(0)
                .build();
    }

    @Test
    void getAllClubs_WithDefaultParameters_ReturnsAllClubs() {
        // Arrange
        GetAllClubsRequest expectedRequest = GetAllClubsRequest.builder()
                .page(0)
                .size(10)
                .sortBy("id")
                .sortDirection("ASC")
                .build();

        when(getAllClubsUseCase.getAllClubs(expectedRequest))
                .thenReturn(getAllResponse);

        // Act
        ResponseEntity<GetAllClubsResponse> response = clubController.getAllClubs(
                null, null, null, null, 0, 10, "id", "ASC");

        // Assert
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals(1, response.getBody().getClubs().size());
        assertEquals(1, response.getBody().getTotalPages());
        assertEquals(0, response.getBody().getCurrentPage());
        verify(getAllClubsUseCase).getAllClubs(expectedRequest);
    }

    @Test
    void getAllClubs_WithFilters_ReturnsFilteredClubs() {
        // Arrange
        GetAllClubsRequest expectedRequest = GetAllClubsRequest.builder()
                .name("Test")
                .stadiumName("Stadium")
                .stadiumCity("City")
                .stadiumCountry("Country")
                .page(0)
                .size(10)
                .sortBy("name")
                .sortDirection("DESC")
                .build();

        when(getAllClubsUseCase.getAllClubs(expectedRequest))
                .thenReturn(getAllResponse);

        // Act
        ResponseEntity<GetAllClubsResponse> response = clubController.getAllClubs(
                "Test", "Stadium", "City", "Country", 0, 10, "name", "DESC");

        // Assert
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        verify(getAllClubsUseCase).getAllClubs(expectedRequest);
    }

    @Test
    void getAllClubs_WithPagination_ReturnsPagedResult() {
        // Arrange
        GetAllClubsRequest expectedRequest = GetAllClubsRequest.builder()
                .page(2)
                .size(5)
                .sortBy("id")
                .sortDirection("ASC")
                .build();

        GetAllClubsResponse pagedResponse = GetAllClubsResponse.builder()
                .clubs(Arrays.asList(new Club()))
                .totalElements(11)
                .totalPages(3)
                .currentPage(2)
                .build();

        when(getAllClubsUseCase.getAllClubs(expectedRequest))
                .thenReturn(pagedResponse);

        // Act
        ResponseEntity<GetAllClubsResponse> response = clubController.getAllClubs(
                null, null, null, null, 2, 5, "id", "ASC");

        // Assert
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals(2, response.getBody().getCurrentPage());
        assertEquals(3, response.getBody().getTotalPages());
        assertEquals(11, response.getBody().getTotalElements());
        verify(getAllClubsUseCase).getAllClubs(expectedRequest);
    }

    @Test
    void createClub_ValidRequest_ReturnsCreated() {
        // Arrange
        when(createClubUseCase.createClub(createRequest))
                .thenReturn(createResponse);

        // Act
        ResponseEntity<CreateClubResponse> response = clubController.createClub(createRequest);

        // Assert
        assertEquals(HttpStatus.CREATED, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals(createResponse, response.getBody());
        assertEquals("Test Club", response.getBody().getClubName());
        verify(createClubUseCase).createClub(createRequest);
    }

    @Test
    void getClub_ExistingClub_ReturnsClub() {
        // Arrange
        GetClubRequest request = new GetClubRequest(1L);
        when(getClubUseCase.getClub(request))
                .thenReturn(getResponse);

        // Act
        ResponseEntity<GetClubResponse> response = clubController.getClub(1L);

        // Assert
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals(getResponse, response.getBody());
        assertEquals("Test Club", response.getBody().getClubName());
        assertEquals(stadium, response.getBody().getStadium());
        verify(getClubUseCase).getClub(request);
    }

    @Test
    void updateClub_ValidRequest_ReturnsUpdatedClub() {
        // Arrange
        when(updateClubUseCase.updateClub(1L, updateRequest))
                .thenReturn(updateResponse);

        // Act
        ResponseEntity<UpdateClubResponse> response = clubController.updateClub(1L, updateRequest);

        // Assert
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals(updateResponse, response.getBody());
        assertEquals("Updated Club", response.getBody().getClubName());
        assertEquals(stadium, response.getBody().getStadium());
        verify(updateClubUseCase).updateClub(1L, updateRequest);
    }

    @Test
    void deleteClub_ExistingClub_ReturnsNoContent() {
        // Arrange
        doNothing().when(deleteClubUseCase).deleteClub(1L);

        // Act
        ResponseEntity<Void> response = clubController.deleteClub(1L);

        // Assert
        assertEquals(HttpStatus.NO_CONTENT, response.getStatusCode());
        assertNull(response.getBody());
        verify(deleteClubUseCase).deleteClub(1L);
    }

    @Test
    void createClub_InvalidRequest_UseCaseThrowsException() {
        // Arrange
        when(createClubUseCase.createClub(createRequest))
                .thenThrow(new IllegalArgumentException("Invalid club data"));

        // Act & Assert
        Exception exception = assertThrows(IllegalArgumentException.class,
                () -> clubController.createClub(createRequest));
        assertEquals("Invalid club data", exception.getMessage());
        verify(createClubUseCase).createClub(createRequest);
    }

    @Test
    void getClub_NonExistingClub_UseCaseThrowsException() {
        // Arrange
        GetClubRequest request = new GetClubRequest(999L);
        when(getClubUseCase.getClub(request))
                .thenThrow(new IllegalArgumentException("Club not found"));

        // Act & Assert
        Exception exception = assertThrows(IllegalArgumentException.class,
                () -> clubController.getClub(999L));
        assertEquals("Club not found", exception.getMessage());
        verify(getClubUseCase).getClub(request);
    }

    @Test
    void updateClub_InvalidRequest_UseCaseThrowsException() {
        // Arrange
        when(updateClubUseCase.updateClub(1L, updateRequest))
                .thenThrow(new IllegalArgumentException("Invalid update data"));

        // Act & Assert
        Exception exception = assertThrows(IllegalArgumentException.class,
                () -> clubController.updateClub(1L, updateRequest));
        assertEquals("Invalid update data", exception.getMessage());
        verify(updateClubUseCase).updateClub(1L, updateRequest);
    }

    @Test
    void deleteClub_NonExistingClub_UseCaseThrowsException() {
        // Arrange
        doThrow(new IllegalArgumentException("Club not found"))
                .when(deleteClubUseCase).deleteClub(999L);

        // Act & Assert
        Exception exception = assertThrows(IllegalArgumentException.class,
                () -> clubController.deleteClub(999L));
        assertEquals("Club not found", exception.getMessage());
        verify(deleteClubUseCase).deleteClub(999L);
    }

    @Test
    void deleteLogo_ExistingClub_ReturnsNoContent() {
        // Arrange
        doNothing().when(deleteLogoUseCase).deleteLogo(1L);

        // Act
        ResponseEntity<Void> response = clubController.deleteLogo(1L);

        // Assert
        assertEquals(HttpStatus.NO_CONTENT, response.getStatusCode());
        assertNull(response.getBody());
        verify(deleteLogoUseCase).deleteLogo(1L);
    }

    @Test
    void deleteLogo_NonExistingClub_UseCaseThrowsException() {
        // Arrange
        doThrow(new IllegalArgumentException("Club not found"))
                .when(deleteLogoUseCase).deleteLogo(999L);

        // Act & Assert
        Exception exception = assertThrows(IllegalArgumentException.class,
                () -> clubController.deleteLogo(999L));
        assertEquals("Club not found", exception.getMessage());
        verify(deleteLogoUseCase).deleteLogo(999L);
    }
}