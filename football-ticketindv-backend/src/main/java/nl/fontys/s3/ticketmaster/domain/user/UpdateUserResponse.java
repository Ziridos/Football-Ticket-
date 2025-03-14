package nl.fontys.s3.ticketmaster.domain.user;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class UpdateUserResponse {
    private Long id;
    private String name;
    private String email;
    private String password;
    private String address;
    private String phone;
    private String city;
    private String country;
    private String postalCode;
    private Role role;
}
