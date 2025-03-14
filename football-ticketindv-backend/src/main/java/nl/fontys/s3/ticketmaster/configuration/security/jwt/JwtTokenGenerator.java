package nl.fontys.s3.ticketmaster.configuration.security.jwt;

import nl.fontys.s3.ticketmaster.persitence.entity.UserEntity;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import java.nio.charset.StandardCharsets;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

@Component
public class JwtTokenGenerator {

    @Value("${jwt.secret}")
    private String secret;

    @Value("${jwt.access-token.expiration}")
    private Long accessTokenExpirationMs;

    @Value("${jwt.refresh-token.expiration}")
    private Long refreshTokenExpirationMs;

    public String generateAccessToken(UserEntity user) {
        return generateToken(user, accessTokenExpirationMs, "ACCESS");
    }

    public String generateRefreshToken(UserEntity user) {
        return generateToken(user, refreshTokenExpirationMs, "REFRESH");
    }

    private String generateToken(UserEntity user, Long expirationTime, String tokenType) {
        Map<String, Object> claims = new HashMap<>();
        claims.put("sub", user.getEmail());
        claims.put("created", new Date());
        claims.put("userId", user.getId());
        claims.put("role", user.getRole().name());
        claims.put("tokenType", tokenType);

        SecretKey key = Keys.hmacShaKeyFor(secret.getBytes(StandardCharsets.UTF_8));

        return Jwts.builder()
                .setClaims(claims)
                .setExpiration(new Date(System.currentTimeMillis() + expirationTime))
                .signWith(key)
                .compact();
    }
}