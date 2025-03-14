package nl.fontys.s3.ticketmaster.domain.competition;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class GetAllCompetitionsResponse {
    private List<Competition> competitions;
    private long totalElements;
    private int totalPages;
    private int currentPage;
}