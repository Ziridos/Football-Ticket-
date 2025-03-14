package nl.fontys.s3.ticketmaster.domain.club;


import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CreateClubRequest {

    @NotBlank
    private String clubName;

    @NotNull
    private Long stadiumId;
}
