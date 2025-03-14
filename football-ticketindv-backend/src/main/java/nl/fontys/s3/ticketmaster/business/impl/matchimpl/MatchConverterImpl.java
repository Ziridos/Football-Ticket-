package nl.fontys.s3.ticketmaster.business.impl.matchimpl;

import jakarta.persistence.EntityNotFoundException;
import lombok.AllArgsConstructor;
import nl.fontys.s3.ticketmaster.business.interfaces.clubinterfaces.ClubService;
import nl.fontys.s3.ticketmaster.business.interfaces.competitioninterfaces.CompetitionService;
import nl.fontys.s3.ticketmaster.business.interfaces.matchinterfaces.MatchConverter;
import nl.fontys.s3.ticketmaster.domain.match.*;
import nl.fontys.s3.ticketmaster.persitence.entity.MatchEntity;
import nl.fontys.s3.ticketmaster.persitence.entity.SeatEntity;
import org.springframework.stereotype.Component;

import java.util.Map;
import java.util.stream.Collectors;

@Component
@AllArgsConstructor
public class MatchConverterImpl implements MatchConverter {
    private final ClubService clubService;
    private final CompetitionService competitionService;

    private Map<Long, Boolean> convertAvailableSeats(Map<SeatEntity, Boolean> availableSeats) {
        return availableSeats.entrySet().stream()
                .collect(Collectors.toMap(
                        entry -> entry.getKey().getId(),
                        Map.Entry::getValue
                ));
    }

    @Override
    public MatchEntity convertToEntity(CreateMatchRequest request) {
        return MatchEntity.builder()
                .homeClub(clubService.findByClubName(request.getHomeClubName())
                        .orElseThrow(() -> new EntityNotFoundException("Home club not found")))
                .awayClub(clubService.findByClubName(request.getAwayClubName())
                        .orElseThrow(() -> new EntityNotFoundException("Away club not found")))
                .matchDateTime(request.getMatchDateTime())
                .competition(competitionService.findByCompetitionName(request.getCompetitionName())
                        .orElseThrow(() -> new EntityNotFoundException("Competition not found")))
                .build();
    }

    @Override
    public CreateMatchResponse convertToCreateMatchResponse(MatchEntity matchEntity) {
        return CreateMatchResponse.builder()
                .matchId(matchEntity.getId())
                .homeClub(matchEntity.getHomeClub())
                .awayClub(matchEntity.getAwayClub())
                .matchDateTime(matchEntity.getMatchDateTime())
                .competition(matchEntity.getCompetition())
                .availableSeats(convertAvailableSeats(matchEntity.getAvailableSeats()))
                .build();
    }

    @Override
    public MatchEntity updateEntityFromRequest(MatchEntity match, UpdateMatchRequest request) {
        match.setHomeClub(clubService.findByClubName(request.getHomeClubName())
                .orElseThrow(() -> new EntityNotFoundException("Home club not found")));
        match.setAwayClub(clubService.findByClubName(request.getAwayClubName())
                .orElseThrow(() -> new EntityNotFoundException("Away club not found")));
        match.setMatchDateTime(request.getMatchDateTime());
        match.setCompetition(competitionService.findByCompetitionName(request.getCompetitionName())
                .orElseThrow(() -> new EntityNotFoundException("Competition not found")));
        return match;
    }

    @Override
    public GetMatchResponse convertToGetMatchResponse(MatchEntity matchEntity) {
        return GetMatchResponse.builder()
                .matchId(matchEntity.getId())
                .homeClub(matchEntity.getHomeClub())
                .awayClub(matchEntity.getAwayClub())
                .matchDateTime(matchEntity.getMatchDateTime())
                .competition(matchEntity.getCompetition())
                .availableSeats(convertAvailableSeats(matchEntity.getAvailableSeats()))
                .build();
    }

    @Override
    public UpdateMatchResponse convertToUpdateMatchResponse(MatchEntity matchEntity) {
        return UpdateMatchResponse.builder()
                .matchId(matchEntity.getId())
                .homeClub(matchEntity.getHomeClub())
                .awayClub(matchEntity.getAwayClub())
                .matchDateTime(matchEntity.getMatchDateTime())
                .competition(matchEntity.getCompetition())
                .availableSeats(convertAvailableSeats(matchEntity.getAvailableSeats()))
                .build();
    }

    @Override
    public Match convertToMatch(MatchEntity matchEntity) {
        return Match.builder()
                .matchId(matchEntity.getId())
                .homeClub(matchEntity.getHomeClub())
                .awayClub(matchEntity.getAwayClub())
                .matchDateTime(matchEntity.getMatchDateTime())
                .competition(matchEntity.getCompetition())
                .availableSeats(convertAvailableSeats(matchEntity.getAvailableSeats()))
                .build();
    }

    public MatchDTO toMatchDTO(MatchEntity entity) {
        return MatchDTO.builder()
                .matchId(entity.getId())
                .homeClub(entity.getHomeClub())
                .awayClub(entity.getAwayClub())
                .matchDateTime(entity.getMatchDateTime())
                .competition(entity.getCompetition())
                .build();
    }
}