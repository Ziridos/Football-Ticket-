package nl.fontys.s3.ticketmaster.business.impl.userimpl;

import nl.fontys.s3.ticketmaster.business.interfaces.userinterfaces.GetUserUseCase;
import nl.fontys.s3.ticketmaster.domain.user.User;
import nl.fontys.s3.ticketmaster.persitence.UserRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
@AllArgsConstructor
public class GetUserUseCaseImpl implements GetUserUseCase {

    private final UserRepository userRepository;
    private final UserConverter userConverter;

    @Override
    @Transactional
    public Optional<User> getUser(Long id) {
        return userRepository.findById(id)
                .map(userConverter::convert);
    }
}