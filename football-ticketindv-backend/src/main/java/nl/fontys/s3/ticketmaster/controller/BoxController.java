package nl.fontys.s3.ticketmaster.controller;

import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import nl.fontys.s3.ticketmaster.business.interfaces.boxinterfaces.*;
import nl.fontys.s3.ticketmaster.domain.box.*;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/stadiums/{stadiumId}/boxes")
@CrossOrigin(origins = "http://localhost:5173")
@AllArgsConstructor
public class BoxController {

    private final GetBoxesByStadiumUseCase getBoxesByStadiumUseCase;
    private final SaveBoxUseCase saveBoxUseCase;
    private final UpdateBoxPriceUseCase updateBoxPriceUseCase;
    private final GetMatchSpecificBoxesUseCase getMatchSpecificBoxesUseCase;
    private final DeleteBoxUseCase deleteBoxUseCase;

    @GetMapping
    public ResponseEntity<List<GetBoxResponse>> getBoxesByStadium(@PathVariable("stadiumId") Long stadiumId) {
        List<GetBoxResponse> boxes = getBoxesByStadiumUseCase.getBoxesByStadium(stadiumId);
        return ResponseEntity.ok(boxes);
    }

    @DeleteMapping("/{boxId}")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public ResponseEntity<Void> deleteBox(
            @PathVariable("boxId") Long boxId) {
        deleteBoxUseCase.deleteBox(boxId);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/match/{matchId}")
    public ResponseEntity<List<GetBoxResponse>> getBoxesByStadiumForMatch(
            @PathVariable("stadiumId") Long stadiumId,
            @PathVariable("matchId") Long matchId) {
        List<GetBoxResponse> boxes = getMatchSpecificBoxesUseCase.getBoxesByStadiumForMatch(stadiumId, matchId);
        return ResponseEntity.ok(boxes);
    }

    @PostMapping
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public ResponseEntity<CreateBoxResponse> createBox(
            @PathVariable("stadiumId") Long stadiumId,
            @Valid @RequestBody CreateBoxRequest request) {
        request.setStadiumId(stadiumId);
        CreateBoxResponse response = saveBoxUseCase.saveBox(request);
        return ResponseEntity.ok(response);
    }

    @PatchMapping("/{boxId}/price")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public ResponseEntity<UpdateBoxPriceResponse> updateBoxPrice(
            @PathVariable("stadiumId") Long stadiumId,
            @PathVariable("boxId") Long boxId,
            @Valid @RequestBody UpdateBoxPriceRequest request) {
        if (!boxId.equals(request.getBoxId())) {
            return ResponseEntity.badRequest().build();
        }
        UpdateBoxPriceResponse response = updateBoxPriceUseCase.updateBoxPrice(request);
        if (response != null) {
            return ResponseEntity.ok(response);
        } else {
            return ResponseEntity.notFound().build();
        }
    }
}