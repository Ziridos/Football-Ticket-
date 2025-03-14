package nl.fontys.s3.ticketmaster.business.interfaces.ticketsales;

import nl.fontys.s3.ticketmaster.domain.ticketsales.GetTicketSalesStatisticsRequest;
import nl.fontys.s3.ticketmaster.domain.ticketsales.GetTicketSalesStatisticsResponse;

public interface GetTicketSalesStatisticsUseCase {
    GetTicketSalesStatisticsResponse getTicketSalesStatistics(GetTicketSalesStatisticsRequest request);
}

