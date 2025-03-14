package nl.fontys.s3.ticketmaster.business.interfaces.blockinterfaces;

import nl.fontys.s3.ticketmaster.domain.block.GetBlockResponse;

import java.util.List;

public interface GetMatchSpecificBlocksUseCase {
    List<GetBlockResponse> getBlocksByStadiumForMatch(Long boxId, Long matchId);
}
