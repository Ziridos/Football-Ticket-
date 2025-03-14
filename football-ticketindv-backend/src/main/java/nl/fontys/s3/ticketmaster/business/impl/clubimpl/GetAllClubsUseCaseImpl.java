package nl.fontys.s3.ticketmaster.business.impl.clubimpl;

import lombok.AllArgsConstructor;
import nl.fontys.s3.ticketmaster.business.interfaces.clubinterfaces.ClubConverter;
import nl.fontys.s3.ticketmaster.business.interfaces.clubinterfaces.GetAllClubsUseCase;
import nl.fontys.s3.ticketmaster.domain.club.GetAllClubsRequest;
import nl.fontys.s3.ticketmaster.domain.club.GetAllClubsResponse;
import nl.fontys.s3.ticketmaster.domain.club.Club;
import nl.fontys.s3.ticketmaster.persitence.ClubRepository;
import nl.fontys.s3.ticketmaster.persitence.entity.ClubEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@AllArgsConstructor
public class GetAllClubsUseCaseImpl implements GetAllClubsUseCase {
    private final ClubRepository clubRepository;
    private final ClubConverter clubConverter;

    @Override
    @Transactional
    public GetAllClubsResponse getAllClubs(GetAllClubsRequest request) {
        Sort sort = Sort.by(Sort.Direction.fromString(request.getSortDirection()), request.getSortBy());
        Pageable pageable = PageRequest.of(request.getPage(), request.getSize(), sort);

        Page<ClubEntity> pageResults = clubRepository.findByFilters(
                request.getName(),
                request.getStadiumName(),
                request.getStadiumCity(),
                request.getStadiumCountry(),
                pageable
        );

        List<Club> clubs = pageResults.getContent().stream()
                .map(clubConverter::convertToClub)
                .toList();

        return GetAllClubsResponse.builder()
                .clubs(clubs)
                .totalElements(pageResults.getTotalElements())
                .totalPages(pageResults.getTotalPages())
                .currentPage(pageResults.getNumber())
                .build();
    }
}