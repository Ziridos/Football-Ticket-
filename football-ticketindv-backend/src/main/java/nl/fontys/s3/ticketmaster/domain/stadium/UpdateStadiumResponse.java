package nl.fontys.s3.ticketmaster.domain.stadium;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class UpdateStadiumResponse {
    private Long stadiumId;
    private String stadiumName;
    private String stadiumAddress;
    private String stadiumPostalCode;
    private String stadiumCity;
    private String stadiumCountry;
}
