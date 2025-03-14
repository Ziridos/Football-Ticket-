package nl.fontys.s3.ticketmaster.domain.competition;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Builder
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Competition {
    private String competitionName;
    private Long competitionId;
}
