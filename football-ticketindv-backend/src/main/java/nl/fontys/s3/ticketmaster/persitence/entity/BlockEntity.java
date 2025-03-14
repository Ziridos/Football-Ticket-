package nl.fontys.s3.ticketmaster.persitence.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "block")
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor

public class BlockEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(name = "block_name")
    private String blockName;

    @Column(name = "x_position")
    private int xPosition;

    @Column(name = "y_position")
    private int yPosition;

    @Column(name = "width")
    private int width;

    @Column(name = "height")
    private int height;

    @ManyToOne
    @JoinColumn(name = "box_id", nullable = false)
    private BoxEntity box;

    @OneToMany(mappedBy = "block", fetch = FetchType.EAGER, cascade = CascadeType.ALL, orphanRemoval = true)
    private List<SeatEntity> seats = new ArrayList<>();

}