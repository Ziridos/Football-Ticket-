package nl.fontys.s3.ticketmaster.business.impl.emailimpl;

import jakarta.mail.internet.MimeMessage;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.context.annotation.Profile;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;


import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class EmailServiceTest {
    @Mock
    private JavaMailSender mailSender;

    private EmailService emailService;

    private static final String FROM_EMAIL = "test@ticketmaster.com";
    private static final String TO_EMAIL = "user@example.com";
    private static final String USER_NAME = "Test User";
    private static final String MATCH_DETAILS = "Home Team vs Away Team - 01-12-2024 20:00";
    private static final List<String> SEAT_NUMBERS = Arrays.asList("Seat 1", "Seat 2");
    private static final double TOTAL_PRICE = 100.0;
    private static final String TICKET_ID = "TICKET123";

    @Test
    void sendTicketConfirmation_SuccessfulSend() {
        // Arrange
        MimeMessage mimeMessage = mock(MimeMessage.class);
        when(mailSender.createMimeMessage()).thenReturn(mimeMessage);
        emailService = new EmailService(mailSender, FROM_EMAIL);

        // Act
        emailService.sendTicketConfirmation(TO_EMAIL, USER_NAME, MATCH_DETAILS,
                SEAT_NUMBERS, TOTAL_PRICE, TICKET_ID);

        // Assert
        verify(mailSender).createMimeMessage();
        verify(mailSender).send(mimeMessage);
    }

    @Test
    void sendTicketConfirmation_HandlesException() {
        // Arrange
        MimeMessage mimeMessage = mock(MimeMessage.class);
        when(mailSender.createMimeMessage()).thenReturn(mimeMessage);
        doThrow(new RuntimeException("Failed to send email"))
                .when(mailSender).send(any(MimeMessage.class));
        emailService = new EmailService(mailSender, FROM_EMAIL);

        // Act & Assert
        assertDoesNotThrow(() -> emailService.sendTicketConfirmation(
                TO_EMAIL, USER_NAME, MATCH_DETAILS, SEAT_NUMBERS, TOTAL_PRICE, TICKET_ID
        ));
    }

    @Test
    void verifyProfileAnnotation() {
        // Arrange
        Profile profileAnnotation = EmailService.class.getAnnotation(Profile.class);

        // Assert
        assertNotNull(profileAnnotation, "Class should have @Profile annotation");
        assertArrayEquals(new String[]{"!test"}, profileAnnotation.value(),
                "Profile should be set to '!test'");
    }

    @Test
    void verifyServiceAnnotation() {
        // Assert
        assertNotNull(EmailService.class.getAnnotation(Service.class),
                "Class should have @Service annotation");
    }
}