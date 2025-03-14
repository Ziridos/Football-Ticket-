package nl.fontys.s3.ticketmaster.controller;

import jakarta.validation.Valid;
import jakarta.validation.constraints.Positive;
import lombok.AllArgsConstructor;
import nl.fontys.s3.ticketmaster.business.interfaces.stadiuminterfaces.CreateStadiumUseCase;
import nl.fontys.s3.ticketmaster.business.interfaces.stadiuminterfaces.DeleteStadiumUseCase;
import nl.fontys.s3.ticketmaster.business.interfaces.stadiuminterfaces.GetAllStadiumsUseCase;
import nl.fontys.s3.ticketmaster.business.interfaces.stadiuminterfaces.GetStadiumUseCase;
import nl.fontys.s3.ticketmaster.business.interfaces.stadiuminterfaces.UpdateStadiumUseCase;
import nl.fontys.s3.ticketmaster.domain.stadium.CreateStadiumRequest;
import nl.fontys.s3.ticketmaster.domain.stadium.CreateStadiumResponse;
import nl.fontys.s3.ticketmaster.domain.stadium.GetAllStadiumsRequest;
import nl.fontys.s3.ticketmaster.domain.stadium.GetAllStadiumsResponse;
import nl.fontys.s3.ticketmaster.domain.stadium.GetStadiumRequest;
import nl.fontys.s3.ticketmaster.domain.stadium.GetStadiumResponse;
import nl.fontys.s3.ticketmaster.domain.stadium.UpdateStadiumRequest;
import nl.fontys.s3.ticketmaster.domain.stadium.UpdateStadiumResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/stadiums")
@CrossOrigin(origins = "http://localhost:5173")
@AllArgsConstructor
public class StadiumController {

    private final CreateStadiumUseCase createStadiumUseCase;
    private final GetAllStadiumsUseCase getAllStadiumsUseCase;
    private final GetStadiumUseCase getStadiumUseCase;
    private final UpdateStadiumUseCase updateStadiumUseCase;
    private final DeleteStadiumUseCase deleteStadiumUseCase;

    @PostMapping
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public ResponseEntity<CreateStadiumResponse> createStadium(@Valid @RequestBody CreateStadiumRequest request) {
        CreateStadiumResponse response = createStadiumUseCase.createStadium(request);
        return ResponseEntity.ok(response);
    }

    @GetMapping
    public ResponseEntity<GetAllStadiumsResponse> getAllStadiums(
            @RequestParam(required = false) String name,
            @RequestParam(required = false) String city,
            @RequestParam(required = false) String country,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "id") String sortBy,
            @RequestParam(defaultValue = "ASC") String sortDirection) {

        GetAllStadiumsRequest request = GetAllStadiumsRequest.builder()
                .name(name)
                .city(city)
                .country(country)
                .page(page)
                .size(size)
                .sortBy(sortBy)
                .sortDirection(sortDirection)
                .build();

        GetAllStadiumsResponse response = getAllStadiumsUseCase.getAllStadiums(request);
        return ResponseEntity.ok(response);
    }


    @GetMapping("/{id}")
    public ResponseEntity<GetStadiumResponse> getStadium(@PathVariable("id")  Long id) {
        GetStadiumRequest request = new GetStadiumRequest(id);
        GetStadiumResponse response = getStadiumUseCase.getStadiumById(request);
        return ResponseEntity.ok(response);
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public ResponseEntity<UpdateStadiumResponse> updateStadium(@PathVariable("id")  Long id,
                                                               @RequestBody @Valid UpdateStadiumRequest request) {
        UpdateStadiumResponse response = updateStadiumUseCase.updateStadium(id, request);
        return ResponseEntity.ok(response);
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public ResponseEntity<Void> deleteStadium(@PathVariable("id")  Long id) {
        deleteStadiumUseCase.deleteStadiumById(id);
        return ResponseEntity.noContent().build();
    }
}
