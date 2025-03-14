package nl.fontys.s3.ticketmaster.business.impl.login;

import lombok.RequiredArgsConstructor;
import nl.fontys.s3.ticketmaster.business.interfaces.logininterfaces.LoginUseCase;
import nl.fontys.s3.ticketmaster.persitence.UserRepository;
import nl.fontys.s3.ticketmaster.persitence.entity.UserEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class LoginUseCaseImpl implements LoginUseCase {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    @Override
    public Optional<UserEntity> authenticateUser(String email, String password) {
        Optional<UserEntity> userOptional = userRepository.findByEmail(email);

        if (userOptional.isPresent()) {
            UserEntity user = userOptional.get();
            if (passwordEncoder.matches(password, user.getPassword())) {
                return Optional.of(user);
            }
        }
        return Optional.empty();
    }

    @Override
    public Optional<UserEntity> getUserByEmail(String email) {
        return userRepository.findByEmail(email);
    }
}