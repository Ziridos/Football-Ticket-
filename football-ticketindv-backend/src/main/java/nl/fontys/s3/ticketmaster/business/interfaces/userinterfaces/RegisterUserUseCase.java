package nl.fontys.s3.ticketmaster.business.interfaces.userinterfaces;

import nl.fontys.s3.ticketmaster.domain.user.RegisterUserRequest;
import nl.fontys.s3.ticketmaster.domain.user.CreateUserResponse;

public interface RegisterUserUseCase {
    CreateUserResponse registerUser(RegisterUserRequest request);
}