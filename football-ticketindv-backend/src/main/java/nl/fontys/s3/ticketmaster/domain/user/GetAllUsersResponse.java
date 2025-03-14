package nl.fontys.s3.ticketmaster.domain.user;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class GetAllUsersResponse {
    private List<User> users;
    private long totalElements;
    private int totalPages;
    private int currentPage;
}