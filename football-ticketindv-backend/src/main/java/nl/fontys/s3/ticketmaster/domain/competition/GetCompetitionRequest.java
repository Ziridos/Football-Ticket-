package nl.fontys.s3.ticketmaster.domain.competition;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class GetCompetitionRequest {
    private Long competitionId;
}
