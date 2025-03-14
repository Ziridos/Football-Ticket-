package nl.fontys.s3.ticketmaster.controller;

import nl.fontys.s3.ticketmaster.business.interfaces.ticketinterfaces.*;
import nl.fontys.s3.ticketmaster.domain.ticket.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.time.LocalDateTime;
import java.util.Arrays;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class TicketControllerTest {

    @Mock
    private CreateTicketUseCase createTicketUseCase;
    @Mock
    private GetTicketUseCase getTicketUseCase;
    @Mock
    private UpdateTicketUseCase updateTicketUseCase;
    @Mock
    private DeleteTicketUseCase deleteTicketUseCase;

    @InjectMocks
    private TicketController ticketController;

    private CreateTicketRequest createTicketRequest;
    private CreateTicketResponse createTicketResponse;
    private GetTicketResponse getTicketResponse;
    private UpdateTicketRequest updateTicketRequest;
    private UpdateTicketResponse updateTicketResponse;
    private LocalDateTime purchaseDateTime;

    @BeforeEach
    void setUp() {
        purchaseDateTime = LocalDateTime.now();

        // Create ticket request setup
        createTicketRequest = CreateTicketRequest.builder()
                .userId(1L)
                .matchId(1L)
                .seatIds(Arrays.asList(1L, 2L))
                .totalPrice(100.0)
                .build();

        createTicketResponse = CreateTicketResponse.builder()
                .id(1L)
                .userId(1L)
                .matchId(1L)
                .seatIds(Arrays.asList(1L, 2L))
                .purchaseDateTime(purchaseDateTime)
                .totalPrice(100.0)
                .build();

        // Get ticket response setup
        getTicketResponse = GetTicketResponse.builder()
                .id(1L)
                .userId(1L)
                .matchId(1L)
                .seatIds(Arrays.asList(1L, 2L))
                .purchaseDateTime(purchaseDateTime)
                .totalPrice(100.0)
                .build();

        // Update ticket request/response setup
        updateTicketRequest = UpdateTicketRequest.builder()
                .userId(2L)
                .matchId(2L)
                .seatIds(Arrays.asList(3L, 4L))
                .totalPrice(150.0)
                .build();

        updateTicketResponse = UpdateTicketResponse.builder()
                .id(1L)
                .userId(2L)
                .matchId(2L)
                .seatIds(Arrays.asList(3L, 4L))
                .purchaseDateTime(purchaseDateTime)
                .totalPrice(150.0)
                .build();
    }

    @Test
    void createTicket_ValidRequest_ReturnsCreated() {
        // Arrange
        when(createTicketUseCase.createTicket(createTicketRequest))
                .thenReturn(createTicketResponse);

        // Act
        ResponseEntity<CreateTicketResponse> response = ticketController.createTicket(createTicketRequest);

        // Assert
        assertEquals(HttpStatus.CREATED, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals(createTicketResponse, response.getBody());
        assertEquals(1L, response.getBody().getId());
        verify(createTicketUseCase).createTicket(createTicketRequest);
    }

    @Test
    void getTicket_ExistingTicket_ReturnsTicket() {
        // Arrange
        GetTicketRequest request = new GetTicketRequest(1L);
        when(getTicketUseCase.getTicket(request)).thenReturn(getTicketResponse);

        // Act
        ResponseEntity<GetTicketResponse> response = ticketController.getTicket(1L);

        // Assert
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals(getTicketResponse, response.getBody());
        assertEquals(1L, response.getBody().getId());
        verify(getTicketUseCase).getTicket(request);
    }

    @Test
    void updateTicket_ValidRequest_ReturnsUpdatedTicket() {
        // Arrange
        when(updateTicketUseCase.updateTicket(1L, updateTicketRequest))
                .thenReturn(updateTicketResponse);

        // Act
        ResponseEntity<UpdateTicketResponse> response =
                ticketController.updateTicket(1L, updateTicketRequest);

        // Assert
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals(updateTicketResponse, response.getBody());
        assertEquals(2L, response.getBody().getUserId()); // New user ID
        assertEquals(150.0, response.getBody().getTotalPrice()); // New price
        verify(updateTicketUseCase).updateTicket(1L, updateTicketRequest);
    }

    @Test
    void deleteTicket_ExistingTicket_ReturnsNoContent() {
        // Arrange
        doNothing().when(deleteTicketUseCase).deleteTicket(1L);

        // Act
        ResponseEntity<Void> response = ticketController.deleteTicket(1L);

        // Assert
        assertEquals(HttpStatus.NO_CONTENT, response.getStatusCode());
        assertNull(response.getBody());
        verify(deleteTicketUseCase).deleteTicket(1L);
    }

    @Test
    void createTicket_InvalidRequest_UseCaseThrowsException() {
        // Arrange
        when(createTicketUseCase.createTicket(createTicketRequest))
                .thenThrow(new IllegalArgumentException("Invalid request"));

        // Act & Assert
        Exception exception = assertThrows(IllegalArgumentException.class,
                () -> ticketController.createTicket(createTicketRequest));
        assertEquals("Invalid request", exception.getMessage());
        verify(createTicketUseCase).createTicket(createTicketRequest);
    }

    @Test
    void getTicket_NonExistingTicket_UseCaseThrowsException() {
        // Arrange
        GetTicketRequest request = new GetTicketRequest(999L);
        when(getTicketUseCase.getTicket(request))
                .thenThrow(new IllegalArgumentException("Ticket not found"));

        // Act & Assert
        Exception exception = assertThrows(IllegalArgumentException.class,
                () -> ticketController.getTicket(999L));
        assertEquals("Ticket not found", exception.getMessage());
        verify(getTicketUseCase).getTicket(request);
    }

    @Test
    void updateTicket_InvalidRequest_UseCaseThrowsException() {
        // Arrange
        when(updateTicketUseCase.updateTicket(1L, updateTicketRequest))
                .thenThrow(new IllegalArgumentException("Invalid update request"));

        // Act & Assert
        Exception exception = assertThrows(IllegalArgumentException.class,
                () -> ticketController.updateTicket(1L, updateTicketRequest));
        assertEquals("Invalid update request", exception.getMessage());
        verify(updateTicketUseCase).updateTicket(1L, updateTicketRequest);
    }
}