package nl.fontys.s3.ticketmaster.domain.stripe;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CreatePaymentResponse {
    private String clientSecret;
}