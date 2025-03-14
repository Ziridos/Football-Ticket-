package nl.fontys.s3.ticketmaster.business.impl.userimpl;

import nl.fontys.s3.ticketmaster.business.exception.UsernameAlreadyExistsException;
import nl.fontys.s3.ticketmaster.business.exception.InvalidUserException;
import nl.fontys.s3.ticketmaster.domain.user.CreateUserRequest;
import nl.fontys.s3.ticketmaster.domain.user.RegisterUserRequest;
import nl.fontys.s3.ticketmaster.domain.user.Role;
import nl.fontys.s3.ticketmaster.persitence.UserRepository;
import nl.fontys.s3.ticketmaster.domain.user.UpdateUserRequest;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class UserValidatorImplTest {

    @Mock
    private UserRepository userRepositoryMock;

    @InjectMocks
    private UserValidatorImpl userValidator;

    @Test
    void validateUniqueUsername_withUniqueUsername_shouldNotThrowException() {
        // Arrange
        String username = "newuser";
        when(userRepositoryMock.existsByName(username)).thenReturn(false);

        // Act & Assert
        assertDoesNotThrow(() -> userValidator.validateUniqueUsername(username));
        verify(userRepositoryMock).existsByName(username);
    }

    @Test
    void validateUniqueUsername_withExistingUsername_shouldThrowUsernameAlreadyExistsException() {
        // Arrange
        String username = "existinguser";
        when(userRepositoryMock.existsByName(username)).thenReturn(true);

        // Act & Assert
        assertThrows(UsernameAlreadyExistsException.class, () -> userValidator.validateUniqueUsername(username));
        verify(userRepositoryMock).existsByName(username);
    }

    @Test
    void validateUpdateUserRequest_withValidRequest_shouldNotThrowException() {
        // Arrange
        UpdateUserRequest request = new UpdateUserRequest();
        request.setId(1L);
        when(userRepositoryMock.existsById(1L)).thenReturn(true);

        // Act & Assert
        assertDoesNotThrow(() -> userValidator.validateUpdateUserRequest(request));
        verify(userRepositoryMock).existsById(1L);
    }

    @Test
    void validateUpdateUserRequest_withNullRequest_shouldThrowInvalidUserException() {
        // Act & Assert
        InvalidUserException exception = assertThrows(InvalidUserException.class,
                () -> userValidator.validateUpdateUserRequest(null));
        assertEquals("USER_ID_INVALID", exception.getMessage());
    }

    @Test
    void validateUpdateUserRequest_withNullId_shouldThrowInvalidUserException() {
        // Arrange
        UpdateUserRequest request = new UpdateUserRequest();

        // Act & Assert
        InvalidUserException exception = assertThrows(InvalidUserException.class,
                () -> userValidator.validateUpdateUserRequest(request));
        assertEquals("USER_ID_INVALID", exception.getMessage());
    }

    @Test
    void validateUpdateUserRequest_withNonExistentUser_shouldThrowInvalidUserException() {
        // Arrange
        UpdateUserRequest request = new UpdateUserRequest();
        request.setId(1L);
        when(userRepositoryMock.existsById(1L)).thenReturn(false);

        // Act & Assert
        InvalidUserException exception = assertThrows(InvalidUserException.class,
                () -> userValidator.validateUpdateUserRequest(request));
        assertEquals("USER_NOT_FOUND", exception.getMessage());
        verify(userRepositoryMock).existsById(1L);
    }

    @Test
    void validateUserExists_withValidUserId_shouldNotThrowException() {
        // Arrange
        Long userId = 1L;
        when(userRepositoryMock.existsById(userId)).thenReturn(true);

        // Act & Assert
        assertDoesNotThrow(() -> userValidator.validateUserExists(userId));
        verify(userRepositoryMock).existsById(userId);
    }

    @Test
    void validateUserExists_withNullUserId_shouldThrowInvalidUserException() {
        // Act & Assert
        InvalidUserException exception = assertThrows(InvalidUserException.class,
                () -> userValidator.validateUserExists(null));
        assertEquals("USER_ID_INVALID", exception.getMessage());
    }

    @Test
    void validateUserExists_withNonExistentUser_shouldThrowInvalidUserException() {
        // Arrange
        Long userId = 1L;
        when(userRepositoryMock.existsById(userId)).thenReturn(false);

        // Act & Assert
        InvalidUserException exception = assertThrows(InvalidUserException.class,
                () -> userValidator.validateUserExists(userId));
        assertEquals("USER_NOT_FOUND", exception.getMessage());
        verify(userRepositoryMock).existsById(userId);
    }

    @Test
    void validateCreateUserRequest_withNullRequest_shouldThrowInvalidUserException() {
        // Act & Assert
        InvalidUserException exception = assertThrows(InvalidUserException.class,
                () -> userValidator.validateCreateUserRequest(null));
        assertEquals("REQUEST_IS_NULL", exception.getMessage());
    }

    @Test
    void validateCreateUserRequest_withValidRequest_shouldNotThrowException() {
        // Arrange
        CreateUserRequest request = CreateUserRequest.builder()
                .name("test")
                .email("test@test.com")
                .password("password")
                .address("address")
                .phone("phone")
                .country("country")
                .city("city")
                .postalCode("postalCode")
                .role(Role.USER)
                .build();

        // Act & Assert
        assertDoesNotThrow(() -> userValidator.validateCreateUserRequest(request));
    }

    @Test
    void validateCreateUserRequest_withMissingFields_shouldThrowInvalidUserException() {
        // Arrange
        CreateUserRequest request = new CreateUserRequest();

        // Act & Assert
        InvalidUserException exception = assertThrows(InvalidUserException.class,
                () -> userValidator.validateCreateUserRequest(request));
        assertEquals("NAME_REQUIRED", exception.getMessage());
    }

    @Test
    void validateRegisterRequest_withNullRequest_shouldThrowInvalidUserException() {
        // Act & Assert
        InvalidUserException exception = assertThrows(InvalidUserException.class,
                () -> userValidator.validateRegisterRequest(null));
        assertEquals("REQUEST_IS_NULL", exception.getMessage());
    }

    @Test
    void validateRegisterRequest_withValidRequest_shouldNotThrowException() {
        // Arrange
        RegisterUserRequest request = RegisterUserRequest.builder()
                .name("test")
                .email("test@test.com")
                .password("password")
                .address("address")
                .phone("phone")
                .country("country")
                .city("city")
                .postalCode("postalCode")
                .build();

        // Act & Assert
        assertDoesNotThrow(() -> userValidator.validateRegisterRequest(request));
    }

    @Test
    void validateRegisterRequest_withMissingFields_shouldThrowInvalidUserException() {
        // Arrange
        RegisterUserRequest request = new RegisterUserRequest();

        // Act & Assert
        InvalidUserException exception = assertThrows(InvalidUserException.class,
                () -> userValidator.validateRegisterRequest(request));
        assertEquals("NAME_REQUIRED", exception.getMessage());
    }

    @Test
    void validateUpdateUserRequestFields_withValidRequest_shouldNotThrowException() {
        // Arrange
        UpdateUserRequest request = UpdateUserRequest.builder()
                .id(1L)
                .name("test")
                .email("test@test.com")
                .password("password")
                .address("address")
                .phone("phone")
                .country("country")
                .city("city")
                .postalCode("postalCode")
                .role(Role.USER)
                .build();

        when(userRepositoryMock.existsById(1L)).thenReturn(true);

        // Act & Assert
        assertDoesNotThrow(() -> userValidator.validateUpdateUserRequestFields(request));
        verify(userRepositoryMock).existsById(1L);
    }

    @Test
    void validateEmail_withInvalidEmailFormat_shouldThrowInvalidUserException() {
        // Arrange
        CreateUserRequest request = CreateUserRequest.builder()
                .name("test")
                .email("invalid-email")
                .password("password")
                .address("address")
                .phone("phone")
                .country("country")
                .city("city")
                .postalCode("postalCode")
                .role(Role.USER)
                .build();

        // Act & Assert
        InvalidUserException exception = assertThrows(InvalidUserException.class,
                () -> userValidator.validateCreateUserRequest(request));
        assertEquals("INVALID_EMAIL_FORMAT", exception.getMessage());
    }

    @Test
    void validateCreateUserRequest_withNullRole_shouldThrowInvalidUserException() {
        // Arrange
        CreateUserRequest request = CreateUserRequest.builder()
                .name("test")
                .email("test@test.com")
                .password("password")
                .address("address")
                .phone("phone")
                .country("country")
                .city("city")
                .postalCode("postalCode")
                .role(null)
                .build();

        // Act & Assert
        InvalidUserException exception = assertThrows(InvalidUserException.class,
                () -> userValidator.validateCreateUserRequest(request));
        assertEquals("ROLE_REQUIRED", exception.getMessage());
    }

    @Test
    void validateUpdateUserRequestFields_withNullRole_shouldThrowInvalidUserException() {
        // Arrange
        UpdateUserRequest request = UpdateUserRequest.builder()
                .id(1L)
                .name("test")
                .email("test@test.com")
                .password("password")
                .address("address")
                .phone("phone")
                .country("country")
                .city("city")
                .postalCode("postalCode")
                .role(null)
                .build();

        when(userRepositoryMock.existsById(1L)).thenReturn(true);

        // Act & Assert
        InvalidUserException exception = assertThrows(InvalidUserException.class,
                () -> userValidator.validateUpdateUserRequestFields(request));
        assertEquals("ROLE_REQUIRED", exception.getMessage());
        verify(userRepositoryMock).existsById(1L);
    }
}