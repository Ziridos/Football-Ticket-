package nl.fontys.s3.ticketmaster.business.interfaces.userinterfaces;

import nl.fontys.s3.ticketmaster.persitence.entity.UserEntity;

public interface UserService {
    UserEntity getUserById(Long id);
    UserEntity saveUser(UserEntity user);
}
