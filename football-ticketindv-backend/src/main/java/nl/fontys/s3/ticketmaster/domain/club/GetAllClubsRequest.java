package nl.fontys.s3.ticketmaster.domain.club;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class GetAllClubsRequest {
    private String name;
    private String stadiumName;
    private String stadiumCity;
    private String stadiumCountry;
    private int page;
    private int size;
    @Builder.Default
    private String sortBy = "id";
    @Builder.Default
    private String sortDirection = "ASC";
}
