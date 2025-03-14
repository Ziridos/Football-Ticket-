package nl.fontys.s3.ticketmaster.business.impl.ticketimpl;

import lombok.AllArgsConstructor;
import nl.fontys.s3.ticketmaster.business.exception.InvalidMatchException;
import nl.fontys.s3.ticketmaster.business.exception.InvalidSeatException;
import nl.fontys.s3.ticketmaster.business.interfaces.matchinterfaces.MatchConverter;
import nl.fontys.s3.ticketmaster.business.interfaces.matchinterfaces.MatchService;
import nl.fontys.s3.ticketmaster.business.interfaces.seatinterfaces.SeatConverter;
import nl.fontys.s3.ticketmaster.business.interfaces.ticketinterfaces.TicketMapper;
import nl.fontys.s3.ticketmaster.business.interfaces.userinterfaces.UserConverterI;
import nl.fontys.s3.ticketmaster.business.interfaces.userinterfaces.UserService;
import nl.fontys.s3.ticketmaster.business.interfaces.userinterfaces.UserValidator;
import nl.fontys.s3.ticketmaster.business.interfaces.matchinterfaces.MatchValidator;
import nl.fontys.s3.ticketmaster.domain.ticket.*;
import nl.fontys.s3.ticketmaster.persitence.entity.*;
import nl.fontys.s3.ticketmaster.persitence.SeatRepository;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.List;


@Component
@AllArgsConstructor
public class TicketMapperImpl implements TicketMapper {
    private final UserValidator userValidator;
    private final MatchValidator matchValidator;
    private final UserService userService;
    private final MatchService matchService;
    private final SeatRepository seatRepository;
    private final UserConverterI userConverter;
    private final MatchConverter matchConverter;
    private final SeatConverter seatConverter;

    @Override
    public CreateTicketResponse toCreateTicketResponse(TicketEntity entity) {
        return CreateTicketResponse.builder()
                .id(entity.getId())
                .userId(entity.getUser().getId())
                .matchId(entity.getMatch().getId())
                .seatIds(entity.getSeats().stream().map(SeatEntity::getId).toList())
                .purchaseDateTime(entity.getPurchaseDateTime())
                .totalPrice(entity.getTotalPrice())
                .build();
    }

    @Override
    public GetTicketResponse toGetTicketResponse(TicketEntity entity) {
        return GetTicketResponse.builder()
                .id(entity.getId())
                .userId(entity.getUser().getId())
                .matchId(entity.getMatch().getId())
                .seatIds(entity.getSeats().stream().map(SeatEntity::getId).toList())
                .purchaseDateTime(entity.getPurchaseDateTime())
                .totalPrice(entity.getTotalPrice())
                .build();
    }

    @Override
    public TicketEntity toTicketEntity(CreateTicketRequest request, UserEntity user, MatchEntity match) {
        List<SeatEntity> seats = seatRepository.findAllById(request.getSeatIds());

        if (seats.size() != request.getSeatIds().size()) {
            throw new InvalidSeatException("One or more seats not found");
        }

        return TicketEntity.builder()
                .user(user)
                .match(match)
                .seats(seats)
                .purchaseDateTime(LocalDateTime.now())
                .totalPrice(request.getTotalPrice())
                .build();
    }

    @Override
    public TicketEntity toUpdatedTicketEntity(TicketEntity originalTicket, UpdateTicketRequest request) {
        userValidator.validateUserExists(request.getUserId());
        UserEntity user = userService.getUserById(request.getUserId());

        matchValidator.validateMatchExists(request.getMatchId());
        MatchEntity match = matchService.getMatchById(request.getMatchId())
                .orElseThrow(() -> new InvalidMatchException("Match not found with id: " + request.getMatchId()));

        List<SeatEntity> newSeats = seatRepository.findAllById(request.getSeatIds());

        if (newSeats.size() != request.getSeatIds().size()) {
            throw new InvalidSeatException("One or more seats not found");
        }

        originalTicket.setUser(user);
        originalTicket.setMatch(match);
        originalTicket.setSeats(newSeats);
        originalTicket.setTotalPrice(request.getTotalPrice());

        return originalTicket;
    }

    @Override
    public UpdateTicketResponse toUpdateTicketResponse(TicketEntity ticket) {
        return UpdateTicketResponse.builder()
                .id(ticket.getId())
                .userId(ticket.getUser().getId())
                .matchId(ticket.getMatch().getId())
                .seatIds(ticket.getSeats().stream().map(SeatEntity::getId).toList())
                .purchaseDateTime(ticket.getPurchaseDateTime())
                .totalPrice(ticket.getTotalPrice())
                .build();
    }



    @Override
    public TicketDTO toTicket(TicketEntity entity) {
        return TicketDTO.builder()
                .id(entity.getId())
                .user(userConverter.toUserDTO(entity.getUser()))
                .match(matchConverter.toMatchDTO(entity.getMatch()))
                .seats(entity.getSeats().stream()
                        .map(seatConverter::toSeatDTO)
                        .toList())
                .purchaseDateTime(entity.getPurchaseDateTime())
                .totalPrice(entity.getTotalPrice())
                .build();
    }










}