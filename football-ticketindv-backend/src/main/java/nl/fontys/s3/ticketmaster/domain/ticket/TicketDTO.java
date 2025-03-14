package nl.fontys.s3.ticketmaster.domain.ticket;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import nl.fontys.s3.ticketmaster.domain.match.MatchDTO;
import nl.fontys.s3.ticketmaster.domain.seat.SeatDTO;
import nl.fontys.s3.ticketmaster.domain.user.UserDTO;




import java.time.LocalDateTime;
import java.util.List;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class TicketDTO {
    private Long id;
    private UserDTO user;
    private MatchDTO match;
    private List<SeatDTO> seats;
    private LocalDateTime purchaseDateTime;
    private double totalPrice;


}
