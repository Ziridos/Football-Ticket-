package nl.fontys.s3.ticketmaster.controller;

import nl.fontys.s3.ticketmaster.business.exception.InvalidCompetitionException;
import nl.fontys.s3.ticketmaster.business.interfaces.competitioninterfaces.*;
import nl.fontys.s3.ticketmaster.domain.competition.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.Arrays;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class CompetitionControllerTest {

    @Mock
    private CreateCompetitionUseCase createCompetitionUseCase;
    @Mock
    private GetAllCompetitionsUseCase getAllCompetitionsUseCase;
    @Mock
    private GetCompetitionUseCase getCompetitionUseCase;
    @Mock
    private UpdateCompetitionUseCase updateCompetitionUseCase;
    @Mock
    private DeleteCompetitionUseCase deleteCompetitionUseCase;

    @InjectMocks
    private CompetitionController competitionController;

    private CompetitionControllerAdvice controllerAdvice;
    private CreateCompetitionRequest createRequest;
    private CreateCompetitionResponse createResponse;
    private GetCompetitionResponse getResponse;
    private UpdateCompetitionRequest updateRequest;
    private UpdateCompetitionResponse updateResponse;
    private GetAllCompetitionsResponse getAllResponse;

    @BeforeEach
    void setUp() {
        controllerAdvice = new CompetitionControllerAdvice();

        createRequest = CreateCompetitionRequest.builder()
                .competitionName("Test Competition")
                .build();

        createResponse = CreateCompetitionResponse.builder()
                .competitionId(1L)
                .competitionName("Test Competition")
                .build();

        getResponse = GetCompetitionResponse.builder()
                .competitionId(1L)
                .competitionName("Test Competition")
                .build();

        updateRequest = UpdateCompetitionRequest.builder()
                .competitionName("Updated Competition")
                .build();

        updateResponse = UpdateCompetitionResponse.builder()
                .competitionId(1L)
                .competitionName("Updated Competition")
                .build();

        Competition competition = Competition.builder()
                .competitionId(1L)
                .competitionName("Test Competition")
                .build();

        getAllResponse = GetAllCompetitionsResponse.builder()
                .competitions(Arrays.asList(competition))
                .totalElements(1)
                .totalPages(1)
                .currentPage(0)
                .build();
    }

    @Test
    void getAllCompetitions_WithDefaultParameters_ReturnsAllCompetitions() {
        // Arrange
        GetAllCompetitionsRequest expectedRequest = GetAllCompetitionsRequest.builder()
                .page(0)
                .size(10)
                .sortBy("id")
                .sortDirection("ASC")
                .build();

        when(getAllCompetitionsUseCase.getAllCompetitions(expectedRequest))
                .thenReturn(getAllResponse);

        // Act
        ResponseEntity<GetAllCompetitionsResponse> response = competitionController.getAllCompetitions(
                null, 0, 10, "id", "ASC");

        // Assert
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals(1, response.getBody().getCompetitions().size());
        assertEquals(1, response.getBody().getTotalPages());
        assertEquals(0, response.getBody().getCurrentPage());
        verify(getAllCompetitionsUseCase).getAllCompetitions(expectedRequest);
    }

    @Test
    void getAllCompetitions_WithFilters_ReturnsFilteredCompetitions() {
        // Arrange
        GetAllCompetitionsRequest expectedRequest = GetAllCompetitionsRequest.builder()
                .name("Test")
                .page(0)
                .size(10)
                .sortBy("name")
                .sortDirection("DESC")
                .build();

        when(getAllCompetitionsUseCase.getAllCompetitions(expectedRequest))
                .thenReturn(getAllResponse);

        // Act
        ResponseEntity<GetAllCompetitionsResponse> response = competitionController.getAllCompetitions(
                "Test", 0, 10, "name", "DESC");

        // Assert
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        verify(getAllCompetitionsUseCase).getAllCompetitions(expectedRequest);
    }

    @Test
    void getAllCompetitions_WithPagination_ReturnsPagedResult() {
        // Arrange
        GetAllCompetitionsRequest expectedRequest = GetAllCompetitionsRequest.builder()
                .page(2)
                .size(5)
                .sortBy("id")
                .sortDirection("ASC")
                .build();

        GetAllCompetitionsResponse pagedResponse = GetAllCompetitionsResponse.builder()
                .competitions(Arrays.asList(new Competition()))
                .totalElements(11)
                .totalPages(3)
                .currentPage(2)
                .build();

        when(getAllCompetitionsUseCase.getAllCompetitions(expectedRequest))
                .thenReturn(pagedResponse);

        // Act
        ResponseEntity<GetAllCompetitionsResponse> response = competitionController.getAllCompetitions(
                null, 2, 5, "id", "ASC");

        // Assert
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals(2, response.getBody().getCurrentPage());
        assertEquals(3, response.getBody().getTotalPages());
        assertEquals(11, response.getBody().getTotalElements());
        verify(getAllCompetitionsUseCase).getAllCompetitions(expectedRequest);
    }

    @Test
    void createCompetition_ValidRequest_ReturnsCreated() {
        when(createCompetitionUseCase.createCompetition(createRequest))
                .thenReturn(createResponse);

        ResponseEntity<CreateCompetitionResponse> response = competitionController.createCompetition(createRequest);

        assertEquals(HttpStatus.CREATED, response.getStatusCode());
        assertEquals(createResponse, response.getBody());
        verify(createCompetitionUseCase).createCompetition(createRequest);
    }

    private static Stream<Arguments> provideCreateCompetitionExceptionCases() {
        return Stream.of(
                Arguments.of(
                        new InvalidCompetitionException("Invalid competition name"),
                        HttpStatus.BAD_REQUEST,
                        "Invalid Request",
                        "Invalid competition name"
                ),
                Arguments.of(
                        new RuntimeException("Unexpected error"),
                        HttpStatus.INTERNAL_SERVER_ERROR,
                        "Internal Server Error",
                        "An unexpected error occurred."
                ),
                Arguments.of(
                        new InvalidCompetitionException("Competition not found"),
                        HttpStatus.BAD_REQUEST,
                        "Invalid Request",
                        "Competition not found"
                )
        );
    }

    @ParameterizedTest
    @MethodSource("provideCreateCompetitionExceptionCases")
    void handleCreateCompetitionExceptions(
            Exception exception,
            HttpStatus expectedStatus,
            String expectedError,
            String expectedMessage) {

        ResponseEntity<ErrorResponse> response;

        if (exception instanceof InvalidCompetitionException) {
            response = controllerAdvice.handleInvalidCompetitionException((InvalidCompetitionException) exception);
        } else {
            response = controllerAdvice.handleGeneralException(exception);
        }

        assertEquals(expectedStatus, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals(expectedError, response.getBody().getError());
        assertEquals(expectedMessage, response.getBody().getMessage());
    }

    @Test
    void getCompetition_ExistingCompetition_ReturnsCompetition() {
        GetCompetitionRequest request = GetCompetitionRequest.builder()
                .competitionId(1L)
                .build();
        when(getCompetitionUseCase.getCompetition(request))
                .thenReturn(getResponse);

        ResponseEntity<GetCompetitionResponse> response = competitionController.getCompetition(1L);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals(getResponse, response.getBody());
        assertEquals("Test Competition", response.getBody().getCompetitionName());
        verify(getCompetitionUseCase).getCompetition(request);
    }

    @Test
    void updateCompetition_ValidRequest_ReturnsUpdatedCompetition() {
        UpdateCompetitionRequest fullRequest = UpdateCompetitionRequest.builder()
                .competitionId(1L)
                .competitionName(updateRequest.getCompetitionName())
                .build();

        when(updateCompetitionUseCase.updateCompetition(fullRequest))
                .thenReturn(updateResponse);

        ResponseEntity<UpdateCompetitionResponse> response =
                competitionController.updateCompetition(1L, updateRequest);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals(updateResponse, response.getBody());
        assertEquals("Updated Competition", response.getBody().getCompetitionName());
        verify(updateCompetitionUseCase).updateCompetition(fullRequest);
    }

    @Test
    void deleteCompetition_ExistingCompetition_ReturnsNoContent() {
        doNothing().when(deleteCompetitionUseCase).deleteCompetitionById(1L);

        ResponseEntity<Void> response = competitionController.deleteCompetition(1L);

        assertEquals(HttpStatus.NO_CONTENT, response.getStatusCode());
        assertNull(response.getBody());
        verify(deleteCompetitionUseCase).deleteCompetitionById(1L);
    }

    @Test
    void updateCompetition_InvalidRequest_ReturnsBadRequest() {
        InvalidCompetitionException exception = new InvalidCompetitionException("Invalid update data");

        ResponseEntity<ErrorResponse> response = controllerAdvice.handleInvalidCompetitionException(exception);

        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals("Invalid Request", response.getBody().getError());
        assertEquals("Invalid update data", response.getBody().getMessage());
    }
}