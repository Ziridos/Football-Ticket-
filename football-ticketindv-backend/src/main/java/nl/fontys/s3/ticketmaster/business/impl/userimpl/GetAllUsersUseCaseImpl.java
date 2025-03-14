package nl.fontys.s3.ticketmaster.business.impl.userimpl;

import nl.fontys.s3.ticketmaster.business.interfaces.userinterfaces.GetAllUsersUseCase;
import nl.fontys.s3.ticketmaster.domain.user.GetAllUsersRequest;
import nl.fontys.s3.ticketmaster.domain.user.GetAllUsersResponse;
import nl.fontys.s3.ticketmaster.domain.user.User;
import nl.fontys.s3.ticketmaster.persitence.UserRepository;
import nl.fontys.s3.ticketmaster.persitence.entity.UserEntity;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@AllArgsConstructor
public class GetAllUsersUseCaseImpl implements GetAllUsersUseCase {
    private final UserRepository userRepository;
    private final UserConverter userConverter;

    @Override
    @Transactional
    public GetAllUsersResponse getAllUsers(GetAllUsersRequest request) {
        Sort sort = Sort.by(Sort.Direction.fromString(request.getSortDirection()), request.getSortBy());
        Pageable pageable = PageRequest.of(request.getPage(), request.getSize(), sort);

        Page<UserEntity> pageResults = userRepository.findByFilters(
                request.getName(),
                request.getEmail(),
                request.getRole(),
                pageable
        );

        List<User> users = pageResults.getContent().stream()
                .map(userConverter::convert)
                .toList();

        return GetAllUsersResponse.builder()
                .users(users)
                .totalElements(pageResults.getTotalElements())
                .totalPages(pageResults.getTotalPages())
                .currentPage(pageResults.getNumber())
                .build();
    }
}