package nl.fontys.s3.ticketmaster.controller;

import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import nl.fontys.s3.ticketmaster.business.interfaces.competitioninterfaces.*;
import nl.fontys.s3.ticketmaster.domain.competition.*;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/competitions")
@CrossOrigin(origins = "http://localhost:5173")
@AllArgsConstructor
public class CompetitionController {
    private final CreateCompetitionUseCase createCompetitionUseCase;
    private final GetAllCompetitionsUseCase getAllCompetitionsUseCase;
    private final GetCompetitionUseCase getCompetitionUseCase;
    private final UpdateCompetitionUseCase updateCompetitionUseCase;
    private final DeleteCompetitionUseCase deleteCompetitionUseCase;

    @PostMapping
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public ResponseEntity<CreateCompetitionResponse> createCompetition(@RequestBody @Valid CreateCompetitionRequest request) {
        CreateCompetitionResponse response = createCompetitionUseCase.createCompetition(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @GetMapping
    public ResponseEntity<GetAllCompetitionsResponse> getAllCompetitions(
            @RequestParam(required = false) String name,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "id") String sortBy,
            @RequestParam(defaultValue = "ASC") String sortDirection) {

        GetAllCompetitionsRequest request = GetAllCompetitionsRequest.builder()
                .name(name)
                .page(page)
                .size(size)
                .sortBy(sortBy)
                .sortDirection(sortDirection)
                .build();

        GetAllCompetitionsResponse response = getAllCompetitionsUseCase.getAllCompetitions(request);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/{competitionId}")
    public ResponseEntity<GetCompetitionResponse> getCompetition(@PathVariable Long competitionId) {
        GetCompetitionRequest request = GetCompetitionRequest.builder()
                .competitionId(competitionId)
                .build();

        GetCompetitionResponse response = getCompetitionUseCase.getCompetition(request);
        return ResponseEntity.ok(response);
    }

    @PutMapping("/{competitionId}")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public ResponseEntity<UpdateCompetitionResponse> updateCompetition(
            @PathVariable Long competitionId,
            @RequestBody @Valid UpdateCompetitionRequest request) {
        UpdateCompetitionRequest updateRequest = UpdateCompetitionRequest.builder()
                .competitionId(competitionId)
                .competitionName(request.getCompetitionName())
                .build();

        UpdateCompetitionResponse response = updateCompetitionUseCase.updateCompetition(updateRequest);
        return ResponseEntity.ok(response);
    }

    @DeleteMapping("/{competitionId}")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public ResponseEntity<Void> deleteCompetition(@PathVariable Long competitionId) {
        deleteCompetitionUseCase.deleteCompetitionById(competitionId);
        return ResponseEntity.noContent().build();
    }


}