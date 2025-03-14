package nl.fontys.s3.ticketmaster.business.impl.boximpl;

import lombok.AllArgsConstructor;
import nl.fontys.s3.ticketmaster.business.interfaces.boxinterfaces.GetBoxesByStadiumUseCase;
import nl.fontys.s3.ticketmaster.business.interfaces.boxinterfaces.BoxConverter;
import nl.fontys.s3.ticketmaster.domain.box.GetBoxResponse;
import nl.fontys.s3.ticketmaster.persitence.BoxRepository;
import nl.fontys.s3.ticketmaster.persitence.entity.BoxEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@AllArgsConstructor
public class GetBoxesByStadiumUseCaseImpl implements GetBoxesByStadiumUseCase {
    private final BoxRepository boxRepository;
    private final BoxConverter boxConverter;

    @Override
    @Transactional
    public List<GetBoxResponse> getBoxesByStadium(Long stadiumId) {
        List<BoxEntity> boxEntities = boxRepository.findByStadiumId(stadiumId);
        return boxEntities.stream()
                .map(boxConverter::convertToGetBoxResponse)
                .toList();
    }
}