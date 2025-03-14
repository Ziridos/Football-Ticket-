package nl.fontys.s3.ticketmaster.business.interfaces.userinterfaces;

import nl.fontys.s3.ticketmaster.domain.user.User;

import java.util.Optional;

public interface GetUserUseCase {
    Optional<User> getUser(Long id);
}
