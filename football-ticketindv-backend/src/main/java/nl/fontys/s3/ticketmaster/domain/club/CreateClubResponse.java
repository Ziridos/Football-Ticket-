package nl.fontys.s3.ticketmaster.domain.club;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class CreateClubResponse {
    private String clubName;
    private Long clubId;
}
