package nl.fontys.s3.ticketmaster.controller;

import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import nl.fontys.s3.ticketmaster.business.interfaces.matchinterfaces.*;
import nl.fontys.s3.ticketmaster.domain.match.*;
import nl.fontys.s3.ticketmaster.domain.seat.SeatAvailabilityResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/matches")
@CrossOrigin(origins = "http://localhost:5173")
@AllArgsConstructor
public class MatchController {
    private final CreateMatchUseCase createMatchUseCase;
    private final GetAllMatchesUseCase getAllMatchesUseCase;
    private final GetMatchUseCase getMatchUseCase;
    private final DeleteMatchUseCase deleteMatchUseCase;
    private final UpdateMatchUseCase updateMatchUseCase;
    private final GetSeatAvailabilityUseCase getSeatAvailabilityUseCase;

    @PostMapping
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public ResponseEntity<CreateMatchResponse> createMatch(@RequestBody @Valid CreateMatchRequest request) {
        CreateMatchResponse response = createMatchUseCase.createMatch(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @GetMapping
    public ResponseEntity<GetAllMatchesResponse> getAllMatches(
            @RequestParam(required = false) String homeClubName,
            @RequestParam(required = false) String awayClubName,
            @RequestParam(required = false) String competitionName,
            @RequestParam(required = false) String date,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "matchDateTime") String sortBy,
            @RequestParam(defaultValue = "DESC") String sortDirection) {

        GetAllMatchesRequest request = GetAllMatchesRequest.builder()
                .homeClubName(homeClubName)
                .awayClubName(awayClubName)
                .competitionName(competitionName)
                .date(date != null ? LocalDate.parse(date) : null)
                .page(page)
                .size(size)
                .sortBy(sortBy)
                .sortDirection(sortDirection)
                .build();

        GetAllMatchesResponse response = getAllMatchesUseCase.getAllMatches(request);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/{matchId}")
    public ResponseEntity<GetMatchResponse> getMatch(@PathVariable("matchId") Long matchId) {
        GetMatchRequest request = new GetMatchRequest(matchId);
        GetMatchResponse response = getMatchUseCase.getMatch(request);
        return ResponseEntity.ok(response);
    }

    @DeleteMapping("/{matchId}")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public ResponseEntity<Void> deleteMatch(@PathVariable("matchId") Long matchId) {
        deleteMatchUseCase.deleteMatch(matchId);
        return ResponseEntity.noContent().build();
    }

    @PutMapping("/{matchId}")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public ResponseEntity<UpdateMatchResponse> updateMatch(
            @PathVariable("matchId") Long matchId,
            @RequestBody @Valid UpdateMatchRequest request) {
        UpdateMatchResponse response = updateMatchUseCase.updateMatch(request, matchId);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/{matchId}/seat-availability")
    public ResponseEntity<List<SeatAvailabilityResponse>> getSeatAvailability(@PathVariable Long matchId) {
        List<SeatAvailabilityResponse> seatAvailability = getSeatAvailabilityUseCase.getSeatAvailability(matchId);
        return ResponseEntity.ok(seatAvailability);
    }


}
