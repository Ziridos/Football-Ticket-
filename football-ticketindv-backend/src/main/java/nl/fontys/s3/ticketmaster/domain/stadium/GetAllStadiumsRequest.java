package nl.fontys.s3.ticketmaster.domain.stadium;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class GetAllStadiumsRequest {
    private String name;
    private String city;
    private String country;
    private int page;
    private int size;
    @Builder.Default
    private String sortBy = "id";
    @Builder.Default
    private String sortDirection = "ASC";
}
