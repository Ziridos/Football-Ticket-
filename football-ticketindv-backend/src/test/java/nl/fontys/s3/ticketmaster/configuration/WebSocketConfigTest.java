package nl.fontys.s3.ticketmaster.configuration;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.messaging.simp.config.MessageBrokerRegistry;
import org.springframework.web.socket.config.annotation.StompEndpointRegistry;
import org.springframework.web.socket.config.annotation.StompWebSocketEndpointRegistration;

import static org.hibernate.validator.internal.util.Contracts.assertTrue;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class WebSocketConfigTest {
    @Mock
    private MessageBrokerRegistry messageBrokerRegistry;

    @Mock
    private StompEndpointRegistry stompEndpointRegistry;

    @Mock
    private StompWebSocketEndpointRegistration endpointRegistration;

    @InjectMocks
    private WebSocketConfig webSocketConfig;

    private static final String ALLOWED_ORIGIN = "http://localhost:5173";

    @Test
    void configureMessageBroker_ShouldSetupBrokerAndApplicationPrefixes() {
        // Act
        webSocketConfig.configureMessageBroker(messageBrokerRegistry);

        // Assert
        verify(messageBrokerRegistry).enableSimpleBroker("/topic");
        verify(messageBrokerRegistry).setApplicationDestinationPrefixes("/app");
    }

    @Test
    void registerStompEndpoints_ShouldRegisterEndpointsWithAndWithoutSockJS() {
        // Arrange
        when(stompEndpointRegistry.addEndpoint("/ws")).thenReturn(endpointRegistration);
        when(endpointRegistration.setAllowedOrigins(ALLOWED_ORIGIN)).thenReturn(endpointRegistration);

        // Act
        webSocketConfig.registerStompEndpoints(stompEndpointRegistry);

        // Assert
        // Verify endpoint registration happened twice
        verify(stompEndpointRegistry, times(2)).addEndpoint("/ws");
        verify(endpointRegistration, times(2)).setAllowedOrigins(ALLOWED_ORIGIN);
        verify(endpointRegistration).withSockJS();
    }

    @Test
    void verifyWebSocketMessageBrokerConfigurer_Implementation() {
        // Assert
        assertTrue(webSocketConfig instanceof org.springframework.web.socket.config.annotation.WebSocketMessageBrokerConfigurer,
                "WebSocketConfig should implement WebSocketMessageBrokerConfigurer");
    }
}