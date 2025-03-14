package nl.fontys.s3.ticketmaster.business.interfaces.boxinterfaces;

import nl.fontys.s3.ticketmaster.domain.box.GetBoxResponse;

import java.util.List;

public interface GetMatchSpecificBoxesUseCase {
    List<GetBoxResponse> getBoxesByStadiumForMatch(Long stadiumId, Long matchId);
}
