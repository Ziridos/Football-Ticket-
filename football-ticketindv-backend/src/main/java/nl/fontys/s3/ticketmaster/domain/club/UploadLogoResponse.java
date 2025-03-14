package nl.fontys.s3.ticketmaster.domain.club;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class UploadLogoResponse {
    private Long clubId;
    private String message;
}