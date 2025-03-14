package nl.fontys.s3.ticketmaster.domain.ticket;


import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;



import java.util.List;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class UpdateTicketRequest {
    @NotNull
    private Long userId;
    @NotNull
    private Long matchId;
    @NotNull
    private List<Long> seatIds;
    @NotNull
    @Positive
    private double totalPrice;
}
