package nl.fontys.s3.ticketmaster.business.impl.stadiumimpl;

import lombok.AllArgsConstructor;
import nl.fontys.s3.ticketmaster.business.interfaces.stadiuminterfaces.GetAllStadiumsUseCase;
import nl.fontys.s3.ticketmaster.business.interfaces.stadiuminterfaces.StadiumConverter;
import nl.fontys.s3.ticketmaster.domain.stadium.GetAllStadiumsRequest;
import nl.fontys.s3.ticketmaster.domain.stadium.GetAllStadiumsResponse;
import nl.fontys.s3.ticketmaster.domain.stadium.Stadium;
import nl.fontys.s3.ticketmaster.persitence.StadiumRepository;
import nl.fontys.s3.ticketmaster.persitence.entity.StadiumEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@AllArgsConstructor
public class GetAllStadiumsUseCaseImpl implements GetAllStadiumsUseCase {
    private final StadiumRepository stadiumRepository;
    private final StadiumConverter stadiumConverter;

    @Override
    @Transactional
    public GetAllStadiumsResponse getAllStadiums(GetAllStadiumsRequest request) {
        Sort sort = Sort.by(Sort.Direction.fromString(request.getSortDirection()), request.getSortBy());
        Pageable pageable = PageRequest.of(request.getPage(), request.getSize(), sort);

        Page<StadiumEntity> pageResults = stadiumRepository.findByFilters(
                request.getName(),
                request.getCity(),
                request.getCountry(),
                pageable
        );

        List<Stadium> stadiums = pageResults.getContent().stream()
                .map(stadiumConverter::convertToDomain)
                .toList();

        return GetAllStadiumsResponse.builder()
                .stadiums(stadiums)
                .totalElements(pageResults.getTotalElements())
                .totalPages(pageResults.getTotalPages())
                .currentPage(pageResults.getNumber())
                .build();
    }
}