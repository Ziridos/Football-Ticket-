package nl.fontys.s3.ticketmaster.business.impl.ticketsalesimpl;

import lombok.AllArgsConstructor;
import nl.fontys.s3.ticketmaster.business.interfaces.ticketsales.GetTicketSalesStatisticsUseCase;
import nl.fontys.s3.ticketmaster.domain.ticketsales.GetTicketSalesStatisticsRequest;
import nl.fontys.s3.ticketmaster.domain.ticketsales.GetTicketSalesStatisticsResponse;
import nl.fontys.s3.ticketmaster.domain.ticketsales.TicketSalesStatistics;
import nl.fontys.s3.ticketmaster.persitence.TicketRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.format.DateTimeFormatter;
import java.util.List;

@Service
@AllArgsConstructor
public class GetTicketSalesStatisticsUseCaseImpl implements GetTicketSalesStatisticsUseCase {
    private final TicketRepository ticketRepository;
    private static final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

    @Override
    @Transactional(readOnly = true)
    public GetTicketSalesStatisticsResponse getTicketSalesStatistics(GetTicketSalesStatisticsRequest request) {
        if (request.getStartDate() == null || request.getEndDate() == null) {
            throw new IllegalArgumentException("Start date and end date must not be null");
        }

        if (request.getEndDate().isBefore(request.getStartDate())) {
            throw new IllegalArgumentException("End date must be after start date");
        }

        List<TicketSalesStatistics> statisticsList = ticketRepository
                .getTicketSalesStatistics(request.getStartDate(), request.getEndDate());

        TicketSalesStatistics statistics = statisticsList.isEmpty() ?
                TicketSalesStatistics.empty() :
                statisticsList.get(0);

        return GetTicketSalesStatisticsResponse.builder()
                .statistics(statistics)
                .periodStart(request.getStartDate().format(formatter))
                .periodEnd(request.getEndDate().format(formatter))
                .build();
    }
}