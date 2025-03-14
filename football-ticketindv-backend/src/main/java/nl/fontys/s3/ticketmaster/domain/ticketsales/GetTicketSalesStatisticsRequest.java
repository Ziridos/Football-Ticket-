package nl.fontys.s3.ticketmaster.domain.ticketsales;

import lombok.Builder;
import lombok.Data;
import java.time.LocalDateTime;

@Data
@Builder
public class GetTicketSalesStatisticsRequest {
    private LocalDateTime startDate;
    private LocalDateTime endDate;
}
