package nl.fontys.s3.ticketmaster.controller;

import io.jsonwebtoken.Claims;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletResponse;
import nl.fontys.s3.ticketmaster.business.interfaces.logininterfaces.LoginUseCase;
import nl.fontys.s3.ticketmaster.configuration.security.jwt.JwtUtils;
import nl.fontys.s3.ticketmaster.domain.login.LoginRequest;
import nl.fontys.s3.ticketmaster.domain.login.LoginResponse;
import nl.fontys.s3.ticketmaster.domain.user.Role;
import nl.fontys.s3.ticketmaster.persitence.entity.UserEntity;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.util.ReflectionTestUtils;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class AuthControllerTest {

    @Mock
    private LoginUseCase loginUseCase;

    @Mock
    private JwtUtils jwtUtils;

    @Mock
    private HttpServletResponse httpServletResponse;

    @InjectMocks
    private AuthController authController;

    @Captor
    private ArgumentCaptor<Cookie> cookieCaptor;

    private LoginRequest loginRequest;
    private UserEntity userEntity;
    private static final String TEST_ACCESS_TOKEN = "test.access.token";
    private static final String TEST_REFRESH_TOKEN = "test.refresh.token";
    private static final String ACCESS_COOKIE_NAME = "access_token";
    private static final String REFRESH_COOKIE_NAME = "refresh_token";
    private static final String REFRESH_TOKEN_PATH = "/tokens/refresh";

    @BeforeEach
    void setUp() {
        ReflectionTestUtils.setField(authController, "accessTokenCookieName", ACCESS_COOKIE_NAME);
        ReflectionTestUtils.setField(authController, "refreshTokenCookieName", REFRESH_COOKIE_NAME);
        ReflectionTestUtils.setField(authController, "refreshTokenPath", REFRESH_TOKEN_PATH);

        loginRequest = LoginRequest.builder()
                .email("test@example.com")
                .password("password123")
                .build();

        userEntity = UserEntity.builder()
                .id(1L)
                .email("test@example.com")
                .password("hashedPassword")
                .name("Test User")
                .role(Role.USER)
                .build();
    }

    @Test
    void login_ValidCredentials_ReturnsLoginResponseAndSetsCookies() {
        // Arrange
        when(loginUseCase.authenticateUser(loginRequest.getEmail(), loginRequest.getPassword()))
                .thenReturn(Optional.of(userEntity));
        when(jwtUtils.generateAccessToken(userEntity)).thenReturn(TEST_ACCESS_TOKEN);
        when(jwtUtils.generateRefreshToken(userEntity)).thenReturn(TEST_REFRESH_TOKEN);

        // Act
        ResponseEntity<LoginResponse> response = authController.login(loginRequest, httpServletResponse);

        // Assert
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals(1L, response.getBody().getUserId());
        assertEquals("test@example.com", response.getBody().getEmail());
        assertEquals(Role.USER, response.getBody().getRole());

        // Verify cookies
        verify(httpServletResponse, times(2)).addCookie(cookieCaptor.capture());
        List<Cookie> cookies = cookieCaptor.getAllValues();

        // Verify access token cookie
        Cookie accessCookie = cookies.get(0);
        assertEquals(ACCESS_COOKIE_NAME, accessCookie.getName());
        assertEquals(TEST_ACCESS_TOKEN, accessCookie.getValue());
        assertTrue(accessCookie.isHttpOnly());
        assertFalse(accessCookie.getSecure());
        assertEquals("/", accessCookie.getPath());
        assertEquals(3600, accessCookie.getMaxAge());
        assertEquals("Lax", accessCookie.getAttribute("SameSite"));

        // Verify refresh token cookie
        Cookie refreshCookie = cookies.get(1);
        assertEquals(REFRESH_COOKIE_NAME, refreshCookie.getName());
        assertEquals(TEST_REFRESH_TOKEN, refreshCookie.getValue());
        assertTrue(refreshCookie.isHttpOnly());
        assertFalse(refreshCookie.getSecure());
        assertEquals(REFRESH_TOKEN_PATH, refreshCookie.getPath());
        assertEquals(604800, refreshCookie.getMaxAge());
        assertEquals("Lax", refreshCookie.getAttribute("SameSite"));
    }

    @Test
    void refreshToken_ValidToken_ReturnsNewTokensAndResponse() {
        // Arrange
        Claims mockClaims = mock(Claims.class);
        when(mockClaims.getSubject()).thenReturn("test@example.com");
        when(jwtUtils.validateJwtToken(TEST_REFRESH_TOKEN)).thenReturn(mockClaims);
        when(jwtUtils.isRefreshToken(mockClaims)).thenReturn(true);
        when(loginUseCase.getUserByEmail("test@example.com")).thenReturn(Optional.of(userEntity));
        when(jwtUtils.generateAccessToken(userEntity)).thenReturn(TEST_ACCESS_TOKEN);
        when(jwtUtils.generateRefreshToken(userEntity)).thenReturn(TEST_REFRESH_TOKEN);

        // Act
        ResponseEntity<LoginResponse> response = authController.refreshToken(TEST_REFRESH_TOKEN, httpServletResponse);

        // Assert
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals(1L, response.getBody().getUserId());

        // Verify both cookies are set
        verify(httpServletResponse, times(2)).addCookie(cookieCaptor.capture());
        List<Cookie> cookies = cookieCaptor.getAllValues();
        assertEquals(2, cookies.size());
    }

    @Test
    void refreshToken_InvalidToken_ReturnsUnauthorized() {
        // Arrange
        when(jwtUtils.validateJwtToken(TEST_REFRESH_TOKEN))
                .thenThrow(new RuntimeException("Invalid token"));

        // Act
        ResponseEntity<LoginResponse> response = authController.refreshToken(TEST_REFRESH_TOKEN, httpServletResponse);

        // Assert
        assertEquals(HttpStatus.UNAUTHORIZED, response.getStatusCode());
        verify(httpServletResponse, never()).addCookie(any(Cookie.class));
    }

    @Test
    void refreshToken_NonRefreshToken_ReturnsUnauthorized() {
        // Arrange
        Claims mockClaims = mock(Claims.class);
        when(jwtUtils.validateJwtToken(TEST_REFRESH_TOKEN)).thenReturn(mockClaims);
        when(jwtUtils.isRefreshToken(mockClaims)).thenReturn(false);

        // Act
        ResponseEntity<LoginResponse> response = authController.refreshToken(TEST_REFRESH_TOKEN, httpServletResponse);

        // Assert
        assertEquals(HttpStatus.UNAUTHORIZED, response.getStatusCode());
        verify(httpServletResponse, never()).addCookie(any(Cookie.class));
    }

    @Test
    void logout_ClearsBothTokenCookies() {
        // Act
        ResponseEntity<Void> response = authController.logout(httpServletResponse);

        // Assert
        assertEquals(HttpStatus.OK, response.getStatusCode());

        verify(httpServletResponse, times(2)).addCookie(cookieCaptor.capture());
        List<Cookie> cookies = cookieCaptor.getAllValues();

        // Verify access token cookie is cleared
        Cookie accessCookie = cookies.get(0);
        assertEquals(ACCESS_COOKIE_NAME, accessCookie.getName());
        assertEquals("", accessCookie.getValue());
        assertEquals(0, accessCookie.getMaxAge());

        // Verify refresh token cookie is cleared
        Cookie refreshCookie = cookies.get(1);
        assertEquals(REFRESH_COOKIE_NAME, refreshCookie.getName());
        assertEquals("", refreshCookie.getValue());
        assertEquals(0, refreshCookie.getMaxAge());
    }

    @Test
    void validateToken_ValidAccessToken_ReturnsLoginResponse() {
        // Arrange
        Claims mockClaims = mock(Claims.class);
        when(mockClaims.getSubject()).thenReturn("test@example.com");
        when(jwtUtils.validateJwtToken(TEST_ACCESS_TOKEN)).thenReturn(mockClaims);
        when(jwtUtils.isAccessToken(mockClaims)).thenReturn(true);
        when(loginUseCase.getUserByEmail("test@example.com")).thenReturn(Optional.of(userEntity));

        // Act
        ResponseEntity<LoginResponse> response = authController.validateToken(TEST_ACCESS_TOKEN);

        // Assert
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals(1L, response.getBody().getUserId());
        assertEquals("test@example.com", response.getBody().getEmail());
    }

    @Test
    void validateToken_NonAccessToken_ReturnsUnauthorized() {
        // Arrange
        Claims mockClaims = mock(Claims.class);
        when(jwtUtils.validateJwtToken(TEST_ACCESS_TOKEN)).thenReturn(mockClaims);
        when(jwtUtils.isAccessToken(mockClaims)).thenReturn(false);

        // Act
        ResponseEntity<LoginResponse> response = authController.validateToken(TEST_ACCESS_TOKEN);

        // Assert
        assertEquals(HttpStatus.UNAUTHORIZED, response.getStatusCode());
    }

    @Test
    void validateToken_InvalidToken_ReturnsUnauthorized() {
        // Arrange
        when(jwtUtils.validateJwtToken(TEST_ACCESS_TOKEN))
                .thenThrow(new RuntimeException("Invalid token"));

        // Act
        ResponseEntity<LoginResponse> response = authController.validateToken(TEST_ACCESS_TOKEN);

        // Assert
        assertEquals(HttpStatus.UNAUTHORIZED, response.getStatusCode());
    }
}