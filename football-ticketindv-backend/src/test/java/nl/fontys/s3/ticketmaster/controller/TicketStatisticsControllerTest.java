package nl.fontys.s3.ticketmaster.controller;

import nl.fontys.s3.ticketmaster.business.interfaces.ticketsales.GetTicketSalesStatisticsUseCase;
import nl.fontys.s3.ticketmaster.domain.ticketsales.GetTicketSalesStatisticsRequest;
import nl.fontys.s3.ticketmaster.domain.ticketsales.GetTicketSalesStatisticsResponse;
import nl.fontys.s3.ticketmaster.domain.ticketsales.TicketSalesStatistics;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.ResponseEntity;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class TicketStatisticsControllerTest {
    @Mock
    private GetTicketSalesStatisticsUseCase getTicketSalesStatisticsUseCase;

    @InjectMocks
    private TicketStatisticsController ticketStatisticsController;

    private GetTicketSalesStatisticsResponse mockResponse;
    private static final DateTimeFormatter formatter = DateTimeFormatter.ISO_DATE_TIME;

    @BeforeEach
    void setUp() {
        TicketSalesStatistics statistics = TicketSalesStatistics.builder()
                .totalTickets(100L)
                .totalRevenue(1000.0)
                .averageTicketPrice(10.0)
                .build();

        LocalDateTime startDate = LocalDateTime.of(2024, 1, 1, 0, 0);
        LocalDateTime endDate = LocalDateTime.of(2024, 1, 31, 23, 59);

        mockResponse = GetTicketSalesStatisticsResponse.builder()
                .statistics(statistics)
                .periodStart(startDate.format(formatter))
                .periodEnd(endDate.format(formatter))
                .build();
    }

    @Test
    void getTicketSalesStatistics_ValidDateRange_ReturnsStatistics() {
        // Arrange
        LocalDateTime startDate = LocalDateTime.of(2024, 1, 1, 0, 0);
        LocalDateTime endDate = LocalDateTime.of(2024, 1, 31, 23, 59);
        when(getTicketSalesStatisticsUseCase.getTicketSalesStatistics(any(GetTicketSalesStatisticsRequest.class)))
                .thenReturn(mockResponse);

        // Act
        ResponseEntity<GetTicketSalesStatisticsResponse> response =
                ticketStatisticsController.getTicketSalesStatistics(startDate, endDate);

        // Assert
        assertTrue(response.getStatusCode().is2xxSuccessful());
        assertNotNull(response.getBody());
        assertEquals(100L, response.getBody().getStatistics().getTotalTickets());
        assertEquals(1000.0, response.getBody().getStatistics().getTotalRevenue());
        assertEquals(10.0, response.getBody().getStatistics().getAverageTicketPrice());
        assertEquals(startDate.format(formatter), response.getBody().getPeriodStart());
        assertEquals(endDate.format(formatter), response.getBody().getPeriodEnd());
    }

    @Test
    void getQuarterlyTicketSales_ValidQuarter_ReturnsStatistics() {
        // Arrange
        when(getTicketSalesStatisticsUseCase.getTicketSalesStatistics(any(GetTicketSalesStatisticsRequest.class)))
                .thenReturn(mockResponse);

        // Act
        ResponseEntity<GetTicketSalesStatisticsResponse> response =
                ticketStatisticsController.getQuarterlyTicketSales(2024, 1);

        // Assert
        assertTrue(response.getStatusCode().is2xxSuccessful());
        assertNotNull(response.getBody());
        assertEquals(100L, response.getBody().getStatistics().getTotalTickets());
        assertEquals(1000.0, response.getBody().getStatistics().getTotalRevenue());
        assertEquals(10.0, response.getBody().getStatistics().getAverageTicketPrice());
    }

    @Test
    void getQuarterlyTicketSales_InvalidQuarter_ReturnsBadRequest() {
        // Act
        ResponseEntity<GetTicketSalesStatisticsResponse> response =
                ticketStatisticsController.getQuarterlyTicketSales(2024, 5);

        // Assert
        assertTrue(response.getStatusCode().is4xxClientError());
        assertNull(response.getBody());
    }

    @Test
    void getQuarterlyTicketSales_VerifyDateRangeForQ1() {
        // Arrange
        ArgumentCaptor<GetTicketSalesStatisticsRequest> requestCaptor = ArgumentCaptor.forClass(GetTicketSalesStatisticsRequest.class);
        when(getTicketSalesStatisticsUseCase.getTicketSalesStatistics(requestCaptor.capture()))
                .thenReturn(mockResponse);

        // Act
        ResponseEntity<GetTicketSalesStatisticsResponse> response =
                ticketStatisticsController.getQuarterlyTicketSales(2024, 1);

        // Assert
        assertTrue(response.getStatusCode().is2xxSuccessful());
        assertNotNull(response.getBody());

        GetTicketSalesStatisticsRequest capturedRequest = requestCaptor.getValue();
        assertEquals(LocalDateTime.of(2024, 1, 1, 0, 0), capturedRequest.getStartDate());
        assertEquals(LocalDateTime.of(2024, 3, 31, 23, 59, 59), capturedRequest.getEndDate());
    }

    @Test
    void getMonthlyTicketSales_ValidMonth_ReturnsStatistics() {
        // Arrange
        when(getTicketSalesStatisticsUseCase.getTicketSalesStatistics(any(GetTicketSalesStatisticsRequest.class)))
                .thenReturn(mockResponse);

        // Act
        ResponseEntity<GetTicketSalesStatisticsResponse> response =
                ticketStatisticsController.getMonthlyTicketSales(2024, 1);

        // Assert
        assertTrue(response.getStatusCode().is2xxSuccessful());
        assertNotNull(response.getBody());
        assertEquals(100L, response.getBody().getStatistics().getTotalTickets());
        assertEquals(1000.0, response.getBody().getStatistics().getTotalRevenue());
        assertEquals(10.0, response.getBody().getStatistics().getAverageTicketPrice());
    }

    @Test
    void getMonthlyTicketSales_InvalidMonth_ReturnsBadRequest() {
        // Act
        ResponseEntity<GetTicketSalesStatisticsResponse> response =
                ticketStatisticsController.getMonthlyTicketSales(2024, 13);

        // Assert
        assertTrue(response.getStatusCode().is4xxClientError());
        assertNull(response.getBody());
    }

    @Test
    void getMonthlyTicketSales_VerifyDateRangeForJanuary() {
        // Arrange
        when(getTicketSalesStatisticsUseCase.getTicketSalesStatistics(any(GetTicketSalesStatisticsRequest.class)))
                .thenReturn(mockResponse);

        // Act
        ResponseEntity<GetTicketSalesStatisticsResponse> response =
                ticketStatisticsController.getMonthlyTicketSales(2024, 1);

        // Assert
        assertTrue(response.getStatusCode().is2xxSuccessful());
        assertNotNull(response.getBody());
        assertTrue(response.getBody().getPeriodStart().startsWith("2024-01-01"));
        assertTrue(response.getBody().getPeriodEnd().startsWith("2024-01-31"));
    }
}