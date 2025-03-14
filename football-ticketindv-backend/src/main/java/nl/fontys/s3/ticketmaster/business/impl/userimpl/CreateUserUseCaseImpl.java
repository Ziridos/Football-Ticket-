package nl.fontys.s3.ticketmaster.business.impl.userimpl;

import nl.fontys.s3.ticketmaster.business.exception.EmailAlreadyExistsException;
import nl.fontys.s3.ticketmaster.business.exception.InvalidUserException;
import nl.fontys.s3.ticketmaster.business.interfaces.userinterfaces.CreateUserUseCase;
import nl.fontys.s3.ticketmaster.business.exception.UsernameAlreadyExistsException;
import nl.fontys.s3.ticketmaster.business.interfaces.userinterfaces.UserValidator;
import nl.fontys.s3.ticketmaster.business.interfaces.userinterfaces.UserService;
import nl.fontys.s3.ticketmaster.domain.user.CreateUserRequest;
import nl.fontys.s3.ticketmaster.domain.user.CreateUserResponse;
import nl.fontys.s3.ticketmaster.persitence.entity.UserEntity;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class CreateUserUseCaseImpl implements CreateUserUseCase {
    private final UserService userService;
    private final UserValidator userValidator;
    private final UserConverter userConverter;
    private final PasswordEncoder passwordEncoder;

    @Override
    @Transactional
    public CreateUserResponse createUser(CreateUserRequest request) {
        try {
            userValidator.validateCreateUserRequest(request);
            userValidator.validateUniqueUsername(request.getName());
            userValidator.validateUniqueEmail(request.getEmail());

            String encryptedPassword = passwordEncoder.encode(request.getPassword());
            request.setPassword(encryptedPassword);

            UserEntity newUser = userConverter.convertToEntity(request);
            UserEntity savedUser = userService.saveUser(newUser);

            return userConverter.convertToCreateUserResponse(savedUser);
        } catch (UsernameAlreadyExistsException | EmailAlreadyExistsException e) {
            throw e;
        } catch (Exception e) {
            throw new InvalidUserException("Error occurred while creating user");
        }
    }
}