package nl.fontys.s3.ticketmaster.business.impl.ticketimpl;

import lombok.AllArgsConstructor;
import nl.fontys.s3.ticketmaster.business.interfaces.ticketinterfaces.TicketCreationService;
import nl.fontys.s3.ticketmaster.business.interfaces.ticketinterfaces.TicketMapper;
import nl.fontys.s3.ticketmaster.business.interfaces.userinterfaces.UserService;
import nl.fontys.s3.ticketmaster.business.interfaces.matchinterfaces.MatchService;
import nl.fontys.s3.ticketmaster.domain.ticket.CreateTicketRequest;
import nl.fontys.s3.ticketmaster.persitence.TicketRepository;
import nl.fontys.s3.ticketmaster.persitence.entity.TicketEntity;
import nl.fontys.s3.ticketmaster.persitence.entity.UserEntity;
import nl.fontys.s3.ticketmaster.persitence.entity.MatchEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


@Service
@AllArgsConstructor
public class TicketCreationServiceImpl implements TicketCreationService {
    private final UserService userService;
    private final MatchService matchService;
    private final TicketRepository ticketRepository;
    private final TicketMapper ticketMapper;

    @Override
    @Transactional
    public TicketEntity createTicket(CreateTicketRequest request) {
        UserEntity user = userService.getUserById(request.getUserId());
        MatchEntity match = matchService.getMatchById(request.getMatchId())
                .orElseThrow(() -> new RuntimeException("Match not found with id: " + request.getMatchId()));

        TicketEntity ticket = ticketMapper.toTicketEntity(request, user, match);
        return ticketRepository.save(ticket);
    }
}