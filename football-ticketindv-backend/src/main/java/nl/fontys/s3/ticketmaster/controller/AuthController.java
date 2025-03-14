package nl.fontys.s3.ticketmaster.controller;

import io.jsonwebtoken.Claims;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import nl.fontys.s3.ticketmaster.business.interfaces.logininterfaces.LoginUseCase;
import nl.fontys.s3.ticketmaster.configuration.security.jwt.JwtUtils;
import nl.fontys.s3.ticketmaster.domain.login.LoginRequest;
import nl.fontys.s3.ticketmaster.domain.login.LoginResponse;
import nl.fontys.s3.ticketmaster.persitence.entity.UserEntity;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@RestController
@RequestMapping("/tokens")
@CrossOrigin(origins = "http://localhost:5173", allowCredentials = "true")
@RequiredArgsConstructor
public class AuthController {
    private static final String COOKIE_SAME_SITE = "SameSite";
    private static final String COOKIE_SAME_SITE_VALUE = "Lax";
    private static final String ROOT_PATH = "/";

    private static final int ACCESS_TOKEN_EXPIRY = 3600; // 1 hour in seconds
    private static final int REFRESH_TOKEN_EXPIRY = 604800; // 7 days in seconds

    private final LoginUseCase loginUseCase;
    private final JwtUtils jwtUtils;

    @Value("${jwt.cookie.name}")
    private String accessTokenCookieName;

    @Value("${jwt.refresh.cookie.name}")
    private String refreshTokenCookieName;

    @Value("${jwt.refresh.token.path}")
    private String refreshTokenPath;

    private Cookie createSecureCookie(String name, String value, String path, int maxAge) {
        Cookie cookie = new Cookie(name, value);
        cookie.setHttpOnly(true);
        cookie.setSecure(false);
        cookie.setPath(path);
        cookie.setMaxAge(maxAge);
        cookie.setAttribute(COOKIE_SAME_SITE, COOKIE_SAME_SITE_VALUE);
        return cookie;
    }

    @PostMapping
    public ResponseEntity<LoginResponse> login(@RequestBody @Valid LoginRequest loginRequest, HttpServletResponse response) {
        Optional<UserEntity> authenticatedUserOptional = loginUseCase.authenticateUser(loginRequest.getEmail(), loginRequest.getPassword());
        if (authenticatedUserOptional.isPresent()) {
            UserEntity authenticatedUser = authenticatedUserOptional.get();

            String accessToken = jwtUtils.generateAccessToken(authenticatedUser);
            String refreshToken = jwtUtils.generateRefreshToken(authenticatedUser);

            response.addCookie(createSecureCookie(accessTokenCookieName, accessToken, ROOT_PATH, ACCESS_TOKEN_EXPIRY));
            response.addCookie(createSecureCookie(refreshTokenCookieName, refreshToken, refreshTokenPath, REFRESH_TOKEN_EXPIRY));

            return ResponseEntity.ok(LoginResponse.builder()
                    .userId(authenticatedUser.getId())
                    .email(authenticatedUser.getEmail())
                    .role(authenticatedUser.getRole())
                    .build());
        }
        return ResponseEntity.badRequest().build();
    }

    @PostMapping("/refresh")
    public ResponseEntity<LoginResponse> refreshToken(
            @CookieValue(name = "${jwt.refresh.cookie.name}", required = false) String refreshToken,
            HttpServletResponse response) {



        if (refreshToken == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }

        try {
            Claims claims = jwtUtils.validateJwtToken(refreshToken);

            if (!jwtUtils.isRefreshToken(claims)) {
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
            }

            String email = claims.getSubject();
            Optional<UserEntity> userOptional = loginUseCase.getUserByEmail(email);

            if (userOptional.isPresent()) {
                UserEntity user = userOptional.get();

                String newAccessToken = jwtUtils.generateAccessToken(user);
                String newRefreshToken = jwtUtils.generateRefreshToken(user);

                response.addCookie(createSecureCookie(accessTokenCookieName, newAccessToken, ROOT_PATH, ACCESS_TOKEN_EXPIRY));
                response.addCookie(createSecureCookie(refreshTokenCookieName, newRefreshToken, refreshTokenPath, REFRESH_TOKEN_EXPIRY));

                return ResponseEntity.ok(LoginResponse.builder()
                        .userId(user.getId())
                        .email(user.getEmail())
                        .role(user.getRole())
                        .build());
            }
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }

        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
    }

    @PostMapping("/logout")
    public ResponseEntity<Void> logout(HttpServletResponse response) {
        response.addCookie(createSecureCookie(accessTokenCookieName, "", ROOT_PATH, 0));
        response.addCookie(createSecureCookie(refreshTokenCookieName, "", refreshTokenPath, 0));
        return ResponseEntity.ok().build();
    }

    @GetMapping("/validate")
    public ResponseEntity<LoginResponse> validateToken(
            @CookieValue(name = "${jwt.cookie.name}", required = false) String token) {
        if (token == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }

        try {
            Claims claims = jwtUtils.validateJwtToken(token);

            if (!jwtUtils.isAccessToken(claims)) {
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
            }

            String email = claims.getSubject();
            Optional<UserEntity> userOptional = loginUseCase.getUserByEmail(email);

            if (userOptional.isPresent()) {
                UserEntity user = userOptional.get();
                return ResponseEntity.ok(LoginResponse.builder()
                        .userId(user.getId())
                        .email(user.getEmail())
                        .role(user.getRole())
                        .build());
            }
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }

        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
    }
}