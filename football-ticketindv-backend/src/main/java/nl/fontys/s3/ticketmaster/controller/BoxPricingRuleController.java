package nl.fontys.s3.ticketmaster.controller;

import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import nl.fontys.s3.ticketmaster.business.interfaces.boxpricingrule.*;
import nl.fontys.s3.ticketmaster.domain.boxpricingrule.*;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/box-pricing-rules")
@CrossOrigin(origins = "http://localhost:5173")
@AllArgsConstructor
public class BoxPricingRuleController {
    private final CreateBoxPricingRuleUseCase createBoxPricingRuleUseCase;
    private final GetBoxPricingRulesForStadiumUseCase getBoxPricingRulesForStadiumUseCase;
    private final DeleteBoxPricingRuleUseCase deleteBoxPricingRuleUseCase;
    private final GetBoxPricingRuleUseCase getBoxPricingRuleUseCase;
    private final UpdateBoxPricingRuleUseCase updateBoxPricingRuleUseCase;

    @PostMapping
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public ResponseEntity<CreateBoxPricingRuleResponse> createBoxPricingRule(@RequestBody @Valid CreateBoxPricingRuleRequest request) {
        CreateBoxPricingRuleResponse response = createBoxPricingRuleUseCase.createBoxPricingRule(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @GetMapping("/stadium/{stadiumId}")
    public ResponseEntity<List<GetBoxPricingRuleResponse>> getPricingRulesForStadium(@PathVariable Long stadiumId) {
        List<GetBoxPricingRuleResponse> response = getBoxPricingRulesForStadiumUseCase.getPricingRulesForStadium(stadiumId);
        return ResponseEntity.ok(response);
    }

    @DeleteMapping("/{ruleId}")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public ResponseEntity<Void> deleteBoxPricingRule(@PathVariable Long ruleId) {
        deleteBoxPricingRuleUseCase.deleteBoxPricingRule(ruleId);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/{ruleId}")
    public ResponseEntity<GetBoxPricingRuleResponse> getPricingRule(@PathVariable Long ruleId) {
        GetBoxPricingRuleResponse response = getBoxPricingRuleUseCase.getPricingRule(ruleId);
        return ResponseEntity.ok(response);
    }

    @PutMapping("/{ruleId}")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public ResponseEntity<UpdateBoxPricingRuleResponse> updateBoxPricingRule(
            @PathVariable Long ruleId,
            @RequestBody @Valid UpdateBoxPricingRuleRequest request) {
        request.setId(ruleId);
        UpdateBoxPricingRuleResponse response = updateBoxPricingRuleUseCase.updateBoxPricingRule(request);
        return ResponseEntity.ok(response);
    }


}