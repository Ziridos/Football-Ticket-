package nl.fontys.s3.ticketmaster.business.impl.userimpl;

import nl.fontys.s3.ticketmaster.domain.user.*;
import nl.fontys.s3.ticketmaster.persitence.entity.UserEntity;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class UserConverterTest {

    private UserConverter userConverter;
    private UserEntity testUserEntity;

    @BeforeEach
    void setUp() {
        userConverter = new UserConverter();

        testUserEntity = UserEntity.builder()
                .id(1L)
                .name("John Doe")
                .email("john.doe@example.com")
                .password("password123")
                .address("123 Main St")
                .phone("123-456-7890")
                .city("City")
                .country("Country")
                .postalCode("12345")
                .role(Role.ADMIN)
                .build();
    }

    @Test
    void convert_shouldConvertUserEntityToUser() {
        // Act
        User actualUser = userConverter.convert(testUserEntity);

        // Assert
        assertEquals(testUserEntity.getId(), actualUser.getId());
        assertEquals(testUserEntity.getName(), actualUser.getName());
        assertEquals(testUserEntity.getEmail(), actualUser.getEmail());
        assertEquals(testUserEntity.getPassword(), actualUser.getPassword());
        assertEquals(testUserEntity.getAddress(), actualUser.getAddress());
        assertEquals(testUserEntity.getPhone(), actualUser.getPhone());
        assertEquals(testUserEntity.getCity(), actualUser.getCity());
        assertEquals(testUserEntity.getCountry(), actualUser.getCountry());
        assertEquals(testUserEntity.getPostalCode(), actualUser.getPostalCode());
        assertEquals(testUserEntity.getRole(), actualUser.getRole());
    }

    @Test
    void convertToGetUserResponse_shouldConvertUserEntityToGetUserResponse() {
        // Act
        GetUserResponse response = userConverter.convertToGetUserResponse(testUserEntity);

        // Assert
        assertEquals(testUserEntity.getId(), response.getId());
        assertEquals(testUserEntity.getName(), response.getName());
        assertEquals(testUserEntity.getEmail(), response.getEmail());
        assertEquals(testUserEntity.getAddress(), response.getAddress());
        assertEquals(testUserEntity.getPhone(), response.getPhone());
        assertEquals(testUserEntity.getCity(), response.getCity());
        assertEquals(testUserEntity.getCountry(), response.getCountry());
        assertEquals(testUserEntity.getPostalCode(), response.getPostalCode());
        assertEquals(testUserEntity.getRole(), response.getRole());
    }

    @Test
    void convertToEntity_shouldConvertCreateUserRequestToUserEntity() {
        // Arrange
        CreateUserRequest request = CreateUserRequest.builder()
                .name("Jane Doe")
                .email("jane.doe@example.com")
                .password("password456")
                .address("456 Second St")
                .phone("098-765-4321")
                .city("New City")
                .country("New Country")
                .postalCode("54321")
                .role(Role.USER)
                .build();

        // Act
        UserEntity result = userConverter.convertToEntity(request);

        // Assert
        assertEquals(request.getName(), result.getName());
        assertEquals(request.getEmail(), result.getEmail());
        assertEquals(request.getPassword(), result.getPassword());
        assertEquals(request.getAddress(), result.getAddress());
        assertEquals(request.getPhone(), result.getPhone());
        assertEquals(request.getCity(), result.getCity());
        assertEquals(request.getCountry(), result.getCountry());
        assertEquals(request.getPostalCode(), result.getPostalCode());
        assertEquals(request.getRole(), result.getRole());
    }

    @Test
    void convertToCreateUserResponse_shouldConvertUserEntityToCreateUserResponse() {
        // Act
        CreateUserResponse response = userConverter.convertToCreateUserResponse(testUserEntity);

        // Assert
        assertEquals(testUserEntity.getId(), response.getId());
        assertEquals(testUserEntity.getName(), response.getName());
        assertEquals(testUserEntity.getEmail(), response.getEmail());
        assertEquals(testUserEntity.getAddress(), response.getAddress());
        assertEquals(testUserEntity.getPhone(), response.getPhone());
        assertEquals(testUserEntity.getCity(), response.getCity());
        assertEquals(testUserEntity.getCountry(), response.getCountry());
        assertEquals(testUserEntity.getPostalCode(), response.getPostalCode());
        assertEquals(testUserEntity.getRole(), response.getRole());
    }

    @Test
    void updateEntityFromRequest_shouldUpdateUserEntityWithRequestData() {
        // Arrange
        UpdateUserRequest request = UpdateUserRequest.builder()
                .name("Updated Name")
                .email("updated@example.com")
                .password("newpassword")
                .address("New Address")
                .phone("999-999-9999")
                .city("Updated City")
                .country("Updated Country")
                .postalCode("99999")
                .role(Role.USER)
                .build();

        UserEntity userToUpdate = UserEntity.builder()
                .id(1L)
                .name("Original Name")
                .email("original@example.com")
                .build();

        // Act
        UserEntity updatedUser = userConverter.updateEntityFromRequest(userToUpdate, request);

        // Assert
        assertEquals(request.getName(), updatedUser.getName());
        assertEquals(request.getEmail(), updatedUser.getEmail());
        assertEquals(request.getPassword(), updatedUser.getPassword());
        assertEquals(request.getAddress(), updatedUser.getAddress());
        assertEquals(request.getPhone(), updatedUser.getPhone());
        assertEquals(request.getCity(), updatedUser.getCity());
        assertEquals(request.getCountry(), updatedUser.getCountry());
        assertEquals(request.getPostalCode(), updatedUser.getPostalCode());
        assertEquals(request.getRole(), updatedUser.getRole());
        // Verify ID remains unchanged
        assertEquals(1L, updatedUser.getId());
    }

    @Test
    void convertToUpdateUserResponse_shouldConvertUserEntityToUpdateUserResponse() {
        // Act
        UpdateUserResponse response = userConverter.convertToUpdateUserResponse(testUserEntity);

        // Assert
        assertEquals(testUserEntity.getId(), response.getId());
        assertEquals(testUserEntity.getName(), response.getName());
        assertEquals(testUserEntity.getEmail(), response.getEmail());
        assertEquals(testUserEntity.getPassword(), response.getPassword());
        assertEquals(testUserEntity.getAddress(), response.getAddress());
        assertEquals(testUserEntity.getPhone(), response.getPhone());
        assertEquals(testUserEntity.getCity(), response.getCity());
        assertEquals(testUserEntity.getCountry(), response.getCountry());
        assertEquals(testUserEntity.getPostalCode(), response.getPostalCode());
        assertEquals(testUserEntity.getRole(), response.getRole());
    }

    @Test
    void toUserDTO_ValidEntity_ReturnsCorrectDTO() {
        // Act
        UserDTO result = userConverter.toUserDTO(testUserEntity);

        // Assert
        assertNotNull(result);
        assertEquals(testUserEntity.getId(), result.getId());
        assertEquals(testUserEntity.getName(), result.getName());
        assertEquals(testUserEntity.getEmail(), result.getEmail());
    }

    @Test
    void toUserDTO_EntityWithNullFields_HandlesNullsGracefully() {
        // Arrange
        UserEntity userWithNulls = UserEntity.builder()
                .id(1L)
                .name(null)
                .email(null)
                .build();

        // Act
        UserDTO result = userConverter.toUserDTO(userWithNulls);

        // Assert
        assertNotNull(result);
        assertEquals(1L, result.getId());
        assertNull(result.getName());
        assertNull(result.getEmail());
    }

    @Test
    void convertRegisterToCreateRequest_shouldConvertRegisterRequestToCreateRequest() {
        // Arrange
        RegisterUserRequest registerRequest = RegisterUserRequest.builder()
                .name("Test User")
                .email("test@example.com")
                .password("password123")
                .address("Test Address")
                .phone("123-456-7890")
                .city("Test City")
                .country("Test Country")
                .postalCode("12345")
                .build();

        // Act
        CreateUserRequest result = userConverter.convertRegisterToCreateRequest(registerRequest);

        // Assert
        assertEquals(registerRequest.getName(), result.getName());
        assertEquals(registerRequest.getEmail(), result.getEmail());
        assertEquals(registerRequest.getPassword(), result.getPassword());
        assertEquals(registerRequest.getAddress(), result.getAddress());
        assertEquals(registerRequest.getPhone(), result.getPhone());
        assertEquals(registerRequest.getCity(), result.getCity());
        assertEquals(registerRequest.getCountry(), result.getCountry());
        assertEquals(registerRequest.getPostalCode(), result.getPostalCode());
        assertEquals(Role.USER, result.getRole());
    }
}