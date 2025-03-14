package nl.fontys.s3.ticketmaster.business.impl.userimpl;

import nl.fontys.s3.ticketmaster.domain.user.GetAllUsersRequest;
import nl.fontys.s3.ticketmaster.domain.user.GetAllUsersResponse;
import nl.fontys.s3.ticketmaster.domain.user.Role;
import nl.fontys.s3.ticketmaster.domain.user.User;
import nl.fontys.s3.ticketmaster.persitence.UserRepository;
import nl.fontys.s3.ticketmaster.persitence.entity.UserEntity;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.*;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class GetAllUsersUseCaseImplTest {

    @Mock
    private UserRepository userRepositoryMock;

    @Mock
    private UserConverter userConverterMock;

    @InjectMocks
    private GetAllUsersUseCaseImpl getAllUsersUseCase;

    private UserEntity createUserEntity(Long id, String name, String email, Role role) {
        return UserEntity.builder()
                .id(id)
                .name(name)
                .email(email)
                .password("password")
                .address("123 Main St")
                .phone("123-456-7890")
                .city("City")
                .country("Country")
                .postalCode("12345")
                .role(role)
                .build();
    }

    private User createUser(Long id, String name, String email, Role role) {
        return User.builder()
                .id(id)
                .name(name)
                .email(email)
                .role(role)
                .build();
    }

    @Test
    void getAllUsers_WithPagination_ShouldReturnPagedResponse() {
        // Arrange
        UserEntity userEntity1 = createUserEntity(1L, "John Doe", "john@example.com", Role.USER);
        UserEntity userEntity2 = createUserEntity(2L, "Jane Doe", "jane@example.com", Role.ADMIN);

        User user1 = createUser(1L, "John Doe", "john@example.com", Role.USER);
        User user2 = createUser(2L, "Jane Doe", "jane@example.com", Role.ADMIN);

        Page<UserEntity> pagedResponse = new PageImpl<>(
                List.of(userEntity1, userEntity2),
                PageRequest.of(0, 10, Sort.by("name").ascending()),
                2
        );

        GetAllUsersRequest request = GetAllUsersRequest.builder()
                .page(0)
                .size(10)
                .sortBy("name")
                .sortDirection("ASC")
                .build();

        when(userRepositoryMock.findByFilters(null, null, null,
                PageRequest.of(0, 10, Sort.by("name").ascending())))
                .thenReturn(pagedResponse);
        when(userConverterMock.convert(userEntity1)).thenReturn(user1);
        when(userConverterMock.convert(userEntity2)).thenReturn(user2);

        // Act
        GetAllUsersResponse result = getAllUsersUseCase.getAllUsers(request);

        // Assert
        assertEquals(2, result.getTotalElements());
        assertEquals(1, result.getTotalPages());
        assertEquals(0, result.getCurrentPage());
        assertEquals(List.of(user1, user2), result.getUsers());
    }

    @Test
    void getAllUsers_WithFilters_ShouldReturnFilteredResults() {
        // Arrange
        UserEntity userEntity = createUserEntity(1L, "John Doe", "john@example.com", Role.USER);
        User user = createUser(1L, "John Doe", "john@example.com", Role.USER);

        Page<UserEntity> pagedResponse = new PageImpl<>(
                List.of(userEntity),
                PageRequest.of(0, 10, Sort.by("name").ascending()),
                1
        );

        GetAllUsersRequest request = GetAllUsersRequest.builder()
                .page(0)
                .size(10)
                .sortBy("name")
                .sortDirection("ASC")
                .name("John")
                .email("john@example.com")
                .role(Role.USER)
                .build();

        when(userRepositoryMock.findByFilters("John", "john@example.com", Role.USER,
                PageRequest.of(0, 10, Sort.by("name").ascending())))
                .thenReturn(pagedResponse);
        when(userConverterMock.convert(userEntity)).thenReturn(user);

        // Act
        GetAllUsersResponse result = getAllUsersUseCase.getAllUsers(request);

        // Assert
        assertEquals(1, result.getTotalElements());
        assertEquals(1, result.getTotalPages());
        assertEquals(List.of(user), result.getUsers());
    }

    @Test
    void getAllUsers_WithDescendingSort_ShouldReturnSortedResults() {
        // Arrange
        UserEntity userEntity1 = createUserEntity(1L, "John Doe", "john@example.com", Role.USER);
        UserEntity userEntity2 = createUserEntity(2L, "Alice Smith", "alice@example.com", Role.USER);

        User user1 = createUser(1L, "John Doe", "john@example.com", Role.USER);
        User user2 = createUser(2L, "Alice Smith", "alice@example.com", Role.USER);

        Page<UserEntity> pagedResponse = new PageImpl<>(
                List.of(userEntity1, userEntity2),
                PageRequest.of(0, 10, Sort.by("name").descending()),
                2
        );

        GetAllUsersRequest request = GetAllUsersRequest.builder()
                .page(0)
                .size(10)
                .sortBy("name")
                .sortDirection("DESC")
                .build();

        when(userRepositoryMock.findByFilters(null, null, null,
                PageRequest.of(0, 10, Sort.by("name").descending())))
                .thenReturn(pagedResponse);
        when(userConverterMock.convert(userEntity1)).thenReturn(user1);
        when(userConverterMock.convert(userEntity2)).thenReturn(user2);

        // Act
        GetAllUsersResponse result = getAllUsersUseCase.getAllUsers(request);

        // Assert
        assertEquals(2, result.getTotalElements());
        assertEquals(List.of(user1, user2), result.getUsers());
    }

    @Test
    void getAllUsers_EmptyResult_ShouldReturnEmptyResponse() {
        // Arrange
        Page<UserEntity> emptyPage = new PageImpl<>(
                List.of(),
                PageRequest.of(0, 10, Sort.by("name").ascending()),
                0
        );

        GetAllUsersRequest request = GetAllUsersRequest.builder()
                .page(0)
                .size(10)
                .sortBy("name")
                .sortDirection("ASC")
                .build();

        when(userRepositoryMock.findByFilters(null, null, null,
                PageRequest.of(0, 10, Sort.by("name").ascending())))
                .thenReturn(emptyPage);

        // Act
        GetAllUsersResponse result = getAllUsersUseCase.getAllUsers(request);

        // Assert
        assertEquals(0, result.getTotalElements());
        assertEquals(0, result.getTotalPages());
        assertEquals(0, result.getCurrentPage());
        assertEquals(List.of(), result.getUsers());
        verify(userConverterMock, never()).convert(any());
    }
}