package nl.fontys.s3.ticketmaster.configuration.security.token.impl;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.MalformedJwtException;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.UnsupportedJwtException;
import io.jsonwebtoken.security.Keys;
import nl.fontys.s3.ticketmaster.domain.user.Role;
import org.springframework.beans.factory.annotation.Value;
import nl.fontys.s3.ticketmaster.configuration.security.token.AccessToken;
import nl.fontys.s3.ticketmaster.configuration.security.token.AccessTokenDecoder;
import nl.fontys.s3.ticketmaster.configuration.security.token.AccessTokenEncoder;
import nl.fontys.s3.ticketmaster.configuration.security.token.exception.InvalidAccessTokenException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import javax.crypto.SecretKey;
import java.nio.charset.StandardCharsets;
import java.util.Date;

@Service
public class AccessTokenEncoderDecoderImpl implements AccessTokenEncoder, AccessTokenDecoder {

    private static final Logger logger = LoggerFactory.getLogger(AccessTokenEncoderDecoderImpl.class);

    private static final String CLAIM_USER_ID = "userId";
    private static final String CLAIM_ROLE = "role";
    private static final String CLAIM_TOKEN_TYPE = "tokenType";

    private static final String TOKEN_TYPE_ACCESS = "ACCESS";
    private static final String TOKEN_TYPE_REFRESH = "REFRESH";

    private static final String BEARER_PREFIX = "Bearer ";

    private final SecretKey key;
    private final long accessTokenExpirationMs;
    private final long refreshTokenExpirationMs;

    public AccessTokenEncoderDecoderImpl(@Value("${jwt.secret}") String secretKey,
                                         @Value("${jwt.access-token.expiration}") long accessTokenExpirationMs,
                                         @Value("${jwt.refresh-token.expiration}") long refreshTokenExpirationMs) {
        this.key = Keys.hmacShaKeyFor(secretKey.getBytes(StandardCharsets.UTF_8));
        this.accessTokenExpirationMs = accessTokenExpirationMs;
        this.refreshTokenExpirationMs = refreshTokenExpirationMs;
    }

    @Override
    public String encode(AccessToken accessToken) {
        return generateToken(accessToken, accessTokenExpirationMs, TOKEN_TYPE_ACCESS);
    }

    public String generateRefreshToken(AccessToken accessToken) {
        return generateToken(accessToken, refreshTokenExpirationMs, TOKEN_TYPE_REFRESH);
    }

    private String generateToken(AccessToken accessToken, long expirationMs, String tokenType) {
        Date now = new Date();
        Date expiryDate = new Date(now.getTime() + expirationMs);

        String token = Jwts.builder()
                .setSubject(accessToken.getSubject())
                .claim(CLAIM_USER_ID, accessToken.getUserId())
                .claim(CLAIM_ROLE, accessToken.getRole().name())
                .claim(CLAIM_TOKEN_TYPE, tokenType)
                .setIssuedAt(now)
                .setExpiration(expiryDate)
                .signWith(key, SignatureAlgorithm.HS256)
                .compact();

        logger.debug("Encoded {} token: {}", tokenType, token);
        return token;
    }

    @Override
    public AccessToken decode(String accessTokenEncoded) {
        try {
            logger.debug("Attempting to decode token: {}", accessTokenEncoded);

            if (accessTokenEncoded.startsWith(BEARER_PREFIX)) {
                accessTokenEncoded = accessTokenEncoded.substring(BEARER_PREFIX.length());
            }

            Claims claims = Jwts.parserBuilder()
                    .setSigningKey(key)
                    .build()
                    .parseClaimsJws(accessTokenEncoded)
                    .getBody();

            String tokenType = claims.get(CLAIM_TOKEN_TYPE, String.class);
            if (!TOKEN_TYPE_ACCESS.equals(tokenType)) {
                logger.error("Invalid token type: {}", tokenType);
                throw new InvalidAccessTokenException("Invalid token type");
            }

            String subject = claims.getSubject();
            Long userId = claims.get(CLAIM_USER_ID, Long.class);
            Role role = Role.valueOf(claims.get(CLAIM_ROLE, String.class));

            AccessToken decodedToken = new AccessTokenImpl(subject, userId, role);
            logger.debug("Successfully decoded token for user: {}", decodedToken.getSubject());
            return decodedToken;

        } catch (ExpiredJwtException e) {
            logger.error("Token has expired: {}", e.getMessage());
            throw new InvalidAccessTokenException("The access token has expired");
        } catch (MalformedJwtException e) {
            logger.error("Malformed token: {}", e.getMessage());
            throw new InvalidAccessTokenException("Malformed access token");
        } catch (UnsupportedJwtException e) {
            logger.error("Unsupported token format: {}", e.getMessage());
            throw new InvalidAccessTokenException("Unsupported token format");
        } catch (IllegalArgumentException e) {
            logger.error("Invalid role in token: {}", e.getMessage());
            throw new InvalidAccessTokenException("Invalid role in access token");
        } catch (JwtException e) {
            logger.error("JWT token processing error: {}", e.getMessage());
            throw new InvalidAccessTokenException("Error processing access token");
        } catch (Exception e) {
            logger.error("Unexpected error while decoding token: {}", e.getMessage(), e);
            throw new InvalidAccessTokenException("Unexpected error while processing access token");
        }
    }

    public AccessToken decodeRefreshToken(String refreshTokenEncoded) {
        try {
            logger.debug("Attempting to decode refresh token: {}", refreshTokenEncoded);

            Claims claims = Jwts.parserBuilder()
                    .setSigningKey(key)
                    .build()
                    .parseClaimsJws(refreshTokenEncoded)
                    .getBody();

            String tokenType = claims.get(CLAIM_TOKEN_TYPE, String.class);
            if (!TOKEN_TYPE_REFRESH.equals(tokenType)) {
                logger.error("Invalid token type: {}", tokenType);
                throw new InvalidAccessTokenException("Invalid token type");
            }

            String subject = claims.getSubject();
            Long userId = claims.get(CLAIM_USER_ID, Long.class);
            Role role = Role.valueOf(claims.get(CLAIM_ROLE, String.class));

            AccessToken decodedToken = new AccessTokenImpl(subject, userId, role);
            logger.debug("Successfully decoded refresh token for user: {}", decodedToken.getSubject());
            return decodedToken;

        } catch (ExpiredJwtException e) {
            logger.error("Refresh token has expired: {}", e.getMessage());
            throw new InvalidAccessTokenException("The refresh token has expired");
        } catch (JwtException e) {
            logger.error("Refresh token processing error: {}", e.getMessage());
            throw new InvalidAccessTokenException("Error processing refresh token");
        } catch (Exception e) {
            logger.error("Unexpected error while decoding refresh token: {}", e.getMessage(), e);
            throw new InvalidAccessTokenException("Unexpected error while processing refresh token");
        }
    }
}