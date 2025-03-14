package nl.fontys.s3.ticketmaster.persitence.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;


@Entity
@Table(name = "seat")
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class SeatEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(name = "seat_number")
    private String seatNumber;

    @Column(name = "x_position")
    private int xPosition;

    @Column(name = "y_position")
    private int yPosition;

    @ManyToOne
    @JoinColumn(name = "block_id", nullable = false)
    private BlockEntity block;

    @Column(name = "price")
    private Double price;
}