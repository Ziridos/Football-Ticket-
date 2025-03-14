package nl.fontys.s3.ticketmaster.configuration.security;

import nl.fontys.s3.ticketmaster.business.interfaces.userinterfaces.GetUserUseCase;
import nl.fontys.s3.ticketmaster.configuration.security.auth.AuthenticationRequestFilter;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.security.access.expression.method.MethodSecurityExpressionHandler;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.mock;

@ExtendWith(MockitoExtension.class)
class WebSecurityConfigTest {

    @Mock
    private GetUserUseCase getUserUseCase;

    @Mock
    private HttpSecurity httpSecurity;

    @Mock
    private AuthenticationEntryPoint authenticationEntryPoint;

    @Mock
    private AuthenticationRequestFilter authenticationRequestFilter;

    private WebSecurityConfig webSecurityConfig;

    @BeforeEach
    void setUp() {
        webSecurityConfig = new WebSecurityConfig(getUserUseCase);
    }

    @Test
    void filterChain_ShouldConfigureSecurityFilters() throws Exception {
        // Arrange
        HttpSecurity mockHttpSecurity = mock(HttpSecurity.class, invocation -> {
            if (invocation.getMethod().getName().equals("build")) {
                return mock(SecurityFilterChain.class);
            }
            return invocation.getMock();
        });

        // Act
        SecurityFilterChain filterChain = webSecurityConfig.filterChain(
                mockHttpSecurity,
                authenticationEntryPoint,
                authenticationRequestFilter
        );

        // Assert
        assertNotNull(filterChain, "Filter chain should not be null");
    }

    @Test
    void corsConfigurationSource_ShouldReturnProperConfiguration() {
        // Arrange
        MockHttpServletRequest request = new MockHttpServletRequest();
        request.setRequestURI("/api/test");

        // Act
        CorsConfigurationSource corsConfigurationSource = webSecurityConfig.corsConfigurationSource();
        CorsConfiguration corsConfiguration = corsConfigurationSource.getCorsConfiguration(request);

        // Assert
        assertNotNull(corsConfiguration, "CORS configuration should not be null");
        assertTrue(corsConfiguration.getAllowedOrigins().contains("http://localhost:5173"),
                "Should allow localhost:5173");
        assertTrue(corsConfiguration.getAllowedMethods().containsAll(java.util.Arrays.asList("GET", "POST", "PUT", "DELETE", "OPTIONS", "PATCH")),
                "Should allow specified HTTP methods");
        assertTrue(corsConfiguration.getAllowedHeaders().containsAll(java.util.Arrays.asList("Authorization", "Content-Type", "X-Requested-With")),
                "Should allow specified headers");
        assertTrue(corsConfiguration.getExposedHeaders().contains("Authorization"),
                "Should expose Authorization header");
        assertTrue(corsConfiguration.getAllowCredentials(),
                "Should allow credentials");
    }

    @Test
    void methodSecurityExpressionHandler_ShouldReturnHandlerWithCustomPermissionEvaluator() {
        // Act
        MethodSecurityExpressionHandler handler = webSecurityConfig.methodSecurityExpressionHandler();

        // Assert
        assertNotNull(handler, "Method security expression handler should not be null");
        assertTrue(handler instanceof org.springframework.security.access.expression.method.DefaultMethodSecurityExpressionHandler,
                "Should return DefaultMethodSecurityExpressionHandler");
    }
}