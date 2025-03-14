package nl.fontys.s3.ticketmaster.domain.block;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class BlockDTO {
    private Long blockId;
    private String blockName;

}
