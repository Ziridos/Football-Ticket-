package nl.fontys.s3.ticketmaster.controller;

import nl.fontys.s3.ticketmaster.business.interfaces.stadiuminterfaces.*;
import nl.fontys.s3.ticketmaster.domain.stadium.*;
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
class StadiumControllerTest {

    @Mock
    private CreateStadiumUseCase createStadiumUseCase;
    @Mock
    private GetAllStadiumsUseCase getAllStadiumsUseCase;
    @Mock
    private GetStadiumUseCase getStadiumUseCase;
    @Mock
    private UpdateStadiumUseCase updateStadiumUseCase;
    @Mock
    private DeleteStadiumUseCase deleteStadiumUseCase;

    @InjectMocks
    private StadiumController stadiumController;

    private CreateStadiumRequest createStadiumRequest;
    private CreateStadiumResponse createStadiumResponse;
    private GetStadiumResponse getStadiumResponse;
    private UpdateStadiumRequest updateStadiumRequest;
    private UpdateStadiumResponse updateStadiumResponse;
    private GetAllStadiumsResponse getAllStadiumsResponse;

    @BeforeEach
    void setUp() {
        createStadiumRequest = CreateStadiumRequest.builder()
                .stadiumName("Test Stadium")
                .stadiumAddress("123 Stadium St")
                .stadiumPostalCode("1234 AB")
                .stadiumCity("Test City")
                .stadiumCountry("Test Country")
                .build();

        createStadiumResponse = CreateStadiumResponse.builder()
                .stadiumId(1L)
                .stadiumName("Test Stadium")
                .build();

        getStadiumResponse = GetStadiumResponse.builder()
                .stadiumId(1L)
                .stadiumName("Test Stadium")
                .stadiumAddress("123 Stadium St")
                .stadiumPostalCode("1234 AB")
                .stadiumCity("Test City")
                .stadiumCountry("Test Country")
                .build();

        updateStadiumRequest = UpdateStadiumRequest.builder()
                .stadiumName("Updated Stadium")
                .stadiumAddress("456 Stadium Ave")
                .stadiumPostalCode("5678 CD")
                .stadiumCity("New City")
                .stadiumCountry("New Country")
                .build();

        updateStadiumResponse = UpdateStadiumResponse.builder()
                .stadiumId(1L)
                .stadiumName("Updated Stadium")
                .stadiumAddress("456 Stadium Ave")
                .stadiumPostalCode("5678 CD")
                .stadiumCity("New City")
                .stadiumCountry("New Country")
                .build();

        Stadium stadium = Stadium.builder()
                .stadiumId(1L)
                .stadiumName("Test Stadium")
                .stadiumAddress("123 Stadium St")
                .stadiumPostalCode("1234 AB")
                .stadiumCity("Test City")
                .stadiumCountry("Test Country")
                .build();

        getAllStadiumsResponse = GetAllStadiumsResponse.builder()
                .stadiums(Arrays.asList(stadium))
                .totalElements(1)
                .totalPages(1)
                .currentPage(0)
                .build();
    }

    @Test
    void getAllStadiums_WithDefaultParameters_ReturnsAllStadiums() {
        // Arrange
        GetAllStadiumsRequest expectedRequest = GetAllStadiumsRequest.builder()
                .page(0)
                .size(10)
                .sortBy("id")
                .sortDirection("ASC")
                .build();

        when(getAllStadiumsUseCase.getAllStadiums(expectedRequest))
                .thenReturn(getAllStadiumsResponse);

        // Act
        ResponseEntity<GetAllStadiumsResponse> response = stadiumController.getAllStadiums(
                null, null, null, 0, 10, "id", "ASC");

        // Assert
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals(1, response.getBody().getStadiums().size());
        assertEquals(1, response.getBody().getTotalPages());
        assertEquals(0, response.getBody().getCurrentPage());
        verify(getAllStadiumsUseCase).getAllStadiums(expectedRequest);
    }

    @Test
    void getAllStadiums_WithFilters_ReturnsFilteredStadiums() {
        // Arrange
        GetAllStadiumsRequest expectedRequest = GetAllStadiumsRequest.builder()
                .name("Test Stadium")
                .city("Test City")
                .country("Test Country")
                .page(0)
                .size(10)
                .sortBy("name")
                .sortDirection("DESC")
                .build();

        when(getAllStadiumsUseCase.getAllStadiums(expectedRequest))
                .thenReturn(getAllStadiumsResponse);

        // Act
        ResponseEntity<GetAllStadiumsResponse> response = stadiumController.getAllStadiums(
                "Test Stadium", "Test City", "Test Country", 0, 10, "name", "DESC");

        // Assert
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        verify(getAllStadiumsUseCase).getAllStadiums(expectedRequest);
    }

    @Test
    void getAllStadiums_WithPagination_ReturnsPagedResult() {
        // Arrange
        GetAllStadiumsRequest expectedRequest = GetAllStadiumsRequest.builder()
                .page(2)
                .size(5)
                .sortBy("id")
                .sortDirection("ASC")
                .build();

        GetAllStadiumsResponse pagedResponse = GetAllStadiumsResponse.builder()
                .stadiums(Arrays.asList(new Stadium()))
                .totalElements(11)
                .totalPages(3)
                .currentPage(2)
                .build();

        when(getAllStadiumsUseCase.getAllStadiums(expectedRequest))
                .thenReturn(pagedResponse);

        // Act
        ResponseEntity<GetAllStadiumsResponse> response = stadiumController.getAllStadiums(
                null, null, null, 2, 5, "id", "ASC");

        // Assert
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals(2, response.getBody().getCurrentPage());
        assertEquals(3, response.getBody().getTotalPages());
        assertEquals(11, response.getBody().getTotalElements());
        verify(getAllStadiumsUseCase).getAllStadiums(expectedRequest);
    }

    // Rest of existing tests...
    @Test
    void createStadium_ValidRequest_ReturnsCreatedStadium() {
        when(createStadiumUseCase.createStadium(createStadiumRequest))
                .thenReturn(createStadiumResponse);

        ResponseEntity<CreateStadiumResponse> response = stadiumController.createStadium(createStadiumRequest);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals(createStadiumResponse, response.getBody());
        assertEquals(1L, response.getBody().getStadiumId());
        verify(createStadiumUseCase).createStadium(createStadiumRequest);
    }

    @Test
    void getStadium_ExistingStadium_ReturnsStadium() {
        GetStadiumRequest request = new GetStadiumRequest(1L);
        when(getStadiumUseCase.getStadiumById(request))
                .thenReturn(getStadiumResponse);

        ResponseEntity<GetStadiumResponse> response = stadiumController.getStadium(1L);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals(getStadiumResponse, response.getBody());
        assertEquals(1L, response.getBody().getStadiumId());
        verify(getStadiumUseCase).getStadiumById(request);
    }

    @Test
    void updateStadium_ValidRequest_ReturnsUpdatedStadium() {
        when(updateStadiumUseCase.updateStadium(1L, updateStadiumRequest))
                .thenReturn(updateStadiumResponse);

        ResponseEntity<UpdateStadiumResponse> response =
                stadiumController.updateStadium(1L, updateStadiumRequest);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals(updateStadiumResponse, response.getBody());
        assertEquals("Updated Stadium", response.getBody().getStadiumName());
        verify(updateStadiumUseCase).updateStadium(1L, updateStadiumRequest);
    }

    @Test
    void deleteStadium_ExistingStadium_ReturnsNoContent() {
        doNothing().when(deleteStadiumUseCase).deleteStadiumById(1L);

        ResponseEntity<Void> response = stadiumController.deleteStadium(1L);

        assertEquals(HttpStatus.NO_CONTENT, response.getStatusCode());
        assertNull(response.getBody());
        verify(deleteStadiumUseCase).deleteStadiumById(1L);
    }

    @Test
    void createStadium_InvalidRequest_UseCaseThrowsException() {
        when(createStadiumUseCase.createStadium(createStadiumRequest))
                .thenThrow(new IllegalArgumentException("Invalid stadium data"));

        Exception exception = assertThrows(IllegalArgumentException.class,
                () -> stadiumController.createStadium(createStadiumRequest));
        assertEquals("Invalid stadium data", exception.getMessage());
        verify(createStadiumUseCase).createStadium(createStadiumRequest);
    }

    @Test
    void getStadium_NonExistingStadium_UseCaseThrowsException() {
        GetStadiumRequest request = new GetStadiumRequest(999L);
        when(getStadiumUseCase.getStadiumById(request))
                .thenThrow(new IllegalArgumentException("Stadium not found"));

        Exception exception = assertThrows(IllegalArgumentException.class,
                () -> stadiumController.getStadium(999L));
        assertEquals("Stadium not found", exception.getMessage());
        verify(getStadiumUseCase).getStadiumById(request);
    }

    @Test
    void updateStadium_InvalidRequest_UseCaseThrowsException() {
        when(updateStadiumUseCase.updateStadium(1L, updateStadiumRequest))
                .thenThrow(new IllegalArgumentException("Invalid update data"));

        Exception exception = assertThrows(IllegalArgumentException.class,
                () -> stadiumController.updateStadium(1L, updateStadiumRequest));
        assertEquals("Invalid update data", exception.getMessage());
        verify(updateStadiumUseCase).updateStadium(1L, updateStadiumRequest);
    }
}