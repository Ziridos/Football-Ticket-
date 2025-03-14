package nl.fontys.s3.ticketmaster.business.interfaces.userinterfaces;

import nl.fontys.s3.ticketmaster.business.exception.EmailAlreadyExistsException;
import nl.fontys.s3.ticketmaster.business.exception.InvalidUserException;
import nl.fontys.s3.ticketmaster.business.exception.UsernameAlreadyExistsException;
import nl.fontys.s3.ticketmaster.domain.user.CreateUserRequest;
import nl.fontys.s3.ticketmaster.domain.user.RegisterUserRequest;
import nl.fontys.s3.ticketmaster.domain.user.UpdateUserRequest;

public interface UserValidator {
    void validateUniqueUsername(String username) throws UsernameAlreadyExistsException;
    void validateUpdateUserRequest(UpdateUserRequest request) throws InvalidUserException;
    void validateUserExists(Long userId);
    void validateUniqueEmail(String email) throws EmailAlreadyExistsException;
    void validateCreateUserRequest(CreateUserRequest request);
    void validateRegisterRequest(RegisterUserRequest request);
    void validateUpdateUserRequestFields(UpdateUserRequest request);

}
