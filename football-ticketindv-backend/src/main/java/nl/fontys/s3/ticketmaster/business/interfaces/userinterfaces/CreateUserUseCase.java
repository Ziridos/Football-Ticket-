package nl.fontys.s3.ticketmaster.business.interfaces.userinterfaces;

import nl.fontys.s3.ticketmaster.domain.user.CreateUserRequest;
import nl.fontys.s3.ticketmaster.domain.user.CreateUserResponse;

public interface CreateUserUseCase {
    CreateUserResponse createUser(CreateUserRequest request);
}
