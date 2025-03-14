package nl.fontys.s3.ticketmaster.configuration.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.PermissionEvaluator;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;
import nl.fontys.s3.ticketmaster.business.interfaces.userinterfaces.GetUserUseCase;
import nl.fontys.s3.ticketmaster.domain.user.User;

import java.io.Serializable;
import java.util.Optional;

@Component
public class CustomPermissionEvaluator implements PermissionEvaluator {
    private final GetUserUseCase getUserUseCase;

    @Autowired
    public CustomPermissionEvaluator(GetUserUseCase getUserUseCase) {
        this.getUserUseCase = getUserUseCase;
    }

    @Override
    public boolean hasPermission(Authentication authentication, Object targetDomainObject, Object permission) {
        if (authentication == null || targetDomainObject == null) {
            return false;
        }

        try {
            long userId = Long.parseLong(targetDomainObject.toString());
            Optional<User> userToAccess = getUserUseCase.getUser(userId);

            return userToAccess
                    .map(u -> u.getEmail().equals(authentication.getName()))
                    .orElse(false);
        } catch (Exception e) {
            return false;
        }
    }

    @Override
    public boolean hasPermission(Authentication authentication, Serializable targetId, String targetType, Object permission) {
        return false;
    }
}