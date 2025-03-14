package nl.fontys.s3.ticketmaster.business.impl.userimpl;

import nl.fontys.s3.ticketmaster.domain.user.Role;
import nl.fontys.s3.ticketmaster.domain.user.User;
import nl.fontys.s3.ticketmaster.persitence.UserRepository;
import nl.fontys.s3.ticketmaster.persitence.entity.UserEntity;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class GetUserUseCaseImplTest {

    @Mock
    private UserRepository userRepositoryMock;

    @Mock
    private UserConverter userConverterMock;

    @InjectMocks
    private GetUserUseCaseImpl getUserUseCase;

    @Test
    void getUser_shouldReturnUser_whenUserExists() {
        // Arrange
        Long userId = 1L;
        UserEntity userEntity = UserEntity.builder()
                .id(userId)
                .name("John Doe")
                .email("john.doe@example.com")
                .password("password")
                .address("123 Main St")
                .phone("123-456-7890")
                .city("City")
                .country("Country")
                .postalCode("12345")
                .role(Role.USER)
                .build();

        User user = User.builder()
                .id(userId)
                .name("John Doe")
                .email("john.doe@example.com")
                .role(Role.USER)
                .build();

        when(userRepositoryMock.findById(userId)).thenReturn(Optional.of(userEntity));
        when(userConverterMock.convert(userEntity)).thenReturn(user);

        // Act
        Optional<User> result = getUserUseCase.getUser(userId);

        // Assert
        assertEquals(Optional.of(user), result);
        verify(userRepositoryMock).findById(userId);
        verify(userConverterMock).convert(userEntity);
    }

    @Test
    void getUser_shouldReturnEmpty_whenUserDoesNotExist() {
        // Arrange
        Long userId = 1L;
        when(userRepositoryMock.findById(userId)).thenReturn(Optional.empty());

        // Act
        Optional<User> result = getUserUseCase.getUser(userId);

        // Assert
        assertFalse(result.isPresent());
        verify(userRepositoryMock).findById(userId);
        verify(userConverterMock, never()).convert(any(UserEntity.class));
    }
}
