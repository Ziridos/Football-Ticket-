package nl.fontys.s3.ticketmaster.business.interfaces.emailinterfaces;

import java.util.List;

public interface EmailServiceI {
    void sendTicketConfirmation(
            String to,
            String userName,
            String matchDetails,
            List<String> seatNumbers,
            double totalPrice,
            String ticketId
    );
}