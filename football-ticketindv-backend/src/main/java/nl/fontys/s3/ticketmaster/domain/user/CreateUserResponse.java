package nl.fontys.s3.ticketmaster.domain.user;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class CreateUserResponse {
    private Long id;
    private String name;
    private String email;
    // Remove password field
    private String address;
    private String phone;
    private String country;
    private String city;
    private String postalCode;
    private Role role;
}
