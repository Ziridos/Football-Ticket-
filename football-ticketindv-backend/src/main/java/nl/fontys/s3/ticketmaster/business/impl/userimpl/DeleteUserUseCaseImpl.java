package nl.fontys.s3.ticketmaster.business.impl.userimpl;

import nl.fontys.s3.ticketmaster.business.interfaces.userinterfaces.DeleteUserUseCase;
import nl.fontys.s3.ticketmaster.persitence.UserRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@AllArgsConstructor
public class DeleteUserUseCaseImpl implements DeleteUserUseCase {
    private final UserRepository userRepository;

    @Override
    @Transactional
    public void deleteUser(long userId) {
        userRepository.deleteById(userId);
    }
}
