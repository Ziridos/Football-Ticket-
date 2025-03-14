package nl.fontys.s3.ticketmaster.domain.competition;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class CreateCompetitionRequest {
    @NotBlank
    private String competitionName;
}
