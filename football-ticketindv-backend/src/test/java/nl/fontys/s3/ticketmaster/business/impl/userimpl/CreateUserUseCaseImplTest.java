package nl.fontys.s3.ticketmaster.business.impl.userimpl;

import nl.fontys.s3.ticketmaster.business.exception.InvalidUserException;
import nl.fontys.s3.ticketmaster.business.exception.UsernameAlreadyExistsException;
import nl.fontys.s3.ticketmaster.business.interfaces.userinterfaces.UserValidator;
import nl.fontys.s3.ticketmaster.business.interfaces.userinterfaces.UserService;
import nl.fontys.s3.ticketmaster.domain.user.CreateUserRequest;
import nl.fontys.s3.ticketmaster.domain.user.CreateUserResponse;
import nl.fontys.s3.ticketmaster.domain.user.Role;
import nl.fontys.s3.ticketmaster.persitence.entity.UserEntity;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class CreateUserUseCaseImplTest {

    @Mock
    private UserService userService;
    @Mock
    private UserValidator userValidator;
    @Mock
    private UserConverter userConverter;
    @Mock
    private PasswordEncoder passwordEncoder;

    @InjectMocks
    private CreateUserUseCaseImpl createUserUseCase;

    private CreateUserRequest request;
    private UserEntity userEntity;
    private CreateUserResponse response;
    private static final String ORIGINAL_PASSWORD = "password123";
    private static final String ENCRYPTED_PASSWORD = "encrypted_password";

    @BeforeEach
    void setUp() {
        request = CreateUserRequest.builder()
                .name("testUser")
                .email("test@test.com")
                .password(ORIGINAL_PASSWORD)
                .address("Test Address")
                .phone("1234567890")
                .city("Test City")
                .country("Test Country")
                .postalCode("12345")
                .build();

        userEntity = UserEntity.builder()
                .id(1L)
                .name("testUser")
                .email("test@test.com")
                .password(ENCRYPTED_PASSWORD)
                .address("Test Address")
                .phone("1234567890")
                .city("Test City")
                .country("Test Country")
                .postalCode("12345")
                .role(Role.ADMIN)
                .build();

        response = CreateUserResponse.builder()
                .id(1L)
                .name("testUser")
                .build();
    }

    @Test
    void createUser_Success() {
        doNothing().when(userValidator).validateUniqueUsername(request.getName());
        when(passwordEncoder.encode(ORIGINAL_PASSWORD)).thenReturn(ENCRYPTED_PASSWORD);
        when(userConverter.convertToEntity(request)).thenReturn(userEntity);
        when(userService.saveUser(userEntity)).thenReturn(userEntity);
        when(userConverter.convertToCreateUserResponse(userEntity)).thenReturn(response);

        CreateUserResponse result = createUserUseCase.createUser(request);

        assertNotNull(result);
        assertEquals(response.getId(), result.getId());
        assertEquals(response.getName(), result.getName());

        verify(userValidator).validateUniqueUsername(request.getName());
        verify(passwordEncoder).encode(ORIGINAL_PASSWORD);
        verify(userConverter).convertToEntity(request);
        verify(userService).saveUser(userEntity);
        verify(userConverter).convertToCreateUserResponse(userEntity);
    }

    @Test
    void createUser_ThrowsUsernameAlreadyExistsException() {
        doThrow(new UsernameAlreadyExistsException())
                .when(userValidator).validateUniqueUsername(request.getName());

        assertThrows(UsernameAlreadyExistsException.class, () -> createUserUseCase.createUser(request));

        verify(userValidator).validateUniqueUsername(request.getName());
        verifyNoInteractions(passwordEncoder);
        verifyNoInteractions(userConverter);
        verifyNoInteractions(userService);
    }

    @Test
    void createUser_ThrowsInvalidUserException_WhenSavingFails() {
        doNothing().when(userValidator).validateUniqueUsername(request.getName());
        when(passwordEncoder.encode(ORIGINAL_PASSWORD)).thenReturn(ENCRYPTED_PASSWORD);
        when(userConverter.convertToEntity(request)).thenReturn(userEntity);
        when(userService.saveUser(userEntity)).thenThrow(new RuntimeException("Database error"));

        InvalidUserException exception = assertThrows(InvalidUserException.class,
                () -> createUserUseCase.createUser(request));
        assertEquals("Error occurred while creating user", exception.getMessage());

        verify(userValidator).validateUniqueUsername(request.getName());
        verify(passwordEncoder).encode(ORIGINAL_PASSWORD);
        verify(userConverter).convertToEntity(request);
        verify(userService).saveUser(userEntity);
        verify(userConverter, never()).convertToCreateUserResponse(any(UserEntity.class));
    }

    @Test
    void createUser_ThrowsInvalidUserException_WhenPasswordEncodingFails() {
        doNothing().when(userValidator).validateUniqueUsername(request.getName());
        when(passwordEncoder.encode(ORIGINAL_PASSWORD)).thenThrow(new RuntimeException("Encoding failed"));

        InvalidUserException exception = assertThrows(InvalidUserException.class,
                () -> createUserUseCase.createUser(request));
        assertEquals("Error occurred while creating user", exception.getMessage());

        verify(userValidator).validateUniqueUsername(request.getName());
        verify(passwordEncoder).encode(ORIGINAL_PASSWORD);
        verifyNoInteractions(userService);
        verify(userConverter, never()).convertToCreateUserResponse(any(UserEntity.class));
    }
}