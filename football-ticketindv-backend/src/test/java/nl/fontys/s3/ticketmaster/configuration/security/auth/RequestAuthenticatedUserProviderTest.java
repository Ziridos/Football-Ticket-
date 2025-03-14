package nl.fontys.s3.ticketmaster.configuration.security.auth;

import nl.fontys.s3.ticketmaster.configuration.security.token.AccessToken;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class RequestAuthenticatedUserProviderTest {

    @Mock
    private SecurityContext securityContext;

    @Mock
    private Authentication authentication;

    @Mock
    private AccessToken accessToken;

    @InjectMocks
    private RequestAuthenticatedUserProvider userProvider;

    @BeforeEach
    void setUp() {
        SecurityContextHolder.clearContext();
    }

    @AfterEach
    void tearDown() {
        SecurityContextHolder.clearContext();
    }

    @Test
    void getAuthenticatedUserInRequest() {
        // Test 1: With valid AccessToken
        when(securityContext.getAuthentication()).thenReturn(authentication);
        when(authentication.getDetails()).thenReturn(accessToken);
        SecurityContextHolder.setContext(securityContext);

        AccessToken result = userProvider.getAuthenticatedUserInRequest();
        assertEquals(accessToken, result);

        // Test 2: With null authentication
        SecurityContextHolder.clearContext();
        when(securityContext.getAuthentication()).thenReturn(null);
        SecurityContextHolder.setContext(securityContext);

        result = userProvider.getAuthenticatedUserInRequest();
        assertNull(result);

        // Test 3: With non-AccessToken details
        SecurityContextHolder.clearContext();
        when(securityContext.getAuthentication()).thenReturn(authentication);
        when(authentication.getDetails()).thenReturn("not an access token");
        SecurityContextHolder.setContext(securityContext);

        result = userProvider.getAuthenticatedUserInRequest();
        assertNull(result);

        // Test 4: With null security context
        SecurityContextHolder.clearContext();

        result = userProvider.getAuthenticatedUserInRequest();
        assertNull(result);

        // Test 5: With null details
        when(securityContext.getAuthentication()).thenReturn(authentication);
        when(authentication.getDetails()).thenReturn(null);
        SecurityContextHolder.setContext(securityContext);

        result = userProvider.getAuthenticatedUserInRequest();
        assertNull(result);
    }
}