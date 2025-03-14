package nl.fontys.s3.ticketmaster.configuration.security;

import nl.fontys.s3.ticketmaster.business.interfaces.userinterfaces.GetUserUseCase;
import nl.fontys.s3.ticketmaster.domain.user.User;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.core.Authentication;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class CustomPermissionEvaluatorTest {

    @Mock
    private GetUserUseCase getUserUseCase;

    @Mock
    private Authentication authentication;

    private CustomPermissionEvaluator permissionEvaluator;
    private User testUser;

    @BeforeEach
    void setUp() {
        permissionEvaluator = new CustomPermissionEvaluator(getUserUseCase);

        testUser = User.builder()
                .id(1L)
                .email("test@example.com")
                .build();
    }

    @Test
    void hasPermission_WithMatchingEmail_ReturnsTrue() {
        // Arrange
        when(authentication.getName()).thenReturn("test@example.com");
        when(getUserUseCase.getUser(1L)).thenReturn(Optional.of(testUser));

        // Act
        boolean result = permissionEvaluator.hasPermission(authentication, "1", "read");

        // Assert
        assertTrue(result);
    }

    @Test
    void hasPermission_WithDifferentEmail_ReturnsFalse() {
        // Arrange
        when(authentication.getName()).thenReturn("other@example.com");
        when(getUserUseCase.getUser(1L)).thenReturn(Optional.of(testUser));

        // Act
        boolean result = permissionEvaluator.hasPermission(authentication, "1", "read");

        // Assert
        assertFalse(result);
    }

    @Test
    void hasPermission_WithNullAuthentication_ReturnsFalse() {
        // Act
        boolean result = permissionEvaluator.hasPermission(null, "1", "read");

        // Assert
        assertFalse(result);
    }

    @Test
    void hasPermission_WithNullTargetDomainObject_ReturnsFalse() {
        // Act
        boolean result = permissionEvaluator.hasPermission(authentication, null, "read");

        // Assert
        assertFalse(result);
    }


    @Test
    void hasPermission_WithUserNotFound_ReturnsFalse() {
        // Arrange
        when(getUserUseCase.getUser(1L)).thenReturn(Optional.empty());

        // Act
        boolean result = permissionEvaluator.hasPermission(authentication, "1", "read");

        // Assert
        assertFalse(result);
    }

    @Test
    void hasPermission_WithInvalidUserId_ReturnsFalse() {
        // Act
        boolean result = permissionEvaluator.hasPermission(authentication, "invalid_id", "read");

        // Assert
        assertFalse(result);
    }

    @Test
    void testHasPermission_AlwaysReturnsFalse() {
        // Act
        boolean result = permissionEvaluator.hasPermission(authentication, 1L, "user", "read");

        // Assert
        assertFalse(result);
    }
}