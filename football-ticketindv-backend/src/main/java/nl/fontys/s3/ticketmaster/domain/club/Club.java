package nl.fontys.s3.ticketmaster.domain.club;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import nl.fontys.s3.ticketmaster.persitence.entity.StadiumEntity;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Club {
    private Long clubId;
    private String clubName;
    private byte[] logo;
    private String logoContentType;
    private StadiumEntity stadium;
}