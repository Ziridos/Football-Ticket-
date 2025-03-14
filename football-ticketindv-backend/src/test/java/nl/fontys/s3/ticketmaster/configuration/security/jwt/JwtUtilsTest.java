package nl.fontys.s3.ticketmaster.configuration.security.jwt;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtException;
import nl.fontys.s3.ticketmaster.domain.user.Role;
import nl.fontys.s3.ticketmaster.persitence.entity.UserEntity;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;



import static org.junit.jupiter.api.Assertions.*;

class JwtUtilsTest {

    private JwtUtils jwtUtils;
    private UserEntity testUser;
    private static final String JWT_SECRET = "yourTestSecretKeyMustBeAtLeast32CharactersLong";
    private static final long ACCESS_TOKEN_EXPIRATION = 3600000L; // 1 hour
    private static final long REFRESH_TOKEN_EXPIRATION = 86400000L; // 24 hours

    @BeforeEach
    void setUp() {
        jwtUtils = new JwtUtils(JWT_SECRET, ACCESS_TOKEN_EXPIRATION, REFRESH_TOKEN_EXPIRATION);

        testUser = UserEntity.builder()
                .id(1L)
                .email("test@example.com")
                .role(Role.ADMIN)
                .build();
    }

    @Test
    void generateAccessToken_ValidateTokenStructureAndClaims() {
        // Act
        String token = jwtUtils.generateAccessToken(testUser);

        // Assert
        assertNotNull(token);
        String[] parts = token.split("\\.");
        assertEquals(3, parts.length, "JWT should have three parts");

        Claims claims = jwtUtils.validateJwtToken(token);
        assertEquals(testUser.getEmail(), claims.getSubject());
        assertEquals(testUser.getId(), claims.get("userId", Long.class));
        assertEquals(testUser.getRole().name(), claims.get("role", String.class));
        assertEquals("ACCESS", claims.get("tokenType"));
        assertTrue(jwtUtils.isAccessToken(claims));
        assertFalse(jwtUtils.isRefreshToken(claims));
    }

    @Test
    void generateRefreshToken_ValidateTokenStructureAndClaims() {
        // Act
        String token = jwtUtils.generateRefreshToken(testUser);

        // Assert
        assertNotNull(token);
        String[] parts = token.split("\\.");
        assertEquals(3, parts.length, "JWT should have three parts");

        Claims claims = jwtUtils.validateJwtToken(token);
        assertEquals(testUser.getEmail(), claims.getSubject());
        assertEquals(testUser.getId(), claims.get("userId", Long.class));
        assertEquals("REFRESH", claims.get("tokenType"));
        assertFalse(jwtUtils.isAccessToken(claims));
        assertTrue(jwtUtils.isRefreshToken(claims));
    }

    @Test
    void generateAccessToken_VerifyTimestamps() {
        // Arrange
        long beforeTokenGeneration = System.currentTimeMillis();

        // Act
        String token = jwtUtils.generateAccessToken(testUser);

        // Assert
        Claims claims = jwtUtils.validateJwtToken(token);
        long afterTokenGeneration = System.currentTimeMillis();

        long issuedAtTime = claims.getIssuedAt().getTime();
        assertTrue(issuedAtTime >= beforeTokenGeneration - 1000 &&
                        issuedAtTime <= afterTokenGeneration + 1000,
                "IssuedAt time should be within 1 second of token generation");

        long expectedExpiration = issuedAtTime + ACCESS_TOKEN_EXPIRATION;
        long actualExpiration = claims.getExpiration().getTime();
        assertTrue(Math.abs(expectedExpiration - actualExpiration) < 1000,
                "Expiration time should be within 1 second of expected time");
    }

    @Test
    void generateRefreshToken_VerifyTimestamps() {
        // Arrange
        long beforeTokenGeneration = System.currentTimeMillis();

        // Act
        String token = jwtUtils.generateRefreshToken(testUser);

        // Assert
        Claims claims = jwtUtils.validateJwtToken(token);
        long afterTokenGeneration = System.currentTimeMillis();

        long issuedAtTime = claims.getIssuedAt().getTime();
        assertTrue(issuedAtTime >= beforeTokenGeneration - 1000 &&
                        issuedAtTime <= afterTokenGeneration + 1000,
                "IssuedAt time should be within 1 second of token generation");

        long expectedExpiration = issuedAtTime + REFRESH_TOKEN_EXPIRATION;
        long actualExpiration = claims.getExpiration().getTime();
        assertTrue(Math.abs(expectedExpiration - actualExpiration) < 1000,
                "Expiration time should be within 1 second of expected time");
    }

    @Test
    void validateJwtToken_ExpiredToken_ThrowsJwtException() {
        // Arrange
        JwtUtils shortLivedJwtUtils = new JwtUtils(JWT_SECRET, 0, REFRESH_TOKEN_EXPIRATION); // 0ms access token expiration
        String token = shortLivedJwtUtils.generateAccessToken(testUser);

        // Act & Assert
        JwtException exception = assertThrows(JwtException.class,
                () -> shortLivedJwtUtils.validateJwtToken(token));
        assertEquals("Invalid JWT token", exception.getMessage());
    }


    @Test
    void validateJwtToken_MalformedToken_ThrowsJwtException() {
        // Arrange
        String malformedToken = "not.a.jwt.token";

        // Act & Assert
        JwtException exception = assertThrows(JwtException.class,
                () -> jwtUtils.validateJwtToken(malformedToken));
        assertEquals("Invalid JWT token", exception.getMessage());
    }

    @Test
    void validateJwtToken_TokenFromDifferentSecret_ThrowsJwtException() {
        // Arrange
        JwtUtils differentJwtUtils = new JwtUtils(
                "DifferentSecretKeyThatIsAtLeast32Chars!!",
                ACCESS_TOKEN_EXPIRATION,
                REFRESH_TOKEN_EXPIRATION
        );
        String token = differentJwtUtils.generateAccessToken(testUser);

        // Act & Assert
        JwtException exception = assertThrows(JwtException.class,
                () -> jwtUtils.validateJwtToken(token));
        assertEquals("Invalid JWT token", exception.getMessage());
    }

    @Test
    void tokenTypeValidation_WithNonMatchingTypes_ReturnsFalse() {
        // Arrange
        String accessToken = jwtUtils.generateAccessToken(testUser);
        String refreshToken = jwtUtils.generateRefreshToken(testUser);
        Claims accessClaims = jwtUtils.validateJwtToken(accessToken);
        Claims refreshClaims = jwtUtils.validateJwtToken(refreshToken);

        // Assert
        assertFalse(jwtUtils.isRefreshToken(accessClaims), "Access token should not be identified as refresh token");
        assertFalse(jwtUtils.isAccessToken(refreshClaims), "Refresh token should not be identified as access token");
    }
}