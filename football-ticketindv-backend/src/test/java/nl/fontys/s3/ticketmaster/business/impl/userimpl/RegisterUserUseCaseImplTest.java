package nl.fontys.s3.ticketmaster.business.impl.userimpl;

import nl.fontys.s3.ticketmaster.business.exception.InvalidUserException;
import nl.fontys.s3.ticketmaster.business.exception.UsernameAlreadyExistsException;
import nl.fontys.s3.ticketmaster.business.interfaces.userinterfaces.UserService;
import nl.fontys.s3.ticketmaster.business.interfaces.userinterfaces.UserValidator;
import nl.fontys.s3.ticketmaster.domain.user.*;
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
class RegisterUserUseCaseImplTest {
    @Mock
    private UserService userService;
    @Mock
    private UserValidator userValidator;
    @Mock
    private UserConverter userConverter;
    @Mock
    private PasswordEncoder passwordEncoder;

    @InjectMocks
    private RegisterUserUseCaseImpl registerUserUseCase;

    private RegisterUserRequest registerRequest;
    private CreateUserRequest createRequest;
    private UserEntity userEntity;
    private CreateUserResponse response;
    private static final String ORIGINAL_PASSWORD = "password123";
    private static final String ENCRYPTED_PASSWORD = "encrypted_password";

    @BeforeEach
    void setUp() {
        registerRequest = RegisterUserRequest.builder()
                .name("testUser")
                .email("test@test.com")
                .password(ORIGINAL_PASSWORD)
                .address("Test Address")
                .phone("1234567890")
                .city("Test City")
                .country("Test Country")
                .postalCode("12345")
                .build();

        createRequest = CreateUserRequest.builder()
                .name("testUser")
                .email("test@test.com")
                .password(ENCRYPTED_PASSWORD)
                .address("Test Address")
                .phone("1234567890")
                .city("Test City")
                .country("Test Country")
                .postalCode("12345")
                .role(Role.USER)
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
                .role(Role.USER)
                .build();

        response = CreateUserResponse.builder()
                .id(1L)
                .name("testUser")
                .build();
    }

    @Test
    void registerUser_Success() {
        doNothing().when(userValidator).validateUniqueUsername(registerRequest.getName());
        when(passwordEncoder.encode(ORIGINAL_PASSWORD)).thenReturn(ENCRYPTED_PASSWORD);
        when(userConverter.convertRegisterToCreateRequest(registerRequest)).thenReturn(createRequest);
        when(userConverter.convertToEntity(createRequest)).thenReturn(userEntity);
        when(userService.saveUser(userEntity)).thenReturn(userEntity);
        when(userConverter.convertToCreateUserResponse(userEntity)).thenReturn(response);

        CreateUserResponse result = registerUserUseCase.registerUser(registerRequest);

        assertNotNull(result);
        assertEquals(response.getId(), result.getId());
        assertEquals(response.getName(), result.getName());
        verify(userValidator).validateUniqueUsername(registerRequest.getName());
        verify(passwordEncoder).encode(ORIGINAL_PASSWORD);
        verify(userConverter).convertRegisterToCreateRequest(registerRequest);
        verify(userConverter).convertToEntity(createRequest);
        verify(userService).saveUser(userEntity);
        verify(userConverter).convertToCreateUserResponse(userEntity);
    }

    @Test
    void registerUser_ThrowsUsernameAlreadyExistsException() {
        doThrow(new UsernameAlreadyExistsException())
                .when(userValidator).validateUniqueUsername(registerRequest.getName());

        assertThrows(UsernameAlreadyExistsException.class,
                () -> registerUserUseCase.registerUser(registerRequest));

        verify(userValidator).validateUniqueUsername(registerRequest.getName());
        verifyNoInteractions(passwordEncoder);
        verifyNoInteractions(userService);
    }

    @Test
    void registerUser_ThrowsInvalidUserException_WhenSavingFails() {
        doNothing().when(userValidator).validateUniqueUsername(registerRequest.getName());
        when(passwordEncoder.encode(ORIGINAL_PASSWORD)).thenReturn(ENCRYPTED_PASSWORD);
        when(userConverter.convertRegisterToCreateRequest(registerRequest)).thenReturn(createRequest);
        when(userConverter.convertToEntity(createRequest)).thenReturn(userEntity);
        when(userService.saveUser(userEntity)).thenThrow(new RuntimeException("Database error"));

        InvalidUserException exception = assertThrows(InvalidUserException.class,
                () -> registerUserUseCase.registerUser(registerRequest));
        assertEquals("Error occurred while registering user", exception.getMessage());

        verify(userConverter, never()).convertToCreateUserResponse(any(UserEntity.class));
    }

    @Test
    void registerUser_ThrowsInvalidUserException_WhenPasswordEncodingFails() {
        doNothing().when(userValidator).validateUniqueUsername(registerRequest.getName());
        when(passwordEncoder.encode(ORIGINAL_PASSWORD)).thenThrow(new RuntimeException("Encoding failed"));

        InvalidUserException exception = assertThrows(InvalidUserException.class,
                () -> registerUserUseCase.registerUser(registerRequest));
        assertEquals("Error occurred while registering user", exception.getMessage());

        verify(userValidator).validateUniqueUsername(registerRequest.getName());
        verify(passwordEncoder).encode(ORIGINAL_PASSWORD);
        verifyNoInteractions(userService);
    }
}