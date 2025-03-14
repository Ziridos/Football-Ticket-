package nl.fontys.s3.ticketmaster.domain.stadium;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class CreateStadiumResponse {
    private Long stadiumId;
    private String stadiumName;
    private String stadiumAddress;
    private String stadiumPostalCode;
    private String stadiumCity;
    private String stadiumCountry;
}
