package nl.fontys.s3.ticketmaster.configuration.security;

import org.junit.jupiter.api.Test;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

import static org.junit.jupiter.api.Assertions.*;

class PasswordEncoderConfigTest {

    @Test
    void createBCryptPasswordEncoder() {
        // Arrange
        PasswordEncoderConfig config = new PasswordEncoderConfig();

        // Act
        PasswordEncoder encoder = config.createBCryptPasswordEncoder();

        // Assert
        assertNotNull(encoder);
        assertTrue(encoder instanceof BCryptPasswordEncoder);

        String password = "testPassword";
        String encodedPassword = encoder.encode(password);
        assertTrue(encoder.matches(password, encodedPassword));
    }
}