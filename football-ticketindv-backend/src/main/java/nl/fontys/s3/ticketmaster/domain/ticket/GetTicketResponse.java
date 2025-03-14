package nl.fontys.s3.ticketmaster.domain.ticket;

import lombok.Builder;
import lombok.Data;



import java.time.LocalDateTime;
import java.util.List;

@Data
@Builder
public class GetTicketResponse {
    private Long id;
    private Long userId;
    private Long matchId;
    private List<Long> seatIds;
    private LocalDateTime purchaseDateTime;
    private double totalPrice;
}
