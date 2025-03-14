package nl.fontys.s3.ticketmaster.business.impl.userimpl;

import nl.fontys.s3.ticketmaster.business.exception.EmailAlreadyExistsException;
import nl.fontys.s3.ticketmaster.business.exception.UsernameAlreadyExistsException;
import nl.fontys.s3.ticketmaster.business.exception.InvalidUserException;
import nl.fontys.s3.ticketmaster.business.interfaces.userinterfaces.UserValidator;
import nl.fontys.s3.ticketmaster.domain.user.CreateUserRequest;
import nl.fontys.s3.ticketmaster.domain.user.RegisterUserRequest;
import nl.fontys.s3.ticketmaster.persitence.UserRepository;
import nl.fontys.s3.ticketmaster.domain.user.UpdateUserRequest;
import org.springframework.stereotype.Service;

@Service
public class UserValidatorImpl implements UserValidator {
    private final UserRepository userRepository;
    private static final String EMAIL_REGEX = "^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,}$";

    // Error message constants
    private static final String ERROR_ROLE_REQUIRED = "ROLE_REQUIRED";
    private static final String ERROR_USER_ID_INVALID = "USER_ID_INVALID";
    private static final String ERROR_USER_NOT_FOUND = "USER_NOT_FOUND";
    private static final String ERROR_REQUEST_NULL = "REQUEST_IS_NULL";
    private static final String ERROR_NAME_REQUIRED = "NAME_REQUIRED";
    private static final String ERROR_EMAIL_REQUIRED = "EMAIL_REQUIRED";
    private static final String ERROR_PASSWORD_REQUIRED = "PASSWORD_REQUIRED";
    private static final String ERROR_ADDRESS_REQUIRED = "ADDRESS_REQUIRED";
    private static final String ERROR_PHONE_REQUIRED = "PHONE_REQUIRED";
    private static final String ERROR_COUNTRY_REQUIRED = "COUNTRY_REQUIRED";
    private static final String ERROR_CITY_REQUIRED = "CITY_REQUIRED";
    private static final String ERROR_POSTAL_CODE_REQUIRED = "POSTAL_CODE_REQUIRED";
    private static final String ERROR_INVALID_EMAIL_FORMAT = "INVALID_EMAIL_FORMAT";

    public UserValidatorImpl(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public void validateUniqueUsername(String username) throws UsernameAlreadyExistsException {
        if (userRepository.existsByName(username)) {
            throw new UsernameAlreadyExistsException();
        }
    }

    private void validateField(String field, String errorMessage) throws InvalidUserException {
        if (field == null || field.trim().isEmpty()) {
            throw new InvalidUserException(errorMessage);
        }
    }

    @Override
    public void validateCreateUserRequest(CreateUserRequest request) throws InvalidUserException {
        if (request == null) {
            throw new InvalidUserException(ERROR_REQUEST_NULL);
        }
        validateCommonFields(request);
    }

    @Override
    public void validateRegisterRequest(RegisterUserRequest request) throws InvalidUserException {
        if (request == null) {
            throw new InvalidUserException(ERROR_REQUEST_NULL);
        }
        validateCommonFields(request);
    }

    @Override
    public void validateUpdateUserRequestFields(UpdateUserRequest request) throws InvalidUserException {
        validateUserIdExists(request);
        validateCommonFields(request);
    }

    private void validateCommonFields(Object request) throws InvalidUserException {
        UserValidationData data = extractValidationData(request);
        validateUserFields(data);
    }

    private UserValidationData extractValidationData(Object request) throws InvalidUserException {
        if (request instanceof CreateUserRequest createRequest) {
            return new UserValidationData(
                    createRequest.getName(),
                    createRequest.getEmail(),
                    createRequest.getPassword(),
                    createRequest.getAddress(),
                    createRequest.getPhone(),
                    createRequest.getCountry(),
                    createRequest.getCity(),
                    createRequest.getPostalCode(),
                    createRequest.getRole(),
                    true // CreateUserRequest requires role
            );
        } else if (request instanceof RegisterUserRequest registerRequest) {
            return new UserValidationData(
                    registerRequest.getName(),
                    registerRequest.getEmail(),
                    registerRequest.getPassword(),
                    registerRequest.getAddress(),
                    registerRequest.getPhone(),
                    registerRequest.getCountry(),
                    registerRequest.getCity(),
                    registerRequest.getPostalCode(),
                    null,
                    false // RegisterUserRequest doesn't require role
            );
        } else if (request instanceof UpdateUserRequest updateRequest) {
            return new UserValidationData(
                    updateRequest.getName(),
                    updateRequest.getEmail(),
                    updateRequest.getPassword(),
                    updateRequest.getAddress(),
                    updateRequest.getPhone(),
                    updateRequest.getCountry(),
                    updateRequest.getCity(),
                    updateRequest.getPostalCode(),
                    updateRequest.getRole(),
                    true // UpdateUserRequest requires role
            );
        }
        throw new InvalidUserException(ERROR_REQUEST_NULL);
    }

    private void validateUserFields(UserValidationData data) throws InvalidUserException {
        validateField(data.name(), ERROR_NAME_REQUIRED);
        validateEmail(data.email());
        validateField(data.password(), ERROR_PASSWORD_REQUIRED);
        validateField(data.address(), ERROR_ADDRESS_REQUIRED);
        validateField(data.phone(), ERROR_PHONE_REQUIRED);
        validateField(data.country(), ERROR_COUNTRY_REQUIRED);
        validateField(data.city(), ERROR_CITY_REQUIRED);
        validateField(data.postalCode(), ERROR_POSTAL_CODE_REQUIRED);

        // Only check role if it's required (for CreateUserRequest and UpdateUserRequest)
        if (data.roleRequired() && data.role() == null) {
            throw new InvalidUserException(ERROR_ROLE_REQUIRED);
        }
    }

    private void validateEmail(String email) throws InvalidUserException {
        if (email == null || email.trim().isEmpty()) {
            throw new InvalidUserException(ERROR_EMAIL_REQUIRED);
        }
        if (!email.matches(EMAIL_REGEX)) {
            throw new InvalidUserException(ERROR_INVALID_EMAIL_FORMAT);
        }
    }

    @Override
    public void validateUniqueEmail(String email) throws InvalidUserException {
        if (userRepository.existsByEmail(email)) {
            throw new EmailAlreadyExistsException();
        }
    }

    @Override
    public void validateUpdateUserRequest(UpdateUserRequest request) throws InvalidUserException {
        validateUserIdExists(request);
    }

    @Override
    public void validateUserExists(Long userId) throws InvalidUserException {
        if (userId == null) {
            throw new InvalidUserException(ERROR_USER_ID_INVALID);
        }
        if (!userRepository.existsById(userId)) {
            throw new InvalidUserException(ERROR_USER_NOT_FOUND);
        }
    }

    private void validateUserIdExists(UpdateUserRequest request) throws InvalidUserException {
        if (request == null || request.getId() == null) {
            throw new InvalidUserException(ERROR_USER_ID_INVALID);
        }
        if (!userRepository.existsById(request.getId())) {
            throw new InvalidUserException(ERROR_USER_NOT_FOUND);
        }
    }

    // Record to handle multiple parameters
    private record UserValidationData(
            String name,
            String email,
            String password,
            String address,
            String phone,
            String country,
            String city,
            String postalCode,
            Object role,
            boolean roleRequired
    ) {}
}