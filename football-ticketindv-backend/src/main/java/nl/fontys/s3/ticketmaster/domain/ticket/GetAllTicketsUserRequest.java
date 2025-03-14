package nl.fontys.s3.ticketmaster.domain.ticket;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class GetAllTicketsUserRequest {
    private long id;
    private Integer year;
    private Integer quarter;
    private int page;
    private int size;
    @Builder.Default
    private String sortBy = "purchaseDateTime";
    @Builder.Default
    private String sortDirection = "DESC";
}