package nl.fontys.s3.ticketmaster.domain.competition;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class GetAllCompetitionsRequest {
    private String name;
    private int page;
    private int size;
    @Builder.Default
    private String sortBy = "id";
    @Builder.Default
    private String sortDirection = "ASC";
}