package nl.fontys.s3.ticketmaster.persitence.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "stadium")
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class StadiumEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(name = "stadium_name", unique = true)
    private String stadiumName;

    @Column(name = "stadium_address")
    private String stadiumAddress;

    @Column(name = "stadium_postal_code")
    private String stadiumPostalCode;

    @Column(name = "stadium_city")
    private String stadiumCity;

    @Column(name = "stadium_country")
    private String stadiumCountry;
}