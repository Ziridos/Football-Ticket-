package nl.fontys.s3.ticketmaster.domain.ticketsales;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class TicketSalesStatistics {
    private long totalTickets;
    private double totalRevenue;
    private double averageTicketPrice;

    public static TicketSalesStatistics empty() {
        return TicketSalesStatistics.builder()
                .totalTickets(0L)
                .totalRevenue(0.0)
                .averageTicketPrice(0.0)
                .build();
    }
}