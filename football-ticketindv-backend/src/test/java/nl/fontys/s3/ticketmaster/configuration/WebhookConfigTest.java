package nl.fontys.s3.ticketmaster.configuration;

import org.junit.jupiter.api.Test;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.converter.StringHttpMessageConverter;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class WebhookConfigTest {

    @Test
    void configureMessageConverters_ShouldAddStringConverterAtFirstPosition() {
        // Arrange
        WebhookConfig webhookConfig = new WebhookConfig();
        List<HttpMessageConverter<?>> converters = new ArrayList<>();

        // Act
        webhookConfig.configureMessageConverters(converters);

        // Assert
        assertFalse(converters.isEmpty(), "Converters list should not be empty");
        assertEquals(1, converters.size(), "Should add exactly one converter");
        assertTrue(converters.get(0) instanceof StringHttpMessageConverter,
                "First converter should be StringHttpMessageConverter");
    }

    @Test
    void configureMessageConverters_ShouldPreserveExistingConverters() {
        // Arrange
        WebhookConfig webhookConfig = new WebhookConfig();
        List<HttpMessageConverter<?>> converters = new ArrayList<>();
        HttpMessageConverter<?> existingConverter = new MockHttpMessageConverter();
        converters.add(existingConverter);

        // Act
        webhookConfig.configureMessageConverters(converters);

        // Assert
        assertEquals(2, converters.size(), "Should have two converters");
        assertTrue(converters.get(0) instanceof StringHttpMessageConverter,
                "First converter should be StringHttpMessageConverter");
        assertEquals(existingConverter, converters.get(1),
                "Second converter should be the existing converter");
    }

    @Test
    void verifyWebMvcConfigurer_Implementation() {
        // Arrange
        WebhookConfig webhookConfig = new WebhookConfig();

        // Assert
        assertTrue(webhookConfig instanceof org.springframework.web.servlet.config.annotation.WebMvcConfigurer,
                "WebhookConfig should implement WebMvcConfigurer");
    }

    // Helper class for testing
    private static class MockHttpMessageConverter implements HttpMessageConverter<Object> {
        @Override
        public boolean canRead(Class<?> clazz, org.springframework.http.MediaType mediaType) {
            return false;
        }

        @Override
        public boolean canWrite(Class<?> clazz, org.springframework.http.MediaType mediaType) {
            return false;
        }

        @Override
        public List<org.springframework.http.MediaType> getSupportedMediaTypes() {
            return new ArrayList<>();
        }

        @Override
        public Object read(Class<?> clazz, org.springframework.http.HttpInputMessage inputMessage) {
            return null;
        }

        @Override
        public void write(Object o, org.springframework.http.MediaType contentType, org.springframework.http.HttpOutputMessage outputMessage) {
            throw new UnsupportedOperationException("Mock converter - write operations not implemented");
        }
    }
}