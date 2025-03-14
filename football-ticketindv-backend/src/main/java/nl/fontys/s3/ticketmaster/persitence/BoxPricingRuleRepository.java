package nl.fontys.s3.ticketmaster.persitence;

import nl.fontys.s3.ticketmaster.persitence.entity.BoxPricingRuleEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface BoxPricingRuleRepository extends JpaRepository<BoxPricingRuleEntity, Long> {
    List<BoxPricingRuleEntity> findAllByStadiumId(Long stadiumId);
}