package nl.fontys.s3.ticketmaster.business.impl.emailimpl;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.client.j2se.MatrixToImageWriter;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.qrcode.QRCodeWriter;
import jakarta.mail.internet.MimeMessage;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Profile;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;
import nl.fontys.s3.ticketmaster.business.interfaces.emailinterfaces.EmailServiceI;

import java.io.ByteArrayOutputStream;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Base64;

@Service
@Slf4j
@Profile("!test")
public class EmailService implements EmailServiceI {

    private final JavaMailSender mailSender;
    private final String fromEmail;
    private static final String RICK_ROLL_URL = "https://www.youtube.com/watch?v=dQw4w9WgXcQ";
    private static final String CLOSING_DIV = "</div>";

    public EmailService(JavaMailSender mailSender,
                        @Value("${spring.mail.username}") String fromEmail) {
        this.mailSender = mailSender;
        this.fromEmail = fromEmail;
    }

    @Override
    public void sendTicketConfirmation(String to, String userName, String matchDetails,
                                       List<String> seatNumbers, double totalPrice, String ticketId) {
        try {
            MimeMessage message = mailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(message, true, "UTF-8");

            String qrCodeImage = generateQrCodeBase64(RICK_ROLL_URL);

            helper.setFrom(fromEmail);
            helper.setTo(to);
            helper.setSubject("Your Ticket Confirmation");
            helper.setText(generateEmailContent(userName, matchDetails, seatNumbers, totalPrice, ticketId, qrCodeImage), true);

            mailSender.send(message);
            log.info("Sent ticket confirmation email to: {}", to);
        } catch (Exception e) {
            log.error("Failed to send ticket confirmation email: {}", e.getMessage(), e);
        }
    }

    private String generateQrCodeBase64(String content) {
        try {
            QRCodeWriter qrCodeWriter = new QRCodeWriter();
            BitMatrix bitMatrix = qrCodeWriter.encode(content, BarcodeFormat.QR_CODE, 200, 200);

            ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
            MatrixToImageWriter.writeToStream(bitMatrix, "PNG", outputStream);

            return "data:image/png;base64," + Base64.getEncoder().encodeToString(outputStream.toByteArray());
        } catch (Exception e) {
            log.error("Failed to generate QR code: {}", e.getMessage(), e);
            return "";
        }
    }

    private String generateEmailContent(String userName, String matchDetails,
                                        List<String> seatNumbers, double totalPrice, String ticketId, String qrCodeBase64) {
        StringBuilder html = new StringBuilder();
        html.append("<div style='font-family: Arial, sans-serif; max-width: 600px; margin: 0 auto;'>");
        html.append("<div style='background-color: #003366; color: white; padding: 20px; text-align: center;'>");
        html.append("<h1>Ticket Confirmation</h1>");
        html.append(CLOSING_DIV);

        html.append("<div style='padding: 20px;'>");
        html.append("<p>Dear ").append(userName).append(",</p>");
        html.append("<p>Thank you for your purchase! Here are your ticket details:</p>");

        html.append("<div style='background-color: #f5f5f5; padding: 20px; border-radius: 5px;'>");
        html.append("<h2>Match Information</h2>");
        html.append("<p>").append(matchDetails).append("</p>");

        html.append("<h3>Seat Numbers</h3>");
        html.append("<ul>");
        for (String seat : seatNumbers) {
            html.append("<li>").append(seat).append("</li>");
        }
        html.append("</ul>");

        html.append("<p style='font-size: 24px; color: #003366;'>Total Price: €")
                .append(String.format("%.2f", totalPrice)).append("</p>");

        html.append("<p>Ticket ID: <strong>").append(ticketId).append("</strong></p>");
        html.append("<p>Purchase Date: ").append(LocalDateTime.now().format(DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm")))
                .append("</p>");

        html.append("<div style='text-align: center; margin-top: 20px;'>");
        html.append("<p><strong>Scan to View Your Digital Ticket</strong></p>");
        if (!qrCodeBase64.isEmpty()) {
            html.append("<img src='").append(qrCodeBase64).append("' alt='Ticket QR Code' style='max-width: 200px;'/>");
            html.append("<p style='font-size: 12px; color: #666;'>Scan this QR code to access your digital ticket</p>");
        }
        html.append(CLOSING_DIV);
        html.append(CLOSING_DIV);

        html.append("<p>Please keep this email for your records. Show this QR code at the venue entrance.</p>");
        html.append(CLOSING_DIV);

        html.append("<div style='text-align: center; margin-top: 30px; font-size: 12px; color: #666;'>");
        html.append("<p>This is an automated email, please do not reply.</p>");
        html.append("<p>For support, please contact our customer service.</p>");
        html.append(CLOSING_DIV);
        html.append(CLOSING_DIV);

        return html.toString();
    }
}