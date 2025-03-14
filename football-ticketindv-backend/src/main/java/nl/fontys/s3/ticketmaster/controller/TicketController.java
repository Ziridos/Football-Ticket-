package nl.fontys.s3.ticketmaster.controller;

import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import nl.fontys.s3.ticketmaster.business.interfaces.ticketinterfaces.*;
import nl.fontys.s3.ticketmaster.domain.ticket.*;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/tickets")
@CrossOrigin(origins = "http://localhost:5173")
@AllArgsConstructor
public class TicketController {
    private final CreateTicketUseCase createTicketUseCase;
    private final GetTicketUseCase getTicketUseCase;
    private final UpdateTicketUseCase updateTicketUseCase;
    private final DeleteTicketUseCase deleteTicketUseCase;
    private final GetAllTicketsUserUseCase getAllTicketsUserUseCase;

    @PostMapping
    public ResponseEntity<CreateTicketResponse> createTicket(@Valid @RequestBody CreateTicketRequest request) {
        CreateTicketResponse response = createTicketUseCase.createTicket(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @GetMapping("/{id}")
    public ResponseEntity<GetTicketResponse> getTicket(@PathVariable("id") Long id) {
        GetTicketRequest request = new GetTicketRequest(id);
        GetTicketResponse response = getTicketUseCase.getTicket(request);
        return ResponseEntity.ok(response);
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public ResponseEntity<UpdateTicketResponse> updateTicket(
            @PathVariable("id") Long id,
            @RequestBody @Valid UpdateTicketRequest request) {
        UpdateTicketResponse response = updateTicketUseCase.updateTicket(id, request);
        return ResponseEntity.ok(response);
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public ResponseEntity<Void> deleteTicket(@PathVariable("id") Long id) {
        deleteTicketUseCase.deleteTicket(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/user/{userId}")
    public ResponseEntity<GetAllTicketsUserResponse> getAllTicketsForUser(
            @PathVariable("userId") Long userId,
            @RequestParam(required = false) Integer year,
            @RequestParam(required = false) Integer quarter,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "purchaseDateTime") String sortBy,
            @RequestParam(defaultValue = "DESC") String sortDirection) {
        GetAllTicketsUserRequest request = GetAllTicketsUserRequest.builder()
                .id(userId)
                .year(year)
                .quarter(quarter)
                .page(page)
                .size(size)
                .sortBy(sortBy)
                .sortDirection(sortDirection)
                .build();
        GetAllTicketsUserResponse response = getAllTicketsUserUseCase.getAllTicketsForUser(request);
        return ResponseEntity.ok(response);
    }
}