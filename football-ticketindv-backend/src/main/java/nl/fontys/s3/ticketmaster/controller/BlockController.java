package nl.fontys.s3.ticketmaster.controller;

import lombok.AllArgsConstructor;
import nl.fontys.s3.ticketmaster.business.interfaces.blockinterfaces.DeleteBlockUseCase;
import nl.fontys.s3.ticketmaster.business.interfaces.blockinterfaces.GetBlocksByStadiumUseCase;
import nl.fontys.s3.ticketmaster.business.interfaces.blockinterfaces.GetMatchSpecificBlocksUseCase;
import nl.fontys.s3.ticketmaster.business.interfaces.blockinterfaces.SaveBlockUseCase;
import nl.fontys.s3.ticketmaster.domain.block.CreateBlockRequest;
import nl.fontys.s3.ticketmaster.domain.block.CreateBlockResponse;
import nl.fontys.s3.ticketmaster.domain.block.GetBlockResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/stadiums/{stadiumId}/boxes/{boxId}/blocks")
@CrossOrigin(origins = "http://localhost:5173")
@AllArgsConstructor
public class BlockController {
    private final SaveBlockUseCase saveBlockUseCase;
    private final GetBlocksByStadiumUseCase getBlocksByStadiumUseCase;
    private final GetMatchSpecificBlocksUseCase getMatchSpecificBlocksUseCase;
    private final DeleteBlockUseCase deleteBlockUseCase;

    @PostMapping
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public ResponseEntity<CreateBlockResponse> createBlock(
            @PathVariable("boxId") Long boxId,
            @RequestBody CreateBlockRequest request) {
        request.setBoxId(boxId);
        CreateBlockResponse response = saveBlockUseCase.saveBlock(request);
        return ResponseEntity.ok(response);
    }

    @GetMapping
    public ResponseEntity<List<GetBlockResponse>> getBlocksByBox(@PathVariable("boxId") Long boxId) {
        List<GetBlockResponse> blocks = getBlocksByStadiumUseCase.getBlocksByStadium(boxId);
        return ResponseEntity.ok(blocks);
    }

    @DeleteMapping("/{blockId}")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public ResponseEntity<Void> deleteBlock(
            @PathVariable("blockId") Long blockId) {
        deleteBlockUseCase.deleteBlock(blockId);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/match/{matchId}")
    public ResponseEntity<List<GetBlockResponse>> getBlocksByBoxForMatch(
            @PathVariable("boxId") Long boxId,
            @PathVariable("matchId") Long matchId) {
        List<GetBlockResponse> blocks = getMatchSpecificBlocksUseCase.getBlocksByStadiumForMatch(boxId, matchId);
        return ResponseEntity.ok(blocks);
    }
}