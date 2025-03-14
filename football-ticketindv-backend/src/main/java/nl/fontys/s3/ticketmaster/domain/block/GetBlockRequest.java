package nl.fontys.s3.ticketmaster.domain.block;

import jakarta.validation.constraints.NotNull;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class GetBlockRequest {
    @NotNull
    private Long boxId;
}
