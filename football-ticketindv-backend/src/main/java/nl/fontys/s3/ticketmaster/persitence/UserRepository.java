package nl.fontys.s3.ticketmaster.persitence;

import nl.fontys.s3.ticketmaster.domain.user.Role;
import nl.fontys.s3.ticketmaster.persitence.entity.UserEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;


import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<UserEntity, Long> {
    boolean existsByName(String name);
    Optional<UserEntity> findByName(String name);
    Optional<UserEntity> findByEmail(String email);
    boolean existsByEmail(String email);


    @Query("SELECT u FROM UserEntity u " +
            "WHERE (:name IS NULL OR :name = '' OR LOWER(u.name) LIKE LOWER(CONCAT('%', :name, '%'))) " +
            "AND (:email IS NULL OR :email = '' OR LOWER(u.email) LIKE LOWER(CONCAT('%', :email, '%'))) " +
            "AND (:role IS NULL OR u.role = :role)")
    Page<UserEntity> findByFilters(
            @Param("name") String name,
            @Param("email") String email,
            @Param("role") Role role,
            Pageable pageable
    );
}