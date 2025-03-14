package nl.fontys.s3.ticketmaster.persitence.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "club")
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ClubEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(name = "club_name", unique = true)
    private String clubName;

    @Lob
    @Column(name = "logo")
    private byte[] logo;

    @Column(name = "logo_content_type")
    private String logoContentType;

    @ManyToOne
    @JoinColumn(name = "stadium_id")
    private StadiumEntity stadium;
}
