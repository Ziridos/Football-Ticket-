package nl.fontys.s3.ticketmaster.configuration.security.auth;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import nl.fontys.s3.ticketmaster.configuration.security.token.AccessToken;
import nl.fontys.s3.ticketmaster.configuration.security.token.AccessTokenDecoder;
import nl.fontys.s3.ticketmaster.configuration.security.token.exception.InvalidAccessTokenException;
import nl.fontys.s3.ticketmaster.domain.user.Role;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.test.util.ReflectionTestUtils;

import java.io.IOException;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class AuthenticationRequestFilterTest {

    @Mock
    private AccessTokenDecoder accessTokenDecoder;

    @Mock
    private HttpServletRequest request;

    @Mock
    private HttpServletResponse response;

    @Mock
    private FilterChain filterChain;

    @InjectMocks
    private AuthenticationRequestFilter authenticationRequestFilter;

    private static final String VALID_TOKEN = "valid_token";
    private static final String COOKIE_NAME = "jwt-token";
    private static final String BEARER_PREFIX = "Bearer ";

    @BeforeEach
    void setUp() {
        ReflectionTestUtils.setField(authenticationRequestFilter, "cookieName", COOKIE_NAME);
        SecurityContextHolder.clearContext();
    }

    @Test
    void doFilterInternal_WithValidTokenInCookie_ShouldAuthenticate() throws ServletException, IOException {
        // Arrange
        Cookie[] cookies = new Cookie[]{new Cookie(COOKIE_NAME, VALID_TOKEN)};
        when(request.getCookies()).thenReturn(cookies);

        AccessToken accessToken = createAccessToken("testUser", Role.USER);
        when(accessTokenDecoder.decode(VALID_TOKEN)).thenReturn(accessToken);

        // Act
        authenticationRequestFilter.doFilterInternal(request, response, filterChain);

        // Assert
        assertNotNull(SecurityContextHolder.getContext().getAuthentication());
        assertEquals("testUser", SecurityContextHolder.getContext().getAuthentication().getName());
        verify(filterChain).doFilter(request, response);
    }

    @Test
    void doFilterInternal_WithValidTokenInHeader_ShouldAuthenticate() throws ServletException, IOException {
        // Arrange
        when(request.getCookies()).thenReturn(null);
        when(request.getHeader("Authorization")).thenReturn(BEARER_PREFIX + VALID_TOKEN);

        AccessToken accessToken = createAccessToken("testUser", Role.ADMIN);
        when(accessTokenDecoder.decode(VALID_TOKEN)).thenReturn(accessToken);

        // Act
        authenticationRequestFilter.doFilterInternal(request, response, filterChain);

        // Assert
        assertNotNull(SecurityContextHolder.getContext().getAuthentication());
        assertTrue(SecurityContextHolder.getContext().getAuthentication()
                .getAuthorities().stream()
                .anyMatch(a -> a.getAuthority().equals("ROLE_ADMIN")));
        verify(filterChain).doFilter(request, response);
    }

    @Test
    void doFilterInternal_WithInvalidToken_ShouldContinueChainWithoutAuthentication() throws ServletException, IOException {
        // Arrange
        when(request.getCookies()).thenReturn(new Cookie[]{new Cookie(COOKIE_NAME, "invalid_token")});
        when(accessTokenDecoder.decode("invalid_token")).thenThrow(new InvalidAccessTokenException("Invalid token"));

        // Act
        authenticationRequestFilter.doFilterInternal(request, response, filterChain);

        // Assert
        assertNull(SecurityContextHolder.getContext().getAuthentication());
        verify(filterChain).doFilter(request, response);
    }

    @Test
    void doFilterInternal_WithNoToken_ShouldContinueChainWithoutAuthentication() throws ServletException, IOException {
        // Arrange
        when(request.getCookies()).thenReturn(null);
        when(request.getHeader("Authorization")).thenReturn(null);

        // Act
        authenticationRequestFilter.doFilterInternal(request, response, filterChain);

        // Assert
        assertNull(SecurityContextHolder.getContext().getAuthentication());
        verify(filterChain).doFilter(request, response);
    }

    @Test
    void doFilterInternal_WithAdminToken_ShouldSetAdminAuthorities() throws ServletException, IOException {
        // Arrange
        when(request.getCookies()).thenReturn(new Cookie[]{new Cookie(COOKIE_NAME, VALID_TOKEN)});

        AccessToken accessToken = createAccessToken("adminUser", Role.ADMIN);
        when(accessTokenDecoder.decode(VALID_TOKEN)).thenReturn(accessToken);

        // Act
        authenticationRequestFilter.doFilterInternal(request, response, filterChain);

        // Assert
        assertNotNull(SecurityContextHolder.getContext().getAuthentication());
        assertTrue(SecurityContextHolder.getContext().getAuthentication()
                .getAuthorities().stream()
                .anyMatch(a -> a.getAuthority().equals("ROLE_ADMIN")));
        assertTrue(SecurityContextHolder.getContext().getAuthentication()
                .getAuthorities().stream()
                .anyMatch(a -> a.getAuthority().equals("ROLE_USER")));
        verify(filterChain).doFilter(request, response);
    }

    @Test
    void doFilterInternal_WithMalformedBearerToken_ShouldContinueChainWithoutAuthentication() throws ServletException, IOException {
        // Arrange
        when(request.getCookies()).thenReturn(null);
        when(request.getHeader("Authorization")).thenReturn("malformed_token");

        // Act
        authenticationRequestFilter.doFilterInternal(request, response, filterChain);

        // Assert
        assertNull(SecurityContextHolder.getContext().getAuthentication());
        verify(filterChain).doFilter(request, response);
    }

    private AccessToken createAccessToken(String subject, Role role) {
        AccessToken accessToken = mock(AccessToken.class);
        when(accessToken.getSubject()).thenReturn(subject);
        when(accessToken.getRole()).thenReturn(role);
        return accessToken;
    }
}