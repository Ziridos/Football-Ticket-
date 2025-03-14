package nl.fontys.s3.ticketmaster.business.impl.ticketimpl;

import nl.fontys.s3.ticketmaster.business.exception.InvalidTicketException;
import nl.fontys.s3.ticketmaster.business.interfaces.ticketinterfaces.TicketValidationService;
import nl.fontys.s3.ticketmaster.business.interfaces.ticketinterfaces.TicketMapper;
import nl.fontys.s3.ticketmaster.domain.ticket.GetTicketRequest;
import nl.fontys.s3.ticketmaster.domain.ticket.GetTicketResponse;
import nl.fontys.s3.ticketmaster.persitence.TicketRepository;
import nl.fontys.s3.ticketmaster.persitence.entity.TicketEntity;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class GetTicketUseCaseImplTest {

    @Mock
    private TicketRepository ticketRepositoryMock;
    @Mock
    private TicketValidationService ticketValidationServiceMock;
    @Mock
    private TicketMapper ticketMapperMock;

    @InjectMocks
    private GetTicketUseCaseImpl getTicketUseCase;

    @Test
    void getTicket_shouldReturnTicketSuccessfully() {
        // Arrange
        Long ticketId = 1L;
        GetTicketRequest request = GetTicketRequest.builder().id(ticketId).build();
        TicketEntity ticket = TicketEntity.builder().id(ticketId).build();
        GetTicketResponse expectedResponse = GetTicketResponse.builder().id(ticketId).build();

        doNothing().when(ticketValidationServiceMock).validateTicketExists(ticketId);
        when(ticketRepositoryMock.findById(ticketId)).thenReturn(Optional.ofNullable(ticket));
        when(ticketMapperMock.toGetTicketResponse(ticket)).thenReturn(expectedResponse);

        // Act
        GetTicketResponse response = getTicketUseCase.getTicket(request);

        // Assert
        assertNotNull(response);
        assertEquals(expectedResponse.getId(), response.getId());

        verify(ticketValidationServiceMock).validateTicketExists(ticketId);
        verify(ticketRepositoryMock).findById(ticketId);
        verify(ticketMapperMock).toGetTicketResponse(ticket);
    }

    @Test
    void getTicket_shouldThrowInvalidTicketException_whenTicketDoesNotExist() {
        // Arrange
        Long ticketId = 1L;
        GetTicketRequest request = GetTicketRequest.builder().id(ticketId).build();

        doThrow(new InvalidTicketException("Ticket does not exist"))
                .when(ticketValidationServiceMock).validateTicketExists(ticketId);

        // Act & Assert
        assertThrows(InvalidTicketException.class, () -> getTicketUseCase.getTicket(request));

        verify(ticketValidationServiceMock).validateTicketExists(ticketId);
        verify(ticketRepositoryMock, never()).findById(ticketId);
        verify(ticketMapperMock, never()).toGetTicketResponse(any());
    }
}