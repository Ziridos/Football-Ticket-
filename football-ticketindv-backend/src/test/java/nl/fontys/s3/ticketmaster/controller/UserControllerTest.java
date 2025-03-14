package nl.fontys.s3.ticketmaster.controller;

import nl.fontys.s3.ticketmaster.business.interfaces.userinterfaces.*;
import nl.fontys.s3.ticketmaster.domain.user.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Arrays;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class UserControllerTest {

    @Mock
    private GetUserUseCase getUserUseCase;
    @Mock
    private GetAllUsersUseCase getAllUsersUseCase;
    @Mock
    private DeleteUserUseCase deleteUserUseCase;
    @Mock
    private CreateUserUseCase createUserUseCase;
    @Mock
    private UpdateUserUseCase updateUserUseCase;
    @Mock
    private RegisterUserUseCase registerUserUseCase;
    @Mock
    private UserDetails userDetails;

    @InjectMocks
    private UserController userController;

    private User testUser;
    private CreateUserRequest createUserRequest;
    private CreateUserResponse createUserResponse;
    private UpdateUserRequest updateUserRequest;
    private RegisterUserRequest registerUserRequest;
    private GetAllUsersResponse getAllUsersResponse;

    @BeforeEach
    void setUp() {
        testUser = User.builder()
                .id(1L)
                .name("Test User")
                .email("test@example.com")
                .address("123 Test St")
                .phone("1234567890")
                .country("Test Country")
                .city("Test City")
                .postalCode("12345")
                .role(Role.USER)
                .build();

        createUserRequest = CreateUserRequest.builder()
                .name("New User")
                .email("new@example.com")
                .password("password123")
                .address("456 New St")
                .phone("0987654321")
                .country("New Country")
                .city("New City")
                .postalCode("54321")
                .build();

        createUserResponse = CreateUserResponse.builder()
                .id(2L)
                .build();

        updateUserRequest = UpdateUserRequest.builder()
                .name("Updated User")
                .email("updated@example.com")
                .password("newpassword123")
                .address("789 Updated St")
                .phone("1122334455")
                .country("Updated Country")
                .city("Updated City")
                .postalCode("98765")
                .build();

        registerUserRequest = RegisterUserRequest.builder()
                .name("New User")
                .email("register@example.com")
                .password("password123")
                .address("456 New St")
                .phone("0987654321")
                .country("New Country")
                .city("New City")
                .postalCode("54321")
                .build();

        getAllUsersResponse = GetAllUsersResponse.builder()
                .users(Arrays.asList(testUser))
                .totalElements(1)
                .totalPages(1)
                .currentPage(0)
                .build();
    }

    @Test
    void getAllUsers_WithDefaultParameters_ReturnsAllUsers() {
        // Arrange
        GetAllUsersRequest expectedRequest = GetAllUsersRequest.builder()
                .page(0)
                .size(10)
                .sortBy("id")
                .sortDirection("ASC")
                .build();

        when(getAllUsersUseCase.getAllUsers(expectedRequest))
                .thenReturn(getAllUsersResponse);

        // Act
        ResponseEntity<GetAllUsersResponse> response = userController.getAllUsers(
                null, null, null, 0, 10, "id", "ASC");

        // Assert
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals(1, response.getBody().getUsers().size());
        assertEquals(1, response.getBody().getTotalPages());
        assertEquals(0, response.getBody().getCurrentPage());
        verify(getAllUsersUseCase).getAllUsers(expectedRequest);
    }

    @Test
    void getAllUsers_WithFilters_ReturnsFilteredUsers() {
        // Arrange
        GetAllUsersRequest expectedRequest = GetAllUsersRequest.builder()
                .name("Test")
                .email("test@example.com")
                .role(Role.ADMIN)
                .page(0)
                .size(10)
                .sortBy("name")
                .sortDirection("DESC")
                .build();

        when(getAllUsersUseCase.getAllUsers(expectedRequest))
                .thenReturn(getAllUsersResponse);

        // Act
        ResponseEntity<GetAllUsersResponse> response = userController.getAllUsers(
                "Test", "test@example.com", Role.ADMIN, 0, 10, "name", "DESC");

        // Assert
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        verify(getAllUsersUseCase).getAllUsers(expectedRequest);
    }

    @Test
    void getAllUsers_WithPagination_ReturnsPagedResult() {
        // Arrange
        GetAllUsersRequest expectedRequest = GetAllUsersRequest.builder()
                .page(2)
                .size(5)
                .sortBy("id")
                .sortDirection("ASC")
                .build();

        GetAllUsersResponse pagedResponse = GetAllUsersResponse.builder()
                .users(Arrays.asList(testUser))
                .totalElements(11)
                .totalPages(3)
                .currentPage(2)
                .build();

        when(getAllUsersUseCase.getAllUsers(expectedRequest))
                .thenReturn(pagedResponse);

        // Act
        ResponseEntity<GetAllUsersResponse> response = userController.getAllUsers(
                null, null, null, 2, 5, "id", "ASC");

        // Assert
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals(2, response.getBody().getCurrentPage());
        assertEquals(3, response.getBody().getTotalPages());
        assertEquals(11, response.getBody().getTotalElements());
        verify(getAllUsersUseCase).getAllUsers(expectedRequest);
    }

    @Test
    void getUserById_ExistingUser_ReturnsUser() {
        when(getUserUseCase.getUser(1L)).thenReturn(Optional.of(testUser));

        ResponseEntity<User> response = userController.getUserById(1L, userDetails);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(testUser, response.getBody());
        verify(getUserUseCase).getUser(1L);
    }

    @Test
    void getUserById_NonExistingUser_ReturnsNotFound() {
        when(getUserUseCase.getUser(999L)).thenReturn(Optional.empty());

        ResponseEntity<User> response = userController.getUserById(999L, userDetails);

        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        assertNull(response.getBody());
        verify(getUserUseCase).getUser(999L);
    }

    @Test
    void deleteUser_ExistingUser_ReturnsNoContent() {
        doNothing().when(deleteUserUseCase).deleteUser(1L);

        ResponseEntity<Void> response = userController.deleteUser(1L);

        assertEquals(HttpStatus.NO_CONTENT, response.getStatusCode());
        assertNull(response.getBody());
        verify(deleteUserUseCase).deleteUser(1L);
    }

    @Test
    void createUser_ValidRequest_ReturnsCreated() {
        when(createUserUseCase.createUser(createUserRequest)).thenReturn(createUserResponse);

        ResponseEntity<CreateUserResponse> response = userController.createUser(createUserRequest);

        assertEquals(HttpStatus.CREATED, response.getStatusCode());
        assertEquals(createUserResponse, response.getBody());
        assertEquals(2L, response.getBody().getId());
        verify(createUserUseCase).createUser(createUserRequest);
    }

    @Test
    void updateUser_ExistingUser_ReturnsNoContent() {
        ResponseEntity<Void> response = userController.updateUser(1L, updateUserRequest, userDetails);

        assertEquals(HttpStatus.NO_CONTENT, response.getStatusCode());
        assertEquals(1L, updateUserRequest.getId());
        assertNull(response.getBody());
        verify(updateUserUseCase).updateUser(updateUserRequest);
    }

    @Test
    void registerUser_ValidRequest_ReturnsCreated() {
        // Arrange
        when(registerUserUseCase.registerUser(registerUserRequest)).thenReturn(createUserResponse);

        // Act
        ResponseEntity<CreateUserResponse> response = userController.registerUser(registerUserRequest);

        // Assert
        assertEquals(HttpStatus.CREATED, response.getStatusCode());
        assertEquals(createUserResponse, response.getBody());
        assertEquals(2L, response.getBody().getId());
        verify(registerUserUseCase).registerUser(registerUserRequest);
    }
}