package nl.fontys.s3.ticketmaster.persitence.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "box")
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class BoxEntity {
        @Id
        @GeneratedValue(strategy = GenerationType.IDENTITY)
        @Column(name = "id")
        private Long id;

        @Column(name = "box_name")
        private String boxName;

        @Column(name = "x_position")
        private int xPosition;

        @Column(name = "y_position")
        private int yPosition;

        @Column(name = "width")
        private int width;

        @Column(name = "height")
        private int height;

        @ManyToOne
        @JoinColumn(name = "stadium_id")
        private StadiumEntity stadium;

        @Column(name = "price")
        private Double price;

        @OneToMany(mappedBy = "box", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.EAGER)
        private List<BlockEntity> blocks = new ArrayList<>();
}