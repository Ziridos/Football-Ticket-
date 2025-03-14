package nl.fontys.s3.ticketmaster.business.interfaces.userinterfaces;

import nl.fontys.s3.ticketmaster.domain.user.UpdateUserRequest;
import nl.fontys.s3.ticketmaster.domain.user.UpdateUserResponse;

public interface UpdateUserUseCase {
    UpdateUserResponse updateUser(UpdateUserRequest request);
}
