package nl.fontys.s3.ticketmaster.domain.competition;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class UpdateCompetitionRequest {
    @NotBlank
    private String competitionName;
    @NotNull
    private Long competitionId;
}
