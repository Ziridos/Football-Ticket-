package nl.fontys.s3.ticketmaster.persitence.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import nl.fontys.s3.ticketmaster.domain.user.Role;
import org.hibernate.validator.constraints.Length;

@Entity
@Table(name = "users")
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class UserEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @NotBlank
    @Length(min = 3, max = 20)
    @Column(name = "name")
    private String name;

    @NotBlank
    @Email
    @Column(name = "email", unique = true)
    private String email;

    @NotBlank
    @Column(name = "password")
    private String password;

    @NotBlank
    @Column(name = "address")
    private String address;

    @NotBlank
    @Length(min = 6, max = 15)
    @Column(name = "phone")
    private String phone;

    @NotBlank
    @Column(name = "country")
    private String country;

    @NotBlank
    @Column(name = "city")
    private String city;

    @NotBlank
    @Column(name = "postal_code")
    private String postalCode;

    @NotNull
    @Enumerated(EnumType.STRING)
    @Column(name = "role")
    private Role role;
}