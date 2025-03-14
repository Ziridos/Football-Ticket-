package nl.fontys.s3.ticketmaster.business.interfaces.matchinterfaces;

import nl.fontys.s3.ticketmaster.domain.match.*;
import nl.fontys.s3.ticketmaster.persitence.entity.MatchEntity;

public interface MatchConverter {
    MatchEntity convertToEntity(CreateMatchRequest request);
    CreateMatchResponse convertToCreateMatchResponse(MatchEntity matchEntity);
    GetMatchResponse convertToGetMatchResponse(MatchEntity matchEntity);
    UpdateMatchResponse convertToUpdateMatchResponse(MatchEntity matchEntity);
    Match convertToMatch(MatchEntity matchEntity);
    MatchEntity updateEntityFromRequest(MatchEntity match, UpdateMatchRequest request);
    MatchDTO toMatchDTO(MatchEntity entity);
}