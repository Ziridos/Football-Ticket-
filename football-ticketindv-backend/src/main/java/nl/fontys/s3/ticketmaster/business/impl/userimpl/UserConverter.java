package nl.fontys.s3.ticketmaster.business.impl.userimpl;

import nl.fontys.s3.ticketmaster.business.interfaces.userinterfaces.UserConverterI;
import nl.fontys.s3.ticketmaster.domain.user.*;
import nl.fontys.s3.ticketmaster.persitence.entity.UserEntity;
import org.springframework.stereotype.Component;

@Component
public class UserConverter implements UserConverterI {

    public User convert(UserEntity userEntity) {
        return User.builder()
                .id(userEntity.getId())
                .name(userEntity.getName())
                .email(userEntity.getEmail())
                .password(userEntity.getPassword())
                .address(userEntity.getAddress())
                .phone(userEntity.getPhone())
                .country(userEntity.getCountry())
                .city(userEntity.getCity())
                .postalCode(userEntity.getPostalCode())
                .role(userEntity.getRole())
                .build();
    }

    public GetUserResponse convertToGetUserResponse(UserEntity userEntity) {
        return GetUserResponse.builder()
                .id(userEntity.getId())
                .name(userEntity.getName())
                .email(userEntity.getEmail())
                .address(userEntity.getAddress())
                .phone(userEntity.getPhone())
                .country(userEntity.getCountry())
                .city(userEntity.getCity())
                .postalCode(userEntity.getPostalCode())
                .role(userEntity.getRole())
                .build();
    }

    public UserEntity convertToEntity(CreateUserRequest request) {
        return UserEntity.builder()
                .name(request.getName())
                .email(request.getEmail())
                .password(request.getPassword())
                .address(request.getAddress())
                .phone(request.getPhone())
                .country(request.getCountry())
                .city(request.getCity())
                .postalCode(request.getPostalCode())
                .role(request.getRole())
                .build();
    }

    public CreateUserResponse convertToCreateUserResponse(UserEntity userEntity) {
        return CreateUserResponse.builder()
                .id(userEntity.getId())
                .name(userEntity.getName())
                .email(userEntity.getEmail())
                .address(userEntity.getAddress())
                .phone(userEntity.getPhone())
                .city(userEntity.getCity())
                .country(userEntity.getCountry())
                .postalCode(userEntity.getPostalCode())
                .role(userEntity.getRole())
                .build();
    }

    public UserEntity updateEntityFromRequest(UserEntity user, UpdateUserRequest request) {
        user.setName(request.getName());
        user.setEmail(request.getEmail());
        user.setPassword(request.getPassword());
        user.setAddress(request.getAddress());
        user.setPhone(request.getPhone());
        user.setCountry(request.getCountry());
        user.setCity(request.getCity());
        user.setPostalCode(request.getPostalCode());
        user.setRole(request.getRole());
        return user;
    }

    public UpdateUserResponse convertToUpdateUserResponse(UserEntity userEntity) {
        return UpdateUserResponse.builder()
                .id(userEntity.getId())
                .name(userEntity.getName())
                .email(userEntity.getEmail())
                .password(userEntity.getPassword())
                .address(userEntity.getAddress())
                .phone(userEntity.getPhone())
                .city(userEntity.getCity())
                .country(userEntity.getCountry())
                .postalCode(userEntity.getPostalCode())
                .role(userEntity.getRole())
                .build();
    }

    @Override
    public UserDTO toUserDTO(UserEntity entity) {
        return UserDTO.builder()
                .id(entity.getId())
                .name(entity.getName())
                .email(entity.getEmail())
                .build();
    }

    public CreateUserRequest convertRegisterToCreateRequest(RegisterUserRequest request) {
        return CreateUserRequest.builder()
                .name(request.getName())
                .email(request.getEmail())
                .password(request.getPassword())
                .address(request.getAddress())
                .phone(request.getPhone())
                .country(request.getCountry())
                .city(request.getCity())
                .postalCode(request.getPostalCode())
                .role(Role.USER)
                .build();
    }
}