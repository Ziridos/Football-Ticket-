package nl.fontys.s3.ticketmaster.domain.match;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class GetAllMatchesResponse {
    private List<Match> matches;
    private long totalElements;
    private int totalPages;
    private int currentPage;
}
