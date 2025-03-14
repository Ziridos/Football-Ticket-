package nl.fontys.s3.ticketmaster.controller;

import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import nl.fontys.s3.ticketmaster.business.interfaces.clubinterfaces.*;
import nl.fontys.s3.ticketmaster.domain.club.*;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/clubs")
@CrossOrigin(origins = "http://localhost:5173")
@AllArgsConstructor
public class ClubController {
    private final CreateClubUseCase createClubUseCase;
    private final GetAllClubsUseCase getAllClubsUseCase;
    private final GetClubUseCase getClubUseCase;
    private final UpdateClubUseCase updateClubUseCase;
    private final DeleteClubUseCase deleteClubUseCase;
    private final UploadLogoUseCase uploadLogoUseCase;
    private final DeleteLogoUseCase deleteLogoUseCase;

    @GetMapping
    public ResponseEntity<GetAllClubsResponse> getAllClubs(
            @RequestParam(required = false) String name,
            @RequestParam(required = false) String stadiumName,
            @RequestParam(required = false) String stadiumCity,
            @RequestParam(required = false) String stadiumCountry,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "id") String sortBy,
            @RequestParam(defaultValue = "ASC") String sortDirection) {

        GetAllClubsRequest request = GetAllClubsRequest.builder()
                .name(name)
                .stadiumName(stadiumName)
                .stadiumCity(stadiumCity)
                .stadiumCountry(stadiumCountry)
                .page(page)
                .size(size)
                .sortBy(sortBy)
                .sortDirection(sortDirection)
                .build();

        GetAllClubsResponse response = getAllClubsUseCase.getAllClubs(request);
        return ResponseEntity.ok(response);
    }

    @PostMapping
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public ResponseEntity<CreateClubResponse> createClub(@RequestBody @Valid CreateClubRequest request) {
        CreateClubResponse response = createClubUseCase.createClub(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @GetMapping("/{clubId}")
    public ResponseEntity<GetClubResponse> getClub(@PathVariable("clubId") Long clubId) {
        GetClubRequest request = new GetClubRequest(clubId);
        GetClubResponse response = getClubUseCase.getClub(request);
        return ResponseEntity.ok(response);
    }

    @PutMapping("/{clubId}")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public ResponseEntity<UpdateClubResponse> updateClub(@PathVariable Long clubId, @RequestBody @Valid UpdateClubRequest request) {
        UpdateClubResponse response = updateClubUseCase.updateClub(clubId, request);
        return ResponseEntity.ok(response);
    }

    @DeleteMapping("/{clubId}")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public ResponseEntity<Void> deleteClub(@PathVariable Long clubId) {
        deleteClubUseCase.deleteClub(clubId);
        return ResponseEntity.noContent().build();
    }

    //Logo's
    @PostMapping(value = "/{clubId}/logo", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public ResponseEntity<UploadLogoResponse> uploadLogo(
            @PathVariable Long clubId,
            @ModelAttribute @Valid UploadLogoRequest request) {
        uploadLogoUseCase.uploadLogo(clubId, request);
        return ResponseEntity.ok(UploadLogoResponse.builder()
                .clubId(clubId)
                .message("Logo uploaded successfully")
                .build());
    }

    @DeleteMapping("/{clubId}/logo")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public ResponseEntity<Void> deleteLogo(@PathVariable Long clubId) {
        deleteLogoUseCase.deleteLogo(clubId);
        return ResponseEntity.noContent().build();
    }
}
