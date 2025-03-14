package nl.fontys.s3.ticketmaster.business.impl.userimpl;

import nl.fontys.s3.ticketmaster.persitence.UserRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.times;

@ExtendWith(MockitoExtension.class)
class DeleteUserUseCaseImplTest {

    @Mock
    private UserRepository userRepositoryMock;

    @InjectMocks
    private DeleteUserUseCaseImpl deleteUserUseCase;

    @Test
    void deleteUser_shouldCallRepositoryToDeleteUser() {
        // Arrange
        long userId = 1L;

        // Act
        deleteUserUseCase.deleteUser(userId);

        // Assert
        verify(userRepositoryMock, times(1)).deleteById(userId);
    }
}
