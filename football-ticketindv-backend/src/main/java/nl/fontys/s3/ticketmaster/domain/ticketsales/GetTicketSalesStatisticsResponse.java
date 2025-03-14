package nl.fontys.s3.ticketmaster.domain.ticketsales;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class GetTicketSalesStatisticsResponse {
    private TicketSalesStatistics statistics;
    private String periodStart;
    private String periodEnd;
}