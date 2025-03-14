package nl.fontys.s3.ticketmaster.business.impl.ticketimpl;

import nl.fontys.s3.ticketmaster.business.interfaces.ticketinterfaces.TicketValidationService;
import nl.fontys.s3.ticketmaster.business.interfaces.ticketinterfaces.TicketMapper;
import nl.fontys.s3.ticketmaster.business.interfaces.matchinterfaces.MatchUpdateService;
import nl.fontys.s3.ticketmaster.domain.ticket.UpdateTicketRequest;
import nl.fontys.s3.ticketmaster.domain.ticket.UpdateTicketResponse;
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
class UpdateTicketUseCaseImplTest {

    @Mock
    private TicketRepository ticketRepositoryMock;
    @Mock
    private TicketValidationService ticketValidationServiceMock;
    @Mock
    private TicketMapper ticketMapperMock;
    @Mock
    private MatchUpdateService matchUpdateServiceMock;

    @InjectMocks
    private UpdateTicketUseCaseImpl updateTicketUseCase;

    @Test
    void updateTicket_shouldUpdateTicketSuccessfully() {
        // Arrange
        Long ticketId = 1L;
        UpdateTicketRequest request = UpdateTicketRequest.builder().userId(1L).matchId(1L).build();
        TicketEntity originalTicket = TicketEntity.builder().id(ticketId).build();
        TicketEntity updatedTicket = TicketEntity.builder().id(ticketId).build();
        UpdateTicketResponse expectedResponse = UpdateTicketResponse.builder().id(ticketId).build();

        when(ticketRepositoryMock.findById(ticketId)).thenReturn(Optional.ofNullable(originalTicket));
        when(ticketMapperMock.toUpdatedTicketEntity(originalTicket, request)).thenReturn(updatedTicket);
        when(ticketRepositoryMock.save(updatedTicket)).thenReturn(updatedTicket);
        when(ticketMapperMock.toUpdateTicketResponse(updatedTicket)).thenReturn(expectedResponse);

        // Act
        UpdateTicketResponse response = updateTicketUseCase.updateTicket(ticketId, request);

        // Assert
        assertNotNull(response);
        assertEquals(expectedResponse.getId(), response.getId());

        verify(ticketValidationServiceMock).validateTicketExists(ticketId);
        verify(ticketValidationServiceMock).validateTicketUpdateRequest(request);
        verify(ticketRepositoryMock).findById(ticketId);
        verify(ticketMapperMock).toUpdatedTicketEntity(originalTicket, request);
        verify(matchUpdateServiceMock).updateMatchSeatsForTicketUpdate(originalTicket, updatedTicket);
        verify(ticketRepositoryMock).save(updatedTicket);
        verify(ticketMapperMock).toUpdateTicketResponse(updatedTicket);
    }
}