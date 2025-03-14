package nl.fontys.s3.ticketmaster.persitence.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "competition")
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class CompetitionEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(name = "competition_name", unique = true)
    private String competitionName;
}