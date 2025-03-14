package nl.fontys.s3.ticketmaster.configuration.security.token.impl;

import nl.fontys.s3.ticketmaster.configuration.security.token.AccessToken;
import nl.fontys.s3.ticketmaster.configuration.security.token.exception.InvalidAccessTokenException;
import nl.fontys.s3.ticketmaster.domain.user.Role;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class AccessTokenEncoderDecoderImplTest {

    private AccessTokenEncoderDecoderImpl accessTokenEncoderDecoder;
    private static final String SECRET_KEY = "yourTestSecretKeyMustBeAtLeast32CharactersLong";
    private static final long ACCESS_TOKEN_EXPIRATION = 3600000L; // 1 hour
    private static final long REFRESH_TOKEN_EXPIRATION = 86400000L; // 24 hours

    @BeforeEach
    void setUp() {
        accessTokenEncoderDecoder = new AccessTokenEncoderDecoderImpl(
                SECRET_KEY,
                ACCESS_TOKEN_EXPIRATION,
                REFRESH_TOKEN_EXPIRATION
        );
    }

    @Test
    void encode_AccessToken_ValidateStructure() {
        // Arrange
        AccessToken accessToken = new AccessTokenImpl("test@example.com", 1L, Role.ADMIN);

        // Act
        String encodedToken = accessTokenEncoderDecoder.encode(accessToken);

        // Assert
        assertNotNull(encodedToken);
        assertTrue(encodedToken.length() > 0);
        String[] parts = encodedToken.split("\\.");
        assertEquals(3, parts.length, "JWT should have three parts");
    }

    @Test
    void generateRefreshToken_ValidateStructure() {
        // Arrange
        AccessToken accessToken = new AccessTokenImpl("test@example.com", 1L, Role.ADMIN);

        // Act
        String refreshToken = accessTokenEncoderDecoder.generateRefreshToken(accessToken);

        // Assert
        assertNotNull(refreshToken);
        assertTrue(refreshToken.length() > 0);
        String[] parts = refreshToken.split("\\.");
        assertEquals(3, parts.length, "JWT should have three parts");
    }

    @Test
    void decode_AccessToken_ValidateContent() {
        // Arrange
        AccessToken originalToken = new AccessTokenImpl("test@example.com", 1L, Role.ADMIN);
        String encodedToken = accessTokenEncoderDecoder.encode(originalToken);

        // Act
        AccessToken decodedToken = accessTokenEncoderDecoder.decode(encodedToken);

        // Assert
        assertNotNull(decodedToken);
        assertEquals(originalToken.getSubject(), decodedToken.getSubject());
        assertEquals(originalToken.getUserId(), decodedToken.getUserId());
        assertEquals(originalToken.getRole(), decodedToken.getRole());
    }

    @Test
    void decodeRefreshToken_ValidateContent() {
        // Arrange
        AccessToken originalToken = new AccessTokenImpl("test@example.com", 1L, Role.ADMIN);
        String refreshToken = accessTokenEncoderDecoder.generateRefreshToken(originalToken);

        // Act
        AccessToken decodedToken = accessTokenEncoderDecoder.decodeRefreshToken(refreshToken);

        // Assert
        assertNotNull(decodedToken);
        assertEquals(originalToken.getSubject(), decodedToken.getSubject());
        assertEquals(originalToken.getUserId(), decodedToken.getUserId());
        assertEquals(originalToken.getRole(), decodedToken.getRole());
    }

    @Test
    void decode_AccessToken_WithBearerPrefix() {
        // Arrange
        AccessToken originalToken = new AccessTokenImpl("test@example.com", 1L, Role.ADMIN);
        String encodedToken = accessTokenEncoderDecoder.encode(originalToken);
        String tokenWithBearer = "Bearer " + encodedToken;

        // Act
        AccessToken decodedToken = accessTokenEncoderDecoder.decode(tokenWithBearer);

        // Assert
        assertNotNull(decodedToken);
        assertEquals(originalToken.getSubject(), decodedToken.getSubject());
        assertEquals(originalToken.getUserId(), decodedToken.getUserId());
        assertEquals(originalToken.getRole(), decodedToken.getRole());
    }

    @Test
    void decode_WithInvalidToken_ShouldThrowException() {
        // Arrange
        String invalidToken = "invalid.token.format";

        // Act & Assert
        assertThrows(InvalidAccessTokenException.class, () ->
                accessTokenEncoderDecoder.decode(invalidToken)
        );
    }

    @Test
    void decode_ExpiredAccessToken_ShouldThrowException() {
        // Arrange
        AccessTokenEncoderDecoderImpl shortLivedEncoder = new AccessTokenEncoderDecoderImpl(
                SECRET_KEY, 1, REFRESH_TOKEN_EXPIRATION
        ); // 1ms access token expiration
        AccessToken originalToken = new AccessTokenImpl("test@example.com", 1L, Role.ADMIN);
        String encodedToken = shortLivedEncoder.encode(originalToken);

        // Act & Assert
        assertThrows(InvalidAccessTokenException.class, () ->
                shortLivedEncoder.decode(encodedToken)
        );
    }

    @Test
    void decodeRefreshToken_ExpiredToken_ShouldThrowException() {
        // Arrange
        AccessTokenEncoderDecoderImpl shortLivedEncoder = new AccessTokenEncoderDecoderImpl(
                SECRET_KEY, ACCESS_TOKEN_EXPIRATION, 1
        ); // 1ms refresh token expiration
        AccessToken originalToken = new AccessTokenImpl("test@example.com", 1L, Role.ADMIN);
        String refreshToken = shortLivedEncoder.generateRefreshToken(originalToken);

        // Act & Assert
        assertThrows(InvalidAccessTokenException.class, () ->
                shortLivedEncoder.decodeRefreshToken(refreshToken)
        );
    }

    @Test
    void decode_RefreshTokenAsAccessToken_ShouldThrowException() {
        // Arrange
        AccessToken originalToken = new AccessTokenImpl("test@example.com", 1L, Role.ADMIN);
        String refreshToken = accessTokenEncoderDecoder.generateRefreshToken(originalToken);

        // Act & Assert
        assertThrows(InvalidAccessTokenException.class, () ->
                accessTokenEncoderDecoder.decode(refreshToken)
        );
    }

    @Test
    void decodeRefreshToken_AccessTokenAsRefreshToken_ShouldThrowException() {
        // Arrange
        AccessToken originalToken = new AccessTokenImpl("test@example.com", 1L, Role.ADMIN);
        String accessToken = accessTokenEncoderDecoder.encode(originalToken);

        // Act & Assert
        assertThrows(InvalidAccessTokenException.class, () ->
                accessTokenEncoderDecoder.decodeRefreshToken(accessToken)
        );
    }

    @Test
    void decode_WithNullToken_ShouldThrowException() {
        // Act & Assert
        assertThrows(InvalidAccessTokenException.class, () ->
                accessTokenEncoderDecoder.decode(null)
        );
    }

    @Test
    void decode_WithEmptyToken_ShouldThrowException() {
        // Act & Assert
        assertThrows(InvalidAccessTokenException.class, () ->
                accessTokenEncoderDecoder.decode("")
        );
    }

    @Test
    void decodeRefreshToken_WithNullToken_ShouldThrowException() {
        // Act & Assert
        assertThrows(InvalidAccessTokenException.class, () ->
                accessTokenEncoderDecoder.decodeRefreshToken(null)
        );
    }

    @Test
    void decodeRefreshToken_WithEmptyToken_ShouldThrowException() {
        // Act & Assert
        assertThrows(InvalidAccessTokenException.class, () ->
                accessTokenEncoderDecoder.decodeRefreshToken("")
        );
    }

    @Test
    void tokenConsistency_MultipleEncodeDecode() {
        // Arrange
        AccessToken originalToken = new AccessTokenImpl("test@example.com", 1L, Role.ADMIN);

        // Act & Assert - Access Token
        String accessToken = accessTokenEncoderDecoder.encode(originalToken);
        AccessToken decodedAccess = accessTokenEncoderDecoder.decode(accessToken);
        assertEquals(originalToken.getSubject(), decodedAccess.getSubject());
        assertEquals(originalToken.getUserId(), decodedAccess.getUserId());
        assertEquals(originalToken.getRole(), decodedAccess.getRole());

        // Act & Assert - Refresh Token
        String refreshToken = accessTokenEncoderDecoder.generateRefreshToken(originalToken);
        AccessToken decodedRefresh = accessTokenEncoderDecoder.decodeRefreshToken(refreshToken);
        assertEquals(originalToken.getSubject(), decodedRefresh.getSubject());
        assertEquals(originalToken.getUserId(), decodedRefresh.getUserId());
        assertEquals(originalToken.getRole(), decodedRefresh.getRole());
    }
}