package nl.fontys.s3.ticketmaster.business.impl.emailimpl;

import lombok.extern.slf4j.Slf4j;
import nl.fontys.s3.ticketmaster.business.interfaces.emailinterfaces.EmailServiceI;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@Profile("test")
@Slf4j
public class MockEmailService implements EmailServiceI {
    private static final String RICK_ROLL_URL = "https://www.youtube.com/watch?v=dQw4w9WgXcQ";

    @Override
    public void sendTicketConfirmation(String to, String userName, String matchDetails,
                                       List<String> seatNumbers, double totalPrice, String ticketId) {
        log.info("MOCK: Would send email to {} for ticket {}", to, ticketId);
        log.info("MOCK: Email content would include match: {}, seats: {}, price: €{}",
                matchDetails, seatNumbers, totalPrice);
        log.info("MOCK: QR code would link to: {}", RICK_ROLL_URL);
    }
}