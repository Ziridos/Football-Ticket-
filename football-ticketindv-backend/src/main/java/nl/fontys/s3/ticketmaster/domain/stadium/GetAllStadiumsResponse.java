package nl.fontys.s3.ticketmaster.domain.stadium;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class GetAllStadiumsResponse {
    private List<Stadium> stadiums;
    private long totalElements;
    private int totalPages;
    private int currentPage;
}