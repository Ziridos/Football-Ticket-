package nl.fontys.s3.ticketmaster.business.impl.ticketimpl;

import nl.fontys.s3.ticketmaster.business.exception.InvalidTicketException;
import nl.fontys.s3.ticketmaster.business.interfaces.ticketinterfaces.TicketValidationService;
import nl.fontys.s3.ticketmaster.persitence.TicketRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class DeleteTicketUseCaseImplTest {

    @Mock
    private TicketRepository ticketRepositoryMock;
    @Mock
    private TicketValidationService ticketValidationServiceMock;

    @InjectMocks
    private DeleteTicketUseCaseImpl deleteTicketUseCase;

    @Test
    void deleteTicket_shouldDeleteTicketSuccessfully() {
        // Arrange
        Long ticketId = 1L;
        doNothing().when(ticketValidationServiceMock).validateTicketExists(ticketId);

        // Act
        deleteTicketUseCase.deleteTicket(ticketId);

        // Assert
        verify(ticketValidationServiceMock).validateTicketExists(ticketId);
        verify(ticketRepositoryMock).deleteById(ticketId);
    }

    @Test
    void deleteTicket_shouldThrowInvalidTicketException_whenTicketDoesNotExist() {
        // Arrange
        Long ticketId = 1L;
        doThrow(new InvalidTicketException("Ticket does not exist"))
                .when(ticketValidationServiceMock).validateTicketExists(ticketId);

        // Act & Assert
        assertThrows(InvalidTicketException.class, () -> deleteTicketUseCase.deleteTicket(ticketId));

        verify(ticketValidationServiceMock).validateTicketExists(ticketId);
        verify(ticketRepositoryMock, never()).deleteById(ticketId);
    }
}