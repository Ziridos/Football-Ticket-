package nl.fontys.s3.ticketmaster.persitence.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "box_pricing_rule")
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class BoxPricingRuleEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @ManyToOne
    @JoinColumn(name = "stadium_id")
    private StadiumEntity stadium;

    @Column(name = "occupancy_threshold")
    private double occupancyThreshold;

    @Column(name = "price_increase_percentage")
    private double priceIncreasePercentage;
}