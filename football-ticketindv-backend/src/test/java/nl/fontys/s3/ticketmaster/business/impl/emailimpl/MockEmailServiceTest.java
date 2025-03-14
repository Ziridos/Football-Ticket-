package nl.fontys.s3.ticketmaster.business.impl.emailimpl;

import ch.qos.logback.classic.Level;
import ch.qos.logback.classic.Logger;
import ch.qos.logback.classic.spi.ILoggingEvent;
import ch.qos.logback.core.read.ListAppender;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class MockEmailServiceTest {
    private MockEmailService mockEmailService;
    private ListAppender<ILoggingEvent> listAppender;
    private static final String RICK_ROLL_URL = "https://www.youtube.com/watch?v=dQw4w9WgXcQ";

    private static final String TO_EMAIL = "test@example.com";
    private static final String USER_NAME = "Test User";
    private static final String MATCH_DETAILS = "Test Match";
    private static final List<String> SEAT_NUMBERS = Arrays.asList("Seat 1", "Seat 2");
    private static final double TOTAL_PRICE = 100.0;
    private static final String TICKET_ID = "TICKET123";

    @BeforeEach
    void setUp() {
        mockEmailService = new MockEmailService();

        // Setup logger to capture log messages
        Logger logger = (Logger) LoggerFactory.getLogger(MockEmailService.class);
        listAppender = new ListAppender<>();
        listAppender.start();
        logger.addAppender(listAppender);
    }

    @Test
    void sendTicketConfirmation_LogsCorrectInformation() {
        // Act
        mockEmailService.sendTicketConfirmation(TO_EMAIL, USER_NAME, MATCH_DETAILS,
                SEAT_NUMBERS, TOTAL_PRICE, TICKET_ID);

        // Assert
        List<ILoggingEvent> logsList = listAppender.list;

        // Verify all log messages are present
        assertTrue(logsList.stream()
                .anyMatch(event -> event.getFormattedMessage()
                        .contains("MOCK: Would send email to " + TO_EMAIL)));

        assertTrue(logsList.stream()
                .anyMatch(event -> event.getFormattedMessage()
                        .contains("MOCK: Email content would include match: " + MATCH_DETAILS)));

        assertTrue(logsList.stream()
                .anyMatch(event -> event.getFormattedMessage()
                        .contains("MOCK: QR code would link to: " + RICK_ROLL_URL)));

        // Verify log levels
        assertEquals(Level.INFO, logsList.get(0).getLevel());
    }

    @Test
    void verifyProfileAnnotation() {
        // Arrange
        Profile profileAnnotation = MockEmailService.class.getAnnotation(Profile.class);

        // Assert
        assertNotNull(profileAnnotation, "Class should have @Profile annotation");
        assertArrayEquals(new String[]{"test"}, profileAnnotation.value(),
                "Profile should be set to 'test'");
    }

    @Test
    void verifyServiceAnnotation() {
        // Assert
        assertNotNull(MockEmailService.class.getAnnotation(Service.class),
                "Class should have @Service annotation");
    }

    @Test
    void verifyConstantValue() {
        // Assert
        assertEquals("https://www.youtube.com/watch?v=dQw4w9WgXcQ", RICK_ROLL_URL,
                "RICK_ROLL_URL should match expected value");
    }
}