package nl.fontys.s3.ticketmaster.persitence.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;

@Entity
@Table(name = "ticket")
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class TicketEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private UserEntity user;

    @ManyToOne
    @JoinColumn(name = "match_id")
    private MatchEntity match;

    @ManyToMany
    @JoinTable(
            name = "ticket_seat",
            joinColumns = @JoinColumn(name = "ticket_id"),
            inverseJoinColumns = @JoinColumn(name = "seat_id")
    )
    private List<SeatEntity> seats;

    @Column(name = "purchase_date_time")
    private LocalDateTime purchaseDateTime;

    @Column(name = "total_price")
    private double totalPrice;
}