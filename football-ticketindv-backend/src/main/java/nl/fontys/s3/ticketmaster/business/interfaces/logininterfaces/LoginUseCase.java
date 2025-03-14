package nl.fontys.s3.ticketmaster.business.interfaces.logininterfaces;


import nl.fontys.s3.ticketmaster.persitence.entity.UserEntity;

import java.util.Optional;

public interface LoginUseCase {
    Optional<UserEntity> authenticateUser(String email, String password);
    Optional<UserEntity> getUserByEmail(String email);

}
