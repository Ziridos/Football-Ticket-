package nl.fontys.s3.ticketmaster.business.impl.userimpl;

import lombok.AllArgsConstructor;
import nl.fontys.s3.ticketmaster.business.interfaces.userinterfaces.UpdateUserUseCase;
import nl.fontys.s3.ticketmaster.business.exception.InvalidUserException;
import nl.fontys.s3.ticketmaster.business.interfaces.userinterfaces.UserValidator;
import nl.fontys.s3.ticketmaster.persitence.UserRepository;
import nl.fontys.s3.ticketmaster.persitence.entity.UserEntity;
import nl.fontys.s3.ticketmaster.domain.user.UpdateUserRequest;
import nl.fontys.s3.ticketmaster.domain.user.UpdateUserResponse;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


@Service
@AllArgsConstructor
public class UpdateUserUseCaseImpl implements UpdateUserUseCase {
    private final UserRepository userRepository;
    private final UserConverter userConverter;
    private final UserValidator userValidator;



    @Override
    @Transactional
    public UpdateUserResponse updateUser(UpdateUserRequest request) {
        if (request == null) {
            throw new InvalidUserException("USER_REQUEST_IS_NULL");
        }

        userValidator.validateUpdateUserRequestFields(request);
        userValidator.validateUpdateUserRequest(request);


        UserEntity user = userRepository.findById(request.getId())
                .orElseThrow(() -> new InvalidUserException("USER_NOT_FOUND"));

        UserEntity updatedUser = userConverter.updateEntityFromRequest(user, request);
        UserEntity savedUser = userRepository.save(updatedUser);

        return userConverter.convertToUpdateUserResponse(savedUser);
    }

}