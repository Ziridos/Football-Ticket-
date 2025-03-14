package nl.fontys.s3.ticketmaster.domain.ticket;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import nl.fontys.s3.ticketmaster.persitence.entity.MatchEntity;
import nl.fontys.s3.ticketmaster.persitence.entity.SeatEntity;
import nl.fontys.s3.ticketmaster.persitence.entity.UserEntity;

import java.time.LocalDateTime;
import java.util.List;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Ticket {
    private Long id;
    private UserEntity user;
    private MatchEntity match;
    private List<SeatEntity> seats;
    private LocalDateTime purchaseDateTime;
    private double totalPrice;


}
