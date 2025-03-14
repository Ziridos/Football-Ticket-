package nl.fontys.s3.ticketmaster.business.impl.userimpl;

import nl.fontys.s3.ticketmaster.business.exception.InvalidUserException;
import nl.fontys.s3.ticketmaster.persitence.UserRepository;
import nl.fontys.s3.ticketmaster.persitence.entity.UserEntity;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class UserServiceImplTest {

    @Mock
    private UserRepository userRepositoryMock;

    @InjectMocks
    private UserServiceImpl userService;

    @Test
    void getUserById_withExistingUser_shouldReturnUser() {
        // Arrange
        Long userId = 1L;
        UserEntity expectedUser = UserEntity.builder()
                .id(userId)
                .name("testuser")
                .email("test@example.com")
                .build();

        when(userRepositoryMock.findById(userId)).thenReturn(Optional.of(expectedUser));

        // Act
        UserEntity actualUser = userService.getUserById(userId);

        // Assert
        assertNotNull(actualUser);
        assertEquals(expectedUser, actualUser);
        verify(userRepositoryMock).findById(userId);
    }

    @Test
    void getUserById_withNonExistingUser_shouldThrowRuntimeException() {
        // Arrange
        Long userId = 1L;
        when(userRepositoryMock.findById(userId)).thenReturn(Optional.empty());

        // Act & Assert
        RuntimeException exception = assertThrows(RuntimeException.class,
                () -> userService.getUserById(userId));

        assertEquals("User not found", exception.getMessage());
        verify(userRepositoryMock).findById(userId);
    }

    @Test
    void saveUser_withValidUser_shouldReturnSavedUser() {
        // Arrange
        UserEntity userToSave = UserEntity.builder()
                .name("testuser")
                .email("test@example.com")
                .build();
        when(userRepositoryMock.save(userToSave)).thenReturn(userToSave);

        // Act
        UserEntity savedUser = userService.saveUser(userToSave);

        // Assert
        assertNotNull(savedUser);
        assertEquals(userToSave, savedUser);
        verify(userRepositoryMock).save(userToSave);
    }

    @Test
    void saveUser_whenRepositoryThrowsException_shouldThrowInvalidUserException() {
        // Arrange
        UserEntity userToSave = UserEntity.builder()
                .name("testuser")
                .email("test@example.com")
                .build();
        when(userRepositoryMock.save(userToSave)).thenThrow(new RuntimeException());

        // Act & Assert
        InvalidUserException exception = assertThrows(InvalidUserException.class,
                () -> userService.saveUser(userToSave));

        assertEquals("Error occurred while saving user to database", exception.getMessage());
        verify(userRepositoryMock).save(userToSave);
    }
}