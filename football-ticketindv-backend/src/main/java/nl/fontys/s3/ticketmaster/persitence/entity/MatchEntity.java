package nl.fontys.s3.ticketmaster.persitence.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.Map;

@Entity
@Table(name = "`match`")
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class MatchEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @ManyToOne
    @JoinColumn(name = "home_club_id")
    private ClubEntity homeClub;

    @ManyToOne
    @JoinColumn(name = "away_club_id")
    private ClubEntity awayClub;

    @Column(name = "match_date_time")
    private LocalDateTime matchDateTime;

    @ManyToOne
    @JoinColumn(name = "competition_id")
    private CompetitionEntity competition;

    @ElementCollection(fetch = FetchType.EAGER)
    @CollectionTable(name = "match_available_seat",
            joinColumns = @JoinColumn(name = "match_id"))
    @MapKeyJoinColumn(name = "seat_id")
    @Column(name = "is_available")
    private Map<SeatEntity, Boolean> availableSeats;

    @ElementCollection(fetch = FetchType.EAGER)
    @CollectionTable(name = "match_specific_box_price",
            joinColumns = @JoinColumn(name = "match_id"))
    @MapKeyJoinColumn(name = "box_id")
    @Column(name = "price")
    private Map<BoxEntity, Double> matchSpecificBoxPrices;

    @ElementCollection(fetch = FetchType.EAGER)
    @CollectionTable(name = "match_specific_seat_price",
            joinColumns = @JoinColumn(name = "match_id"))
    @MapKeyJoinColumn(name = "seat_id")
    @Column(name = "price")
    private Map<SeatEntity, Double> matchSpecificSeatPrices;
}