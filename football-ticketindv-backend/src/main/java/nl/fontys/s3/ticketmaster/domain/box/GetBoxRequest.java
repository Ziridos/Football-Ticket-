package nl.fontys.s3.ticketmaster.domain.box;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class GetBoxRequest {
    @NotNull
    private Long boxId;
}
