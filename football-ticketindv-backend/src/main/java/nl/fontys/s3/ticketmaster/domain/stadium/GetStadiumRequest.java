package nl.fontys.s3.ticketmaster.domain.stadium;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class GetStadiumRequest {
    private Long stadiumId;
}
