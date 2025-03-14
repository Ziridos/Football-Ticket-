package nl.fontys.s3.ticketmaster.domain.ticket;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class GetAllTicketsUserResponse {
    private List<TicketDTO> tickets;
    private long totalElements;
    private int totalPages;
    private int currentPage;
}