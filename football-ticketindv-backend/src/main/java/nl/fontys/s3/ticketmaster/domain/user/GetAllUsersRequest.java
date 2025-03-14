package nl.fontys.s3.ticketmaster.domain.user;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class GetAllUsersRequest {
    private String name;
    private String email;
    private Role role;
    private int page;
    private int size;
    @Builder.Default
    private String sortBy = "id";
    @Builder.Default
    private String sortDirection = "ASC";
}