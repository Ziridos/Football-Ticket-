package nl.fontys.s3.ticketmaster.business.interfaces.blockinterfaces;

import nl.fontys.s3.ticketmaster.domain.block.GetBlockResponse;

import java.util.List;

public interface GetBlocksByStadiumUseCase {
    List<GetBlockResponse> getBlocksByStadium(Long stadiumId);
}
