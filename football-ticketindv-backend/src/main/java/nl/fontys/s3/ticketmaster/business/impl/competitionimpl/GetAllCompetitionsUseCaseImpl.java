package nl.fontys.s3.ticketmaster.business.impl.competitionimpl;

import lombok.AllArgsConstructor;
import nl.fontys.s3.ticketmaster.business.interfaces.competitioninterfaces.GetAllCompetitionsUseCase;
import nl.fontys.s3.ticketmaster.business.interfaces.competitioninterfaces.CompetitionConverter;
import nl.fontys.s3.ticketmaster.domain.competition.GetAllCompetitionsRequest;
import nl.fontys.s3.ticketmaster.domain.competition.GetAllCompetitionsResponse;
import nl.fontys.s3.ticketmaster.domain.competition.Competition;
import nl.fontys.s3.ticketmaster.persitence.CompetitionRepository;
import nl.fontys.s3.ticketmaster.persitence.entity.CompetitionEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@AllArgsConstructor
public class GetAllCompetitionsUseCaseImpl implements GetAllCompetitionsUseCase {
    private final CompetitionRepository competitionRepository;
    private final CompetitionConverter competitionConverter;

    @Override
    @Transactional
    public GetAllCompetitionsResponse getAllCompetitions(GetAllCompetitionsRequest request) {
        Sort sort = Sort.by(Sort.Direction.fromString(request.getSortDirection()), request.getSortBy());
        Pageable pageable = PageRequest.of(request.getPage(), request.getSize(), sort);

        Page<CompetitionEntity> pageResults = competitionRepository.findByFilters(
                request.getName(),
                pageable
        );

        List<Competition> competitions = pageResults.getContent().stream()
                .map(competitionConverter::convertToCompetition)
                .toList();

        return GetAllCompetitionsResponse.builder()
                .competitions(competitions)
                .totalElements(pageResults.getTotalElements())
                .totalPages(pageResults.getTotalPages())
                .currentPage(pageResults.getNumber())
                .build();
    }
}