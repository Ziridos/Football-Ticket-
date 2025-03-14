package nl.fontys.s3.ticketmaster.persitence;

import nl.fontys.s3.ticketmaster.domain.user.Role;
import nl.fontys.s3.ticketmaster.persitence.entity.UserEntity;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
class UserRepositoryTest {
    @Autowired
    private TestEntityManager entityManager;

    @Autowired
    private UserRepository userRepository;

    private UserEntity createTestUser(String name, String email) {
        return UserEntity.builder()
                .name(name)
                .email(email)
                .password("password123")
                .address("123 Test St")
                .phone("1234567890")
                .country("Netherlands")
                .city("Eindhoven")
                .postalCode("5611AA")
                .role(Role.USER)
                .build();
    }

    @Test
    void existsByName_ExistingName_ReturnsTrue() {
        // Arrange
        UserEntity user = createTestUser("testUser", "test@test.com");
        entityManager.persist(user);
        entityManager.flush();

        // Act
        boolean exists = userRepository.existsByName("testUser");

        // Assert
        assertTrue(exists);
    }

    @Test
    void existsByName_NonExistingName_ReturnsFalse() {
        // Act
        boolean exists = userRepository.existsByName("nonExistingUser");

        // Assert
        assertFalse(exists);
    }

    @Test
    void findByName_ExistingName_ReturnsUser() {
        // Arrange
        UserEntity user = createTestUser("testUser", "test@test.com");
        entityManager.persist(user);
        entityManager.flush();

        // Act
        Optional<UserEntity> foundUser = userRepository.findByName("testUser");

        // Assert
        assertTrue(foundUser.isPresent());
        assertEquals("testUser", foundUser.get().getName());
        assertEquals("test@test.com", foundUser.get().getEmail());
    }

    @Test
    void findByName_NonExistingName_ReturnsEmpty() {
        // Act
        Optional<UserEntity> foundUser = userRepository.findByName("nonExistingUser");

        // Assert
        assertFalse(foundUser.isPresent());
    }

    @Test
    void findByEmail_ExistingEmail_ReturnsUser() {
        // Arrange
        UserEntity user = createTestUser("testUser", "test@test.com");
        entityManager.persist(user);
        entityManager.flush();

        // Act
        Optional<UserEntity> foundUser = userRepository.findByEmail("test@test.com");

        // Assert
        assertTrue(foundUser.isPresent());
        assertEquals("testUser", foundUser.get().getName());
        assertEquals("test@test.com", foundUser.get().getEmail());
    }

    @Test
    void findByEmail_NonExistingEmail_ReturnsEmpty() {
        // Act
        Optional<UserEntity> foundUser = userRepository.findByEmail("nonexisting@test.com");

        // Assert
        assertFalse(foundUser.isPresent());
    }

    @Test
    void save_ValidUser_SavesSuccessfully() {
        // Arrange
        UserEntity user = createTestUser("testUser", "test@test.com");

        // Act
        UserEntity savedUser = userRepository.save(user);
        userRepository.flush();

        // Assert
        UserEntity foundUser = entityManager.find(UserEntity.class, savedUser.getId());
        assertNotNull(foundUser);
        assertEquals("testUser", foundUser.getName());
        assertEquals("test@test.com", foundUser.getEmail());
    }

    @Test
    void update_ExistingUser_UpdatesSuccessfully() {
        // Arrange
        UserEntity user = createTestUser("testUser", "test@test.com");
        entityManager.persist(user);
        entityManager.flush();

        // Act
        UserEntity userToUpdate = userRepository.findById(user.getId()).get();
        userToUpdate.setName("updatedName");
        userRepository.save(userToUpdate);
        userRepository.flush();

        // Assert
        UserEntity updatedUser = entityManager.find(UserEntity.class, user.getId());
        assertEquals("updatedName", updatedUser.getName());
    }

    @Test
    void delete_ExistingUser_DeletesSuccessfully() {
        // Arrange
        UserEntity user = createTestUser("testUser", "test@test.com");
        entityManager.persist(user);
        entityManager.flush();
        Long userId = user.getId();

        // Act
        userRepository.deleteById(userId);
        userRepository.flush();

        // Assert
        UserEntity foundUser = entityManager.find(UserEntity.class, userId);
        assertNull(foundUser);
    }

    @Test
    void findByFilters_NoFilters_ReturnsAllUsers() {
        // Arrange
        UserEntity user1 = createTestUser("user1", "user1@test.com");
        UserEntity user2 = createTestUser("user2", "user2@test.com");
        entityManager.persist(user1);
        entityManager.persist(user2);
        entityManager.flush();

        // Act
        Page<UserEntity> result = userRepository.findByFilters(null, null, null,
                Pageable.ofSize(10));

        // Assert
        assertEquals(2, result.getTotalElements());
        assertTrue(result.getContent().stream()
                .map(UserEntity::getName)
                .allMatch(name -> name.equals("user1") || name.equals("user2")));
    }

    @Test
    void findByFilters_NameFilter_ReturnsMatchingUsers() {
        // Arrange
        UserEntity user1 = createTestUser("test_user", "user1@test.com");
        UserEntity user2 = createTestUser("another_user", "user2@test.com");
        entityManager.persist(user1);
        entityManager.persist(user2);
        entityManager.flush();

        // Act
        Page<UserEntity> result = userRepository.findByFilters("test", null, null,
                Pageable.ofSize(10));

        // Assert
        assertEquals(1, result.getTotalElements());
        assertEquals("test_user", result.getContent().get(0).getName());
    }

    @Test
    void findByFilters_EmailFilter_ReturnsMatchingUsers() {
        // Arrange
        UserEntity user1 = createTestUser("user1", "test@example.com");
        UserEntity user2 = createTestUser("user2", "other@test.com");
        entityManager.persist(user1);
        entityManager.persist(user2);
        entityManager.flush();

        // Act
        Page<UserEntity> result = userRepository.findByFilters(null, "example", null,
                Pageable.ofSize(10));

        // Assert
        assertEquals(1, result.getTotalElements());
        assertEquals("test@example.com", result.getContent().get(0).getEmail());
    }

    @Test
    void findByFilters_RoleFilter_ReturnsMatchingUsers() {
        // Arrange
        UserEntity user1 = createTestUser("user1", "user1@test.com");
        UserEntity user2 = createTestUser("admin", "admin@test.com");
        user2.setRole(Role.ADMIN);
        entityManager.persist(user1);
        entityManager.persist(user2);
        entityManager.flush();

        // Act
        Page<UserEntity> result = userRepository.findByFilters(null, null, Role.ADMIN,
                Pageable.ofSize(10));

        // Assert
        assertEquals(1, result.getTotalElements());
        assertEquals(Role.ADMIN, result.getContent().get(0).getRole());
    }

    @Test
    void findByFilters_MultipleFilters_ReturnsMatchingUsers() {
        // Arrange
        UserEntity user1 = createTestUser("test_admin", "admin@example.com");
        user1.setRole(Role.ADMIN);
        UserEntity user2 = createTestUser("test_user", "user@example.com");
        UserEntity user3 = createTestUser("other_admin", "admin@other.com");
        user3.setRole(Role.ADMIN);
        entityManager.persist(user1);
        entityManager.persist(user2);
        entityManager.persist(user3);
        entityManager.flush();

        // Act
        Page<UserEntity> result = userRepository.findByFilters("test", "example", Role.ADMIN,
                Pageable.ofSize(10));

        // Assert
        assertEquals(1, result.getTotalElements());
        UserEntity foundUser = result.getContent().get(0);
        assertEquals("test_admin", foundUser.getName());
        assertEquals("admin@example.com", foundUser.getEmail());
        assertEquals(Role.ADMIN, foundUser.getRole());
    }

    @Test
    void findByFilters_Pagination_ReturnsCorrectPage() {
        // Arrange
        for (int i = 1; i <= 15; i++) {
            UserEntity user = createTestUser("user" + i, "user" + i + "@test.com");
            entityManager.persist(user);
        }
        entityManager.flush();

        // Act
        Page<UserEntity> firstPage = userRepository.findByFilters(null, null, null,
                Pageable.ofSize(10).withPage(0));
        Page<UserEntity> secondPage = userRepository.findByFilters(null, null, null,
                Pageable.ofSize(10).withPage(1));

        // Assert
        assertEquals(15, firstPage.getTotalElements());
        assertEquals(10, firstPage.getContent().size());
        assertEquals(5, secondPage.getContent().size());
        assertEquals(0, firstPage.getNumber());
        assertEquals(1, secondPage.getNumber());
    }

    @Test
    void findByFilters_EmptyFilters_ReturnsAllUsers() {
        // Arrange
        UserEntity user1 = createTestUser("user1", "user1@test.com");
        UserEntity user2 = createTestUser("user2", "user2@test.com");
        entityManager.persist(user1);
        entityManager.persist(user2);
        entityManager.flush();

        // Act
        Page<UserEntity> result = userRepository.findByFilters("", "", null,
                Pageable.ofSize(10));

        // Assert
        assertEquals(2, result.getTotalElements());
    }
}