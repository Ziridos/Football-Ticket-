package nl.fontys.s3.ticketmaster.business.interfaces.userinterfaces;

import nl.fontys.s3.ticketmaster.domain.user.*;
import nl.fontys.s3.ticketmaster.persitence.entity.UserEntity;

public interface UserConverterI {
    User convert(UserEntity userEntity);
    GetUserResponse convertToGetUserResponse(UserEntity userEntity);
    UserEntity convertToEntity(CreateUserRequest request);
    CreateUserResponse convertToCreateUserResponse(UserEntity userEntity);
    UserEntity updateEntityFromRequest(UserEntity user, UpdateUserRequest request);
    UpdateUserResponse convertToUpdateUserResponse(UserEntity userEntity);
    UserDTO toUserDTO(UserEntity entity);
    CreateUserRequest convertRegisterToCreateRequest(RegisterUserRequest request);

}
