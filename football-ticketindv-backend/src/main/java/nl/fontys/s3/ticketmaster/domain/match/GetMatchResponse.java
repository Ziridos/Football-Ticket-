package nl.fontys.s3.ticketmaster.domain.match;

import lombok.Builder;
import lombok.Data;
import nl.fontys.s3.ticketmaster.persitence.entity.ClubEntity;
import nl.fontys.s3.ticketmaster.persitence.entity.CompetitionEntity;

import java.time.LocalDateTime;
import java.util.Map;

@Data
@Builder
public class GetMatchResponse {
    private Long matchId;
    private ClubEntity homeClub;
    private ClubEntity awayClub;
    private LocalDateTime matchDateTime;
    private CompetitionEntity competition;
    private Map<Long, Boolean> availableSeats;
}
