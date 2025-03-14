package nl.fontys.s3.ticketmaster.business.impl.matchimpl;

import lombok.AllArgsConstructor;
import nl.fontys.s3.ticketmaster.business.interfaces.matchinterfaces.GetAllMatchesUseCase;
import nl.fontys.s3.ticketmaster.business.interfaces.matchinterfaces.MatchConverter;
import nl.fontys.s3.ticketmaster.domain.match.GetAllMatchesRequest;
import nl.fontys.s3.ticketmaster.domain.match.GetAllMatchesResponse;
import nl.fontys.s3.ticketmaster.domain.match.Match;
import nl.fontys.s3.ticketmaster.persitence.MatchRepository;
import nl.fontys.s3.ticketmaster.persitence.entity.MatchEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;

@Service
@AllArgsConstructor
public class GetAllMatchesUseCaseImpl implements GetAllMatchesUseCase {
    private final MatchRepository matchRepository;
    private final MatchConverter matchConverter;

    @Override
    @Transactional
    public GetAllMatchesResponse getAllMatches(GetAllMatchesRequest request) {
        Sort sort = Sort.by(Sort.Direction.fromString(request.getSortDirection()), request.getSortBy());
        Pageable pageable = PageRequest.of(request.getPage(), request.getSize(), sort);

        Page<MatchEntity> pageResults = matchRepository.findByFilters(
                request.getHomeClubName(),
                request.getAwayClubName(),
                request.getCompetitionName(),
                request.getDate(),
                pageable
        );

        List<Match> matches = pageResults.getContent().stream()
                .map(matchConverter::convertToMatch)
                .toList();

        return GetAllMatchesResponse.builder()
                .matches(matches)
                .totalElements(pageResults.getTotalElements())
                .totalPages(pageResults.getTotalPages())
                .currentPage(pageResults.getNumber())
                .build();
    }
}