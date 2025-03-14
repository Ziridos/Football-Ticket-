package nl.fontys.s3.ticketmaster.business.impl.ticketimpl;

import nl.fontys.s3.ticketmaster.business.impl.userimpl.UserConverter;
import nl.fontys.s3.ticketmaster.business.interfaces.matchinterfaces.MatchConverter;
import nl.fontys.s3.ticketmaster.business.interfaces.matchinterfaces.MatchService;
import nl.fontys.s3.ticketmaster.business.interfaces.matchinterfaces.MatchValidator;
import nl.fontys.s3.ticketmaster.business.interfaces.seatinterfaces.SeatConverter;
import nl.fontys.s3.ticketmaster.business.interfaces.userinterfaces.UserService;
import nl.fontys.s3.ticketmaster.business.interfaces.userinterfaces.UserValidator;
import nl.fontys.s3.ticketmaster.domain.ticket.*;
import nl.fontys.s3.ticketmaster.domain.user.UserDTO;
import nl.fontys.s3.ticketmaster.domain.match.MatchDTO;
import nl.fontys.s3.ticketmaster.domain.seat.SeatDTO;
import nl.fontys.s3.ticketmaster.domain.block.BlockDTO;
import nl.fontys.s3.ticketmaster.domain.box.BoxDTO;
import nl.fontys.s3.ticketmaster.persitence.SeatRepository;
import nl.fontys.s3.ticketmaster.persitence.entity.*;
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
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class TicketMapperImplTest {
    @Mock
    private UserValidator userValidator;
    @Mock
    private MatchValidator matchValidator;
    @Mock
    private UserService userService;
    @Mock
    private MatchService matchService;
    @Mock
    private SeatRepository seatRepository;
    @Mock
    private UserConverter userConverter;
    @Mock
    private MatchConverter matchConverter;
    @Mock
    private SeatConverter seatConverter;

    @InjectMocks
    private TicketMapperImpl ticketMapper;

    private UserEntity user;
    private MatchEntity match;
    private List<SeatEntity> seats;
    private TicketEntity ticket;
    private LocalDateTime purchaseDateTime;
    private UserDTO userDTO;
    private MatchDTO matchDTO;
    private List<SeatDTO> seatDTOs;
    private BoxDTO boxDTO;
    private BlockDTO blockDTO;

    @BeforeEach
    void setUp() {
        purchaseDateTime = LocalDateTime.now();

        user = UserEntity.builder()
                .id(1L)
                .name("Test User")
                .email("test@example.com")
                .build();

        match = MatchEntity.builder()
                .id(1L)
                .matchDateTime(LocalDateTime.now().plusDays(7))
                .build();

        seats = Arrays.asList(
                SeatEntity.builder().id(1L).seatNumber("A1").price(50.0).build(),
                SeatEntity.builder().id(2L).seatNumber("A2").price(50.0).build()
        );

        ticket = TicketEntity.builder()
                .id(1L)
                .user(user)
                .match(match)
                .seats(seats)
                .purchaseDateTime(purchaseDateTime)
                .totalPrice(100.0)
                .build();

        // Setup DTOs
        boxDTO = BoxDTO.builder()
                .boxId(1L)
                .boxName("Box A")
                .build();

        blockDTO = BlockDTO.builder()
                .blockId(1L)
                .blockName("Block 1")
                .build();

        userDTO = UserDTO.builder()
                .id(1L)
                .name("Test User")
                .email("test@example.com")
                .build();

        matchDTO = MatchDTO.builder()
                .matchId(1L)
                .homeClub(new ClubEntity())
                .awayClub(new ClubEntity())
                .matchDateTime(match.getMatchDateTime())
                .competition(new CompetitionEntity())
                .build();

        seatDTOs = Arrays.asList(
                SeatDTO.builder()
                        .seatId(1L)
                        .seatNumber("A1")
                        .block(blockDTO)
                        .box(boxDTO)
                        .price(50.0)
                        .build(),
                SeatDTO.builder()
                        .seatId(2L)
                        .seatNumber("A2")
                        .block(blockDTO)
                        .box(boxDTO)
                        .price(50.0)
                        .build()
        );
    }

    @Test
    void toCreateTicketResponse_ValidTicket_ReturnsCorrectResponse() {
        // Act
        CreateTicketResponse response = ticketMapper.toCreateTicketResponse(ticket);

        // Assert
        assertNotNull(response);
        assertEquals(ticket.getId(), response.getId());
        assertEquals(user.getId(), response.getUserId());
        assertEquals(match.getId(), response.getMatchId());
        assertEquals(Arrays.asList(1L, 2L), response.getSeatIds());
        assertEquals(purchaseDateTime, response.getPurchaseDateTime());
        assertEquals(100.0, response.getTotalPrice());
    }

    @Test
    void toGetTicketResponse_ValidTicket_ReturnsCorrectResponse() {
        // Act
        GetTicketResponse response = ticketMapper.toGetTicketResponse(ticket);

        // Assert
        assertNotNull(response);
        assertEquals(ticket.getId(), response.getId());
        assertEquals(user.getId(), response.getUserId());
        assertEquals(match.getId(), response.getMatchId());
        assertEquals(Arrays.asList(1L, 2L), response.getSeatIds());
        assertEquals(purchaseDateTime, response.getPurchaseDateTime());
        assertEquals(100.0, response.getTotalPrice());
    }

    @Test
    void toTicketEntity_ValidRequest_ReturnsCorrectEntity() {
        // Arrange
        CreateTicketRequest request = CreateTicketRequest.builder()
                .userId(1L)
                .matchId(1L)
                .seatIds(Arrays.asList(1L, 2L))
                .totalPrice(100.0)
                .build();

        when(seatRepository.findAllById(request.getSeatIds())).thenReturn(seats);

        // Act
        TicketEntity result = ticketMapper.toTicketEntity(request, user, match);

        // Assert
        assertNotNull(result);
        assertEquals(user, result.getUser());
        assertEquals(match, result.getMatch());
        assertEquals(seats, result.getSeats());
        assertEquals(100.0, result.getTotalPrice());
        assertNotNull(result.getPurchaseDateTime());
    }

    @Test
    void toTicketEntity_SeatsNotFound_ThrowsException() {
        // Arrange
        CreateTicketRequest request = CreateTicketRequest.builder()
                .userId(1L)
                .matchId(1L)
                .seatIds(Arrays.asList(1L, 2L))
                .totalPrice(100.0)
                .build();

        when(seatRepository.findAllById(request.getSeatIds()))
                .thenReturn(Arrays.asList(seats.get(0))); // Return only one seat

        // Act & Assert
        RuntimeException exception = assertThrows(RuntimeException.class,
                () -> ticketMapper.toTicketEntity(request, user, match));
        assertEquals("One or more seats not found", exception.getMessage());
    }

    @Test
    void toUpdatedTicketEntity_ValidRequest_ReturnsUpdatedEntity() {
        // Arrange
        UpdateTicketRequest request = UpdateTicketRequest.builder()
                .userId(2L)
                .matchId(2L)
                .seatIds(Arrays.asList(1L, 2L))
                .totalPrice(150.0)
                .build();

        UserEntity newUser = UserEntity.builder().id(2L).build();
        MatchEntity newMatch = MatchEntity.builder().id(2L).build();

        doNothing().when(userValidator).validateUserExists(2L);
        doNothing().when(matchValidator).validateMatchExists(2L);

        when(userService.getUserById(2L)).thenReturn(newUser);
        when(matchService.getMatchById(2L)).thenReturn(Optional.of(newMatch));
        when(seatRepository.findAllById(request.getSeatIds())).thenReturn(seats);

        // Act
        TicketEntity result = ticketMapper.toUpdatedTicketEntity(ticket, request);

        // Assert
        assertNotNull(result);
        assertEquals(newUser, result.getUser());
        assertEquals(newMatch, result.getMatch());
        assertEquals(seats, result.getSeats());
        assertEquals(150.0, result.getTotalPrice());

        verify(userValidator).validateUserExists(2L);
        verify(matchValidator).validateMatchExists(2L);
    }

    @Test
    void toUpdatedTicketEntity_InvalidUser_ThrowsException() {
        // Arrange
        UpdateTicketRequest request = UpdateTicketRequest.builder()
                .userId(2L)
                .matchId(2L)
                .seatIds(Arrays.asList(1L, 2L))
                .totalPrice(150.0)
                .build();

        doThrow(new RuntimeException("User not found"))
                .when(userValidator).validateUserExists(2L);

        // Act & Assert
        RuntimeException exception = assertThrows(RuntimeException.class,
                () -> ticketMapper.toUpdatedTicketEntity(ticket, request));
        assertEquals("User not found", exception.getMessage());

        verify(userValidator).validateUserExists(2L);
        verify(matchValidator, never()).validateMatchExists(any());
    }

    @Test
    void toUpdatedTicketEntity_InvalidMatch_ThrowsException() {
        // Arrange
        UpdateTicketRequest request = UpdateTicketRequest.builder()
                .userId(2L)
                .matchId(2L)
                .seatIds(Arrays.asList(1L, 2L))
                .totalPrice(150.0)
                .build();

        doNothing().when(userValidator).validateUserExists(2L);
        doThrow(new RuntimeException("Match not found"))
                .when(matchValidator).validateMatchExists(2L);

        // Act & Assert
        RuntimeException exception = assertThrows(RuntimeException.class,
                () -> ticketMapper.toUpdatedTicketEntity(ticket, request));
        assertEquals("Match not found", exception.getMessage());

        verify(userValidator).validateUserExists(2L);
        verify(matchValidator).validateMatchExists(2L);
    }

    @Test
    void toUpdatedTicketEntity_SeatsNotFound_ThrowsException() {
        // Arrange
        UpdateTicketRequest request = UpdateTicketRequest.builder()
                .userId(2L)
                .matchId(2L)
                .seatIds(Arrays.asList(1L, 2L))
                .totalPrice(150.0)
                .build();

        doNothing().when(userValidator).validateUserExists(2L);
        doNothing().when(matchValidator).validateMatchExists(2L);
        when(userService.getUserById(2L)).thenReturn(user);
        when(matchService.getMatchById(2L)).thenReturn(Optional.of(match));
        when(seatRepository.findAllById(request.getSeatIds()))
                .thenReturn(Arrays.asList(seats.get(0)));

        // Act & Assert
        RuntimeException exception = assertThrows(RuntimeException.class,
                () -> ticketMapper.toUpdatedTicketEntity(ticket, request));
        assertEquals("One or more seats not found", exception.getMessage());
    }

    @Test
    void toUpdateTicketResponse_ValidTicket_ReturnsCorrectResponse() {
        // Act
        UpdateTicketResponse response = ticketMapper.toUpdateTicketResponse(ticket);

        // Assert
        assertNotNull(response);
        assertEquals(ticket.getId(), response.getId());
        assertEquals(user.getId(), response.getUserId());
        assertEquals(match.getId(), response.getMatchId());
        assertEquals(Arrays.asList(1L, 2L), response.getSeatIds());
        assertEquals(purchaseDateTime, response.getPurchaseDateTime());
        assertEquals(100.0, response.getTotalPrice());
    }

    @Test
    void toTicket_ValidEntity_ReturnsCorrectDTO() {
        // Arrange
        when(userConverter.toUserDTO(user)).thenReturn(userDTO);
        when(matchConverter.toMatchDTO(match)).thenReturn(matchDTO);
        when(seatConverter.toSeatDTO(seats.get(0))).thenReturn(seatDTOs.get(0));
        when(seatConverter.toSeatDTO(seats.get(1))).thenReturn(seatDTOs.get(1));

        // Act
        TicketDTO result = ticketMapper.toTicket(ticket);

        // Assert
        assertNotNull(result);
        assertEquals(ticket.getId(), result.getId());
        assertEquals(userDTO, result.getUser());
        assertEquals(matchDTO, result.getMatch());
        assertEquals(seatDTOs, result.getSeats());
        assertEquals(purchaseDateTime, result.getPurchaseDateTime());
        assertEquals(100.0, result.getTotalPrice());

        verify(userConverter).toUserDTO(user);
        verify(matchConverter).toMatchDTO(match);
        verify(seatConverter, times(2)).toSeatDTO(any(SeatEntity.class));
    }

    @Test
    void toTicket_NullEntity_ThrowsException() {
        // Act & Assert
        assertThrows(NullPointerException.class, () -> ticketMapper.toTicket(null));
    }


    @Test
    void toTicket_EntityWithEmptySeats_HandlesEmptyListGracefully() {
        // Arrange
        TicketEntity ticketWithEmptySeats = TicketEntity.builder()
                .id(1L)
                .user(user)
                .match(match)
                .seats(List.of())
                .purchaseDateTime(purchaseDateTime)
                .totalPrice(100.0)
                .build();

        when(userConverter.toUserDTO(user)).thenReturn(userDTO);
        when(matchConverter.toMatchDTO(match)).thenReturn(matchDTO);

        // Act
        TicketDTO result = ticketMapper.toTicket(ticketWithEmptySeats);

        // Assert
        assertNotNull(result);
        assertEquals(1L, result.getId());
        assertEquals(userDTO, result.getUser());
        assertEquals(matchDTO, result.getMatch());
        assertNotNull(result.getSeats());
        assertTrue(result.getSeats().isEmpty());
        assertEquals(purchaseDateTime, result.getPurchaseDateTime());
        assertEquals(100.0, result.getTotalPrice());

        // Verify converters were called appropriately
        verify(userConverter).toUserDTO(user);
        verify(matchConverter).toMatchDTO(match);
        verify(seatConverter, never()).toSeatDTO(any());
    }
}