package nl.fontys.s3.ticketmaster.business.impl.blockimpl;

import lombok.AllArgsConstructor;
import nl.fontys.s3.ticketmaster.business.interfaces.blockinterfaces.GetBlocksByStadiumUseCase;
import nl.fontys.s3.ticketmaster.business.interfaces.blockinterfaces.BlockConverter;
import nl.fontys.s3.ticketmaster.domain.block.GetBlockResponse;
import nl.fontys.s3.ticketmaster.persitence.BlockRepository;
import nl.fontys.s3.ticketmaster.persitence.entity.BlockEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;


@Service
@AllArgsConstructor
public class GetBlocksByStadiumUseCaseImpl implements GetBlocksByStadiumUseCase {
    private final BlockRepository blockRepository;
    private final BlockConverter blockConverter;

    @Override
    @Transactional
    public List<GetBlockResponse> getBlocksByStadium(Long stadiumId) {
        List<BlockEntity> blockEntities = blockRepository.findByBoxId(stadiumId);
        return blockEntities.stream()
                .map(blockConverter::convertToGetBlockResponse)
                .toList();
    }
}