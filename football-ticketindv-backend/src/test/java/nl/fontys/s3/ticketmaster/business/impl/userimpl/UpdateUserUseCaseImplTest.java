package nl.fontys.s3.ticketmaster.business.impl.userimpl;

import nl.fontys.s3.ticketmaster.business.exception.InvalidUserException;
import nl.fontys.s3.ticketmaster.business.interfaces.userinterfaces.UserValidator;
import nl.fontys.s3.ticketmaster.domain.user.Role;
import nl.fontys.s3.ticketmaster.domain.user.UpdateUserRequest;
import nl.fontys.s3.ticketmaster.domain.user.UpdateUserResponse;
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
class UpdateUserUseCaseImplTest {

    @Mock
    private UserRepository userRepositoryMock;

    @Mock
    private UserConverter userConverterMock;

    @Mock
    private UserValidator userValidatorMock;

    @InjectMocks
    private UpdateUserUseCaseImpl updateUserUseCase;

    @Test
    void updateUser_shouldUpdateExistingUser() {
        // Arrange
        Long userId = 1L;
        UpdateUserRequest request = UpdateUserRequest.builder()
                .id(userId)
                .name("John Doe Updated")
                .email("john.doe.updated@example.com")
                .password("newpassword")
                .address("456 Another St")
                .phone("987-654-3210")
                .city("UpdatedCity")
                .country("UpdatedCountry")
                .postalCode("54321")
                .role(Role.ADMIN)
                .build();

        UserEntity existingUser = UserEntity.builder()
                .id(userId)
                .name("John Doe")
                .email("john.doe@example.com")
                .password("oldpassword")
                .address("123 Main St")
                .phone("123-456-7890")
                .city("OldCity")
                .country("OldCountry")
                .postalCode("12345")
                .role(Role.USER)
                .build();

        UserEntity updatedUser = UserEntity.builder()
                .id(userId)
                .name("John Doe Updated")
                .email("john.doe.updated@example.com")
                .password("newpassword")
                .address("456 Another St")
                .phone("987-654-3210")
                .city("UpdatedCity")
                .country("UpdatedCountry")
                .postalCode("54321")
                .role(Role.ADMIN)
                .build();

        UpdateUserResponse response = UpdateUserResponse.builder()
                .id(userId)
                .name("John Doe Updated")
                .email("john.doe.updated@example.com")
                .build();

        when(userRepositoryMock.findById(userId)).thenReturn(Optional.of(existingUser));
        when(userConverterMock.updateEntityFromRequest(existingUser, request)).thenReturn(updatedUser);
        when(userRepositoryMock.save(updatedUser)).thenReturn(updatedUser);
        when(userConverterMock.convertToUpdateUserResponse(updatedUser)).thenReturn(response);

        // Act
        UpdateUserResponse actualResponse = updateUserUseCase.updateUser(request);

        // Assert
        verify(userValidatorMock).validateUpdateUserRequest(request);
        verify(userRepositoryMock).findById(userId);
        verify(userConverterMock).updateEntityFromRequest(existingUser, request);
        verify(userRepositoryMock).save(updatedUser);
        verify(userConverterMock).convertToUpdateUserResponse(updatedUser);

        assertEquals(response, actualResponse);
    }

    @Test
    void updateUser_shouldThrowExceptionWhenRequestIsNull() {
        // Act & Assert
        assertThrows(InvalidUserException.class, () -> {
            updateUserUseCase.updateUser(null);
        });

        verify(userValidatorMock, never()).validateUpdateUserRequest(null);
        verify(userRepositoryMock, never()).findById(anyLong());
        verify(userConverterMock, never()).updateEntityFromRequest(any(), any());
        verify(userRepositoryMock, never()).save(any(UserEntity.class));
    }

    @Test
    void updateUser_shouldThrowExceptionWhenUserNotFound() {
        // Arrange
        UpdateUserRequest request = UpdateUserRequest.builder()
                .id(1L)
                .build();

        when(userRepositoryMock.findById(1L)).thenReturn(Optional.empty());

        // Act & Assert
        assertThrows(InvalidUserException.class, () -> {
            updateUserUseCase.updateUser(request);
        });

        verify(userValidatorMock).validateUpdateUserRequest(request);
        verify(userRepositoryMock).findById(1L);
        verify(userRepositoryMock, never()).save(any(UserEntity.class));
        verify(userConverterMock, never()).updateEntityFromRequest(any(), any());
    }
}
