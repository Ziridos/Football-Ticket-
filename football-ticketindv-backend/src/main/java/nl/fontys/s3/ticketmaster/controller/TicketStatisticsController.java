package nl.fontys.s3.ticketmaster.controller;

import lombok.AllArgsConstructor;
import nl.fontys.s3.ticketmaster.business.interfaces.ticketsales.GetTicketSalesStatisticsUseCase;
import nl.fontys.s3.ticketmaster.domain.ticketsales.GetTicketSalesStatisticsRequest;
import nl.fontys.s3.ticketmaster.domain.ticketsales.GetTicketSalesStatisticsResponse;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;

@RestController
@RequestMapping("/tickets/statistics")
@CrossOrigin(origins = "http://localhost:5173")
@AllArgsConstructor
public class TicketStatisticsController {
    private final GetTicketSalesStatisticsUseCase getTicketSalesStatisticsUseCase;

    @GetMapping
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public ResponseEntity<GetTicketSalesStatisticsResponse> getTicketSalesStatistics(
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime startDate,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime endDate) {

        GetTicketSalesStatisticsRequest request = GetTicketSalesStatisticsRequest.builder()
                .startDate(startDate)
                .endDate(endDate)
                .build();

        GetTicketSalesStatisticsResponse response = getTicketSalesStatisticsUseCase.getTicketSalesStatistics(request);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/quarterly")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public ResponseEntity<GetTicketSalesStatisticsResponse> getQuarterlyTicketSales(
            @RequestParam int year,
            @RequestParam int quarter) {

        if (quarter < 1 || quarter > 4) {
            return ResponseEntity.badRequest().build();
        }

        LocalDateTime startDate = LocalDateTime.of(year, ((quarter - 1) * 3) + 1, 1, 0, 0);
        LocalDateTime endDate = startDate.plusMonths(3).minusSeconds(1);

        GetTicketSalesStatisticsRequest request = GetTicketSalesStatisticsRequest.builder()
                .startDate(startDate)
                .endDate(endDate)
                .build();

        GetTicketSalesStatisticsResponse response = getTicketSalesStatisticsUseCase.getTicketSalesStatistics(request);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/monthly")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public ResponseEntity<GetTicketSalesStatisticsResponse> getMonthlyTicketSales(
            @RequestParam int year,
            @RequestParam int month) {

        if (month < 1 || month > 12) {
            return ResponseEntity.badRequest().build();
        }

        LocalDateTime startDate = LocalDateTime.of(year, month, 1, 0, 0);
        LocalDateTime endDate = startDate.plusMonths(1).minusSeconds(1);

        GetTicketSalesStatisticsRequest request = GetTicketSalesStatisticsRequest.builder()
                .startDate(startDate)
                .endDate(endDate)
                .build();

        GetTicketSalesStatisticsResponse response = getTicketSalesStatisticsUseCase.getTicketSalesStatistics(request);
        return ResponseEntity.ok(response);
    }
}