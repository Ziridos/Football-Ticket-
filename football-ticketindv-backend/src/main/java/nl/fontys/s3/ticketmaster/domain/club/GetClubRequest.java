package nl.fontys.s3.ticketmaster.domain.club;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class GetClubRequest {
    @NotBlank
    private Long clubId;
}
