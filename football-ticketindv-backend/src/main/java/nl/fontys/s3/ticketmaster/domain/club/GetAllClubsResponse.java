package nl.fontys.s3.ticketmaster.domain.club;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class GetAllClubsResponse {
    private List<Club> clubs;
    private long totalElements;
    private int totalPages;
    private int currentPage;
}