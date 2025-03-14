package nl.fontys.s3.ticketmaster.business.impl.ticketimpl;

import nl.fontys.s3.ticketmaster.business.interfaces.matchinterfaces.MatchService;
import nl.fontys.s3.ticketmaster.business.interfaces.ticketinterfaces.TicketMapper;
import nl.fontys.s3.ticketmaster.business.interfaces.userinterfaces.UserService;
import nl.fontys.s3.ticketmaster.domain.ticket.CreateTicketRequest;
import nl.fontys.s3.ticketmaster.persitence.TicketRepository;
import nl.fontys.s3.ticketmaster.persitence.entity.MatchEntity;

import nl.fontys.s3.ticketmaster.persitence.entity.TicketEntity;
import nl.fontys.s3.ticketmaster.persitence.entity.UserEntity;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class TicketCreationServiceImplTest {
    @Mock
    private UserService userService;
    @Mock
    private MatchService matchService;
    @Mock
    private TicketRepository ticketRepository;
    @Mock
    private TicketMapper ticketMapper;

    @InjectMocks
    private TicketCreationServiceImpl ticketCreationService;

    private CreateTicketRequest request;
    private UserEntity user;
    private MatchEntity match;
    private TicketEntity ticket;
    private List<Long> seatIds;

    @BeforeEach
    void setUp() {
        // Initialize test data
        seatIds = Arrays.asList(1L, 2L);
        request = CreateTicketRequest.builder()
                .matchId(1L)
                .userId(1L)
                .seatIds(seatIds)
                .build();

        user = UserEntity.builder()
                .id(1L)
                .name("Test User")
                .email("test@example.com")
                .build();

        match = MatchEntity.builder()
                .id(1L)
                .matchDateTime(LocalDateTime.now().plusDays(7))
                .build();

        ticket = TicketEntity.builder()
                .id(1L)
                .user(user)
                .match(match)
                .purchaseDateTime(LocalDateTime.now())
                .totalPrice(100.0)
                .build();
    }

    @Test
    void createTicket_ValidRequest_ReturnsTicketEntity() {
        // Arrange
        when(userService.getUserById(request.getUserId())).thenReturn(user);
        when(matchService.getMatchById(request.getMatchId())).thenReturn(Optional.of(match));
        when(ticketMapper.toTicketEntity(request, user, match)).thenReturn(ticket);
        when(ticketRepository.save(any(TicketEntity.class))).thenReturn(ticket);

        // Act
        TicketEntity createdTicket = ticketCreationService.createTicket(request);

        // Assert
        assertNotNull(createdTicket);
        assertEquals(ticket.getId(), createdTicket.getId());
        assertEquals(ticket.getUser(), createdTicket.getUser());
        assertEquals(ticket.getMatch(), createdTicket.getMatch());
        assertEquals(ticket.getTotalPrice(), createdTicket.getTotalPrice());

        verify(userService).getUserById(request.getUserId());
        verify(matchService).getMatchById(request.getMatchId());
        verify(ticketMapper).toTicketEntity(request, user, match);
        verify(ticketRepository).save(ticket);
    }

    @Test
    void createTicket_UserNotFound_PropagatesException() {
        // Arrange
        RuntimeException expectedException = new RuntimeException("User not found");
        when(userService.getUserById(request.getUserId())).thenThrow(expectedException);

        // Act & Assert
        RuntimeException exception = assertThrows(RuntimeException.class,
                () -> ticketCreationService.createTicket(request));

        assertEquals(expectedException.getMessage(), exception.getMessage());
        verify(matchService, never()).getMatchById(any());
        verify(ticketMapper, never()).toTicketEntity(any(), any(), any());
        verify(ticketRepository, never()).save(any());
    }

    @Test
    void createTicket_MatchNotFound_ThrowsRuntimeException() {
        // Arrange
        when(userService.getUserById(request.getUserId())).thenReturn(user);
        when(matchService.getMatchById(request.getMatchId())).thenReturn(Optional.empty());

        // Act & Assert
        RuntimeException exception = assertThrows(RuntimeException.class,
                () -> ticketCreationService.createTicket(request));

        assertEquals("Match not found with id: " + request.getMatchId(), exception.getMessage());
        verify(ticketMapper, never()).toTicketEntity(any(), any(), any());
        verify(ticketRepository, never()).save(any());
    }

    @Test
    void createTicket_TicketMappingFails_PropagatesException() {
        // Arrange
        when(userService.getUserById(request.getUserId())).thenReturn(user);
        when(matchService.getMatchById(request.getMatchId())).thenReturn(Optional.of(match));
        when(ticketMapper.toTicketEntity(request, user, match)).thenThrow(new RuntimeException("Mapping failed"));

        // Act & Assert
        RuntimeException exception = assertThrows(RuntimeException.class,
                () -> ticketCreationService.createTicket(request));

        assertEquals("Mapping failed", exception.getMessage());
        verify(ticketRepository, never()).save(any());
    }

    @Test
    void createTicket_SaveFails_PropagatesException() {
        // Arrange
        when(userService.getUserById(request.getUserId())).thenReturn(user);
        when(matchService.getMatchById(request.getMatchId())).thenReturn(Optional.of(match));
        when(ticketMapper.toTicketEntity(request, user, match)).thenReturn(ticket);
        when(ticketRepository.save(any(TicketEntity.class))).thenThrow(new RuntimeException("Save failed"));

        // Act & Assert
        RuntimeException exception = assertThrows(RuntimeException.class,
                () -> ticketCreationService.createTicket(request));

        assertEquals("Save failed", exception.getMessage());
    }
}