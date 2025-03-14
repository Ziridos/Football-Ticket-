package nl.fontys.s3.ticketmaster.domain.box;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Builder;
import lombok.Data;
import nl.fontys.s3.ticketmaster.persitence.entity.BlockEntity;

import java.util.ArrayList;
import java.util.List;

@Data
@Builder
public class CreateBoxRequest {
    @NotBlank
    private String boxName;
    @NotNull
    private int xPosition;
    @NotNull
    private int yPosition;
    @NotNull
    private int width;
    @NotNull
    private int height;

    private Long stadiumId;
    @Builder.Default
    private List<BlockEntity> blocks = new ArrayList<>();
}

