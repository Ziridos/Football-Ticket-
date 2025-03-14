package nl.fontys.s3.ticketmaster.business.impl.ticketsalesimpl;

import nl.fontys.s3.ticketmaster.domain.ticketsales.GetTicketSalesStatisticsRequest;
import nl.fontys.s3.ticketmaster.domain.ticketsales.GetTicketSalesStatisticsResponse;
import nl.fontys.s3.ticketmaster.domain.ticketsales.TicketSalesStatistics;
import nl.fontys.s3.ticketmaster.persitence.TicketRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class GetTicketSalesStatisticsUseCaseImplTest {
    @Mock
    private TicketRepository ticketRepository;

    @InjectMocks
    private GetTicketSalesStatisticsUseCaseImpl getTicketSalesStatisticsUseCase;

    private LocalDateTime startDate;
    private LocalDateTime endDate;
    private GetTicketSalesStatisticsRequest request;
    private static final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

    @BeforeEach
    void setUp() {
        startDate = LocalDateTime.of(2024, 1, 1, 0, 0);
        endDate = LocalDateTime.of(2024, 1, 31, 23, 59, 59);
        request = GetTicketSalesStatisticsRequest.builder()
                .startDate(startDate)
                .endDate(endDate)
                .build();
    }

    @Test
    void getTicketSalesStatistics_WithValidDataAndResults_ReturnsStatistics() {
        // Arrange
        TicketSalesStatistics statistics = TicketSalesStatistics.builder()
                .totalTickets(100L)
                .totalRevenue(1000.0)
                .averageTicketPrice(10.0)
                .build();
        when(ticketRepository.getTicketSalesStatistics(startDate, endDate))
                .thenReturn(List.of(statistics));

        // Act
        GetTicketSalesStatisticsResponse response = getTicketSalesStatisticsUseCase.getTicketSalesStatistics(request);

        // Assert
        assertNotNull(response);
        assertEquals(statistics, response.getStatistics());
        assertEquals(startDate.format(formatter), response.getPeriodStart());
        assertEquals(endDate.format(formatter), response.getPeriodEnd());
    }

    @Test
    void getTicketSalesStatistics_WithNoResults_ReturnsEmptyStatistics() {
        // Arrange
        when(ticketRepository.getTicketSalesStatistics(startDate, endDate))
                .thenReturn(Collections.emptyList());

        // Act
        GetTicketSalesStatisticsResponse response = getTicketSalesStatisticsUseCase.getTicketSalesStatistics(request);

        // Assert
        assertNotNull(response);
        assertEquals(0L, response.getStatistics().getTotalTickets());
        assertEquals(0.0, response.getStatistics().getTotalRevenue());
        assertEquals(0.0, response.getStatistics().getAverageTicketPrice());
        assertEquals(startDate.format(formatter), response.getPeriodStart());
        assertEquals(endDate.format(formatter), response.getPeriodEnd());
    }

    @Test
    void getTicketSalesStatistics_WithNullStartDate_ThrowsException() {
        // Arrange
        request = GetTicketSalesStatisticsRequest.builder()
                .startDate(null)
                .endDate(endDate)
                .build();

        // Act & Assert
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class,
                () -> getTicketSalesStatisticsUseCase.getTicketSalesStatistics(request));
        assertEquals("Start date and end date must not be null", exception.getMessage());
    }

    @Test
    void getTicketSalesStatistics_WithNullEndDate_ThrowsException() {
        // Arrange
        request = GetTicketSalesStatisticsRequest.builder()
                .startDate(startDate)
                .endDate(null)
                .build();

        // Act & Assert
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class,
                () -> getTicketSalesStatisticsUseCase.getTicketSalesStatistics(request));
        assertEquals("Start date and end date must not be null", exception.getMessage());
    }

    @Test
    void getTicketSalesStatistics_WithEndDateBeforeStartDate_ThrowsException() {
        // Arrange
        request = GetTicketSalesStatisticsRequest.builder()
                .startDate(endDate)
                .endDate(startDate)
                .build();

        // Act & Assert
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class,
                () -> getTicketSalesStatisticsUseCase.getTicketSalesStatistics(request));
        assertEquals("End date must be after start date", exception.getMessage());
    }

    @Test
    void getTicketSalesStatistics_WithSameDates_Succeeds() {
        // Arrange
        LocalDateTime sameDate = LocalDateTime.of(2024, 1, 1, 0, 0);
        request = GetTicketSalesStatisticsRequest.builder()
                .startDate(sameDate)
                .endDate(sameDate)
                .build();
        when(ticketRepository.getTicketSalesStatistics(any(), any()))
                .thenReturn(Collections.emptyList());

        // Act & Assert
        assertDoesNotThrow(() -> getTicketSalesStatisticsUseCase.getTicketSalesStatistics(request));
    }
}