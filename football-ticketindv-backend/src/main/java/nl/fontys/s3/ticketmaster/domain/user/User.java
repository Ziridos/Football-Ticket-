package nl.fontys.s3.ticketmaster.domain.user;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Builder
@Data
@AllArgsConstructor
@NoArgsConstructor
public class User {
    private Long id;
    private String name;
    private String email;
    private String password;
    private String address;
    private String phone;
    private String country;
    private String city;
    private String postalCode;
    private Role role;
}
