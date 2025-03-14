package nl.fontys.s3.ticketmaster.domain.user;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class CreateUserRequest {
    @NotBlank
    private String name;

    @NotBlank
    @Email
    private String email;

    @NotBlank
    private String password;

    @NotBlank
    private String address;
    @NotBlank
    private String phone;
    @NotBlank
    private String country;
    @NotBlank
    private String city;
    @NotBlank
    private String postalCode;
    @NotNull
    private Role role;
}

