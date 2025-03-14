package nl.fontys.s3.ticketmaster.business.impl.ticketimpl;

import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import nl.fontys.s3.ticketmaster.business.interfaces.ticketinterfaces.GetAllTicketsUserUseCase;
import nl.fontys.s3.ticketmaster.business.interfaces.ticketinterfaces.TicketMapper;
import nl.fontys.s3.ticketmaster.business.interfaces.userinterfaces.UserValidator;
import nl.fontys.s3.ticketmaster.domain.ticket.GetAllTicketsUserRequest;
import nl.fontys.s3.ticketmaster.domain.ticket.GetAllTicketsUserResponse;
import nl.fontys.s3.ticketmaster.persitence.TicketRepository;
import nl.fontys.s3.ticketmaster.persitence.entity.TicketEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class GetAllTicketsUserUseCaseImpl implements GetAllTicketsUserUseCase {
    private final TicketRepository ticketRepository;
    private final UserValidator userValidator;
    private final TicketMapper ticketMapper;

    @Override
    @Transactional
    public GetAllTicketsUserResponse getAllTicketsForUser(GetAllTicketsUserRequest request) {
        userValidator.validateUserExists(request.getId());

        Sort sort = Sort.by(Sort.Direction.fromString(request.getSortDirection()), request.getSortBy());
        Pageable pageable = PageRequest.of(request.getPage(), request.getSize(), sort);

        Page<TicketEntity> pageResults = ticketRepository.findByFilters(
                request.getId(),
                request.getYear(),
                request.getQuarter(),
                pageable
        );

        return GetAllTicketsUserResponse.builder()
                .tickets(pageResults.getContent().stream()
                        .map(ticketMapper::toTicket)
                        .toList())
                .totalElements(pageResults.getTotalElements())
                .totalPages(pageResults.getTotalPages())
                .currentPage(pageResults.getNumber())
                .build();
    }
}