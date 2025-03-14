package nl.fontys.s3.ticketmaster.domain.match;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import nl.fontys.s3.ticketmaster.persitence.entity.ClubEntity;
import nl.fontys.s3.ticketmaster.persitence.entity.CompetitionEntity;

import java.time.LocalDateTime;
import java.util.Map;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Match {
    private Long matchId;
    private ClubEntity homeClub;
    private ClubEntity awayClub;
    private LocalDateTime matchDateTime;
    private CompetitionEntity competition;
    private Long tickets;
    private Map<Long, Boolean> availableSeats;
    private Map<Long, Double> matchSpecificBoxPrices;
    private Map<Long, Double> matchSpecificSeatPrices;
}
