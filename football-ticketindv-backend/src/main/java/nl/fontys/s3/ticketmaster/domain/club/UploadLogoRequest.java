package nl.fontys.s3.ticketmaster.domain.club;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.web.multipart.MultipartFile;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class UploadLogoRequest {
    @NotNull(message = "File is required")
    private MultipartFile file;
}