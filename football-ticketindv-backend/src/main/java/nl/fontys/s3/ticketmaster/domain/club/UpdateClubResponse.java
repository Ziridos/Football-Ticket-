package nl.fontys.s3.ticketmaster.domain.club;

import lombok.Builder;
import lombok.Data;
import nl.fontys.s3.ticketmaster.persitence.entity.StadiumEntity;

@Data
@Builder
public class UpdateClubResponse {
    private Long clubId;
    private String clubName;
    private StadiumEntity stadium;
}
