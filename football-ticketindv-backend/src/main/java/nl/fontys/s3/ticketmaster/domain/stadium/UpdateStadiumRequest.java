package nl.fontys.s3.ticketmaster.domain.stadium;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class UpdateStadiumRequest {
    @NotBlank
    private String stadiumName;
    @NotBlank
    private String stadiumAddress;
    @NotBlank
    private String stadiumPostalCode;
    @NotBlank
    private String stadiumCity;
    @NotBlank
    private String stadiumCountry;
}
