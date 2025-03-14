package nl.fontys.s3.ticketmaster.domain.match;



import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class GetMatchRequest {
    @NotNull
    private Long matchId;
}
