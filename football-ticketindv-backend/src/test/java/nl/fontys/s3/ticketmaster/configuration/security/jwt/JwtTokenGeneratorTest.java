package nl.fontys.s3.ticketmaster.configuration.security.jwt;

import nl.fontys.s3.ticketmaster.domain.user.Role;
import nl.fontys.s3.ticketmaster.persitence.entity.UserEntity;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.util.ReflectionTestUtils;

import javax.crypto.SecretKey;
import java.nio.charset.StandardCharsets;
import java.util.Date;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
class JwtTokenGeneratorTest {

    @InjectMocks
    private JwtTokenGenerator jwtTokenGenerator;

    private static final String SECRET = "testSecretKeyWithAtLeast32CharactersForHS256";
    private static final Long ACCESS_TOKEN_EXPIRATION = 3600000L; // 1 hour
    private static final Long REFRESH_TOKEN_EXPIRATION = 86400000L; // 24 hours
    private UserEntity testUser;
    private SecretKey key;

    @BeforeEach
    void setUp() {
        ReflectionTestUtils.setField(jwtTokenGenerator, "secret", SECRET);
        ReflectionTestUtils.setField(jwtTokenGenerator, "accessTokenExpirationMs", ACCESS_TOKEN_EXPIRATION);
        ReflectionTestUtils.setField(jwtTokenGenerator, "refreshTokenExpirationMs", REFRESH_TOKEN_EXPIRATION);

        testUser = UserEntity.builder()
                .id(1L)
                .email("test@example.com")
                .role(Role.ADMIN)
                .build();

        key = Keys.hmacShaKeyFor(SECRET.getBytes(StandardCharsets.UTF_8));
    }

    @Test
    void generateAccessToken_ValidateTokenFormat() {
        // Act
        String token = jwtTokenGenerator.generateAccessToken(testUser);

        // Assert
        String[] parts = token.split("\\.");
        assertEquals(3, parts.length, "Token should have three parts");

        for (String part : parts) {
            assertFalse(part.isEmpty(), "Token parts should not be empty");
        }
    }

    @Test
    void generateRefreshToken_ValidateTokenFormat() {
        // Act
        String token = jwtTokenGenerator.generateRefreshToken(testUser);

        // Assert
        String[] parts = token.split("\\.");
        assertEquals(3, parts.length, "Token should have three parts");

        for (String part : parts) {
            assertFalse(part.isEmpty(), "Token parts should not be empty");
        }
    }

    @Test
    void generateAccessToken_ValidateTokenClaims() {
        // Act
        String token = jwtTokenGenerator.generateAccessToken(testUser);
        Claims claims = Jwts.parserBuilder()
                .setSigningKey(key)
                .build()
                .parseClaimsJws(token)
                .getBody();

        // Assert
        assertEquals(testUser.getEmail(), claims.get("sub"));
        assertEquals(testUser.getId(), ((Integer) claims.get("userId")).longValue());  // Convert Integer to Long
        assertEquals(testUser.getRole().name(), claims.get("role"));
        assertEquals("ACCESS", claims.get("tokenType"));
        assertNotNull(claims.get("created"));
        assertTrue(claims.getExpiration().after(new Date()));
    }

    @Test
    void generateRefreshToken_ValidateTokenClaims() {
        // Act
        String token = jwtTokenGenerator.generateRefreshToken(testUser);
        Claims claims = Jwts.parserBuilder()
                .setSigningKey(key)
                .build()
                .parseClaimsJws(token)
                .getBody();

        // Assert
        assertEquals(testUser.getEmail(), claims.get("sub"));
        assertEquals(testUser.getId(), ((Integer) claims.get("userId")).longValue());  // Convert Integer to Long
        assertEquals(testUser.getRole().name(), claims.get("role"));
        assertEquals("REFRESH", claims.get("tokenType"));
        assertNotNull(claims.get("created"));
        assertTrue(claims.getExpiration().after(new Date()));
    }

    @Test
    void validateAccessTokenExpiration() {
        // Act
        String token = jwtTokenGenerator.generateAccessToken(testUser);
        Claims claims = Jwts.parserBuilder()
                .setSigningKey(key)
                .build()
                .parseClaimsJws(token)
                .getBody();

        // Assert
        Date expiration = claims.getExpiration();
        long expirationTime = expiration.getTime() - System.currentTimeMillis();
        assertTrue(expirationTime <= ACCESS_TOKEN_EXPIRATION);
        assertTrue(expirationTime > 0);
    }

    @Test
    void validateRefreshTokenExpiration() {
        // Act
        String token = jwtTokenGenerator.generateRefreshToken(testUser);
        Claims claims = Jwts.parserBuilder()
                .setSigningKey(key)
                .build()
                .parseClaimsJws(token)
                .getBody();

        // Assert
        Date expiration = claims.getExpiration();
        long expirationTime = expiration.getTime() - System.currentTimeMillis();
        assertTrue(expirationTime <= REFRESH_TOKEN_EXPIRATION);
        assertTrue(expirationTime > 0);
    }
}