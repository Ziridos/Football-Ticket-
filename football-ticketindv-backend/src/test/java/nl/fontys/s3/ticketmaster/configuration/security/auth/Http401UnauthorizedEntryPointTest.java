package nl.fontys.s3.ticketmaster.configuration.security.auth;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.core.AuthenticationException;

import java.io.IOException;

import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
class Http401UnauthorizedEntryPointTest {

    @Mock
    private HttpServletRequest request;

    @Mock
    private HttpServletResponse response;

    @Mock
    private AuthenticationException authException;

    @InjectMocks
    private Http401UnauthorizedEntryPoint entryPoint;

    @Test
    void commence() throws IOException {
        // Act
        entryPoint.commence(request, response, authException);

        // Assert
        verify(response).sendError(HttpServletResponse.SC_UNAUTHORIZED);
    }
}