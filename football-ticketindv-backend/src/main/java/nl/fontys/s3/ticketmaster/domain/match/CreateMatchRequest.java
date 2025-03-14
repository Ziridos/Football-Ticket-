package nl.fontys.s3.ticketmaster.domain.match;

import jakarta.validation.constraints.NotBlank;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class CreateMatchRequest {
    @NotBlank
    private String homeClubName;
    @NotBlank
    private String awayClubName;
    private LocalDateTime matchDateTime;
    @NotBlank
    private String competitionName;
}
