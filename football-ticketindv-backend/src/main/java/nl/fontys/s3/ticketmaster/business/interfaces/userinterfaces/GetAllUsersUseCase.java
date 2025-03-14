package nl.fontys.s3.ticketmaster.business.interfaces.userinterfaces;

import nl.fontys.s3.ticketmaster.domain.user.GetAllUsersRequest;
import nl.fontys.s3.ticketmaster.domain.user.GetAllUsersResponse;

public interface GetAllUsersUseCase {
    GetAllUsersResponse getAllUsers(GetAllUsersRequest request);
}