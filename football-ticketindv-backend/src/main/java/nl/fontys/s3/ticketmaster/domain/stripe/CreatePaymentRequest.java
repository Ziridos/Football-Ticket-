package nl.fontys.s3.ticketmaster.domain.stripe;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CreatePaymentRequest {
    private Long matchId;
    private List<Long> seatIds;
    private Long userId;
}