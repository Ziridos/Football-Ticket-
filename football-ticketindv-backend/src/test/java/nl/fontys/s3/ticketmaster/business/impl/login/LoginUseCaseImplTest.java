package nl.fontys.s3.ticketmaster.business.impl.login;

import nl.fontys.s3.ticketmaster.persitence.UserRepository;
import nl.fontys.s3.ticketmaster.persitence.entity.UserEntity;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class LoginUseCaseImplTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private PasswordEncoder passwordEncoder;

    @InjectMocks
    private LoginUseCaseImpl loginUseCase;

    private UserEntity validUser;
    private static final String VALID_EMAIL = "test@example.com";
    private static final String VALID_PASSWORD = "password123";
    private static final String ENCODED_PASSWORD = "encodedPassword123";

    @BeforeEach
    void setUp() {
        validUser = UserEntity.builder()
                .id(1L)
                .email(VALID_EMAIL)
                .password(ENCODED_PASSWORD)
                .name("Test User")
                .build();
    }

    @Test
    void authenticateUser_WithValidCredentials_ShouldReturnUser() {
        // Arrange
        when(userRepository.findByEmail(VALID_EMAIL)).thenReturn(Optional.of(validUser));
        when(passwordEncoder.matches(VALID_PASSWORD, ENCODED_PASSWORD)).thenReturn(true);

        // Act
        Optional<UserEntity> result = loginUseCase.authenticateUser(VALID_EMAIL, VALID_PASSWORD);

        // Assert
        assertTrue(result.isPresent());
        assertEquals(validUser, result.get());
        verify(userRepository).findByEmail(VALID_EMAIL);
        verify(passwordEncoder).matches(VALID_PASSWORD, ENCODED_PASSWORD);
    }

    @Test
    void authenticateUser_WithInvalidEmail_ShouldReturnEmpty() {
        // Arrange
        String invalidEmail = "nonexistent@example.com";
        when(userRepository.findByEmail(invalidEmail)).thenReturn(Optional.empty());

        // Act
        Optional<UserEntity> result = loginUseCase.authenticateUser(invalidEmail, VALID_PASSWORD);

        // Assert
        assertTrue(result.isEmpty());
        verify(userRepository).findByEmail(invalidEmail);
        verify(passwordEncoder, never()).matches(any(), any());
    }

    @Test
    void authenticateUser_WithInvalidPassword_ShouldReturnEmpty() {
        // Arrange
        String invalidPassword = "wrongPassword";
        when(userRepository.findByEmail(VALID_EMAIL)).thenReturn(Optional.of(validUser));
        when(passwordEncoder.matches(invalidPassword, ENCODED_PASSWORD)).thenReturn(false);

        // Act
        Optional<UserEntity> result = loginUseCase.authenticateUser(VALID_EMAIL, invalidPassword);

        // Assert
        assertTrue(result.isEmpty());
        verify(userRepository).findByEmail(VALID_EMAIL);
        verify(passwordEncoder).matches(invalidPassword, ENCODED_PASSWORD);
    }

    @Test
    void authenticateUser_WithNullEmail_ShouldReturnEmpty() {
        // Act
        Optional<UserEntity> result = loginUseCase.authenticateUser(null, VALID_PASSWORD);

        // Assert
        assertTrue(result.isEmpty());
        verify(userRepository).findByEmail(null);
        verify(passwordEncoder, never()).matches(any(), any());
    }

    @Test
    void authenticateUser_WithNullPassword_ShouldReturnEmpty() {
        // Arrange
        when(userRepository.findByEmail(VALID_EMAIL)).thenReturn(Optional.of(validUser));
        when(passwordEncoder.matches(null, ENCODED_PASSWORD)).thenReturn(false);

        // Act
        Optional<UserEntity> result = loginUseCase.authenticateUser(VALID_EMAIL, null);

        // Assert
        assertTrue(result.isEmpty());
        verify(userRepository).findByEmail(VALID_EMAIL);
        verify(passwordEncoder).matches(null, ENCODED_PASSWORD);
    }

    @Test
    void authenticateUser_WithEmptyEmail_ShouldReturnEmpty() {
        // Act
        Optional<UserEntity> result = loginUseCase.authenticateUser("", VALID_PASSWORD);

        // Assert
        assertTrue(result.isEmpty());
        verify(userRepository).findByEmail("");
        verify(passwordEncoder, never()).matches(any(), any());
    }

    @Test
    void authenticateUser_WithEmptyPassword_ShouldReturnEmpty() {
        // Arrange
        when(userRepository.findByEmail(VALID_EMAIL)).thenReturn(Optional.of(validUser));
        when(passwordEncoder.matches("", ENCODED_PASSWORD)).thenReturn(false);

        // Act
        Optional<UserEntity> result = loginUseCase.authenticateUser(VALID_EMAIL, "");

        // Assert
        assertTrue(result.isEmpty());
        verify(userRepository).findByEmail(VALID_EMAIL);
        verify(passwordEncoder).matches("", ENCODED_PASSWORD);
    }
}