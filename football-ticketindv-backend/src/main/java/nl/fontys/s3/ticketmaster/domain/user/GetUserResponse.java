package nl.fontys.s3.ticketmaster.domain.user;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class GetUserResponse {
    private Long id;
    private String name;
    private String email;
    private String address;
    private String phone;
    private String country;
    private String city;
    private String postalCode;
    private Role role;
}
