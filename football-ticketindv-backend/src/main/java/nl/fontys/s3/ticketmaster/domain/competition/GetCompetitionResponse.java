package nl.fontys.s3.ticketmaster.domain.competition;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class GetCompetitionResponse {
    private String competitionName;
    private Long competitionId;
}
