package nl.fontys.s3.ticketmaster.persitence;

import nl.fontys.s3.ticketmaster.persitence.entity.BlockEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface BlockRepository extends JpaRepository<BlockEntity, Long> {
    List<BlockEntity> findByBoxId(Long boxId);
    boolean existsByBlockNameAndBoxId(String blockName, Long boxId);
}