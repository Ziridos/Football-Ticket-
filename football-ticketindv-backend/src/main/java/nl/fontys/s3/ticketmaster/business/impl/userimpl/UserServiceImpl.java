package nl.fontys.s3.ticketmaster.business.impl.userimpl;

import lombok.AllArgsConstructor;
import nl.fontys.s3.ticketmaster.business.exception.InvalidUserException;
import nl.fontys.s3.ticketmaster.business.interfaces.userinterfaces.UserService;
import nl.fontys.s3.ticketmaster.persitence.UserRepository;
import nl.fontys.s3.ticketmaster.persitence.entity.UserEntity;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class UserServiceImpl implements UserService {
    private final UserRepository userRepository;

    @Override
    public UserEntity getUserById(Long userId) {
        return userRepository.findById(userId)
                .orElseThrow(() -> new InvalidUserException("User not found"));
    }

    @Override
    public UserEntity saveUser(UserEntity user) {
        try {
            return userRepository.save(user);
        } catch (Exception e) {
            throw new InvalidUserException("Error occurred while saving user to database");
        }
    }
}