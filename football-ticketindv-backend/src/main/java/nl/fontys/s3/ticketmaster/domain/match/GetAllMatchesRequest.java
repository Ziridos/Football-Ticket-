package nl.fontys.s3.ticketmaster.domain.match;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class GetAllMatchesRequest {
    private String homeClubName;
    private String awayClubName;
    private String competitionName;
    private LocalDate date;
    private int page;
    private int size;
    @Builder.Default
    private String sortBy = "matchDateTime";
    @Builder.Default
    private String sortDirection = "DESC";
}
