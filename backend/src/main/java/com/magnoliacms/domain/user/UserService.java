package com.magnoliacms.domain.user;

import com.magnoliacms.exception.AuthenticationException;
import com.magnoliacms.exception.UserAlreadyExistsException;
import com.magnoliacms.security.PasswordHash;
import com.magnoliacms.security.PasswordHashCalculator;
import jakarta.enterprise.context.ApplicationScoped;
import lombok.RequiredArgsConstructor;
import org.jboss.logging.Logger;

import java.util.List;
import java.util.UUID;

@RequiredArgsConstructor
@ApplicationScoped
public class UserService {

    private static final Logger LOG = Logger.getLogger(UserService.class);

    private final UserRepository userRepository;

    public void registerUser(UserDto userDto) {
        userRepository.findByEmail(userDto.getEmail())
                .ifPresent(user -> {
                    throw new UserAlreadyExistsException();
                });
        PasswordHash hash = PasswordHashCalculator.calculateHash(userDto.getPassword());
        UserEntity user = UserEntity.builder()
                .id(UUID.randomUUID().toString())
                .email(userDto.getEmail())
                .password(hash.passwordHash())
                .salt(hash.salt())
                .build();
        userRepository.save(user);
    }

    public void authenticate(UserDto userDto) {
        userRepository.findByEmail(userDto.getEmail())
                .ifPresentOrElse(user -> {
                    var hash = PasswordHashCalculator.calculateHash(userDto.getPassword(), user.getSalt());
                    if (!user.getPassword().equals(hash.passwordHash())) {
                        throw new AuthenticationException(userDto.getEmail());
                    }
                }, () -> {
                    throw new AuthenticationException();
                });
    }

    public List<UserDto> getAllUsers() {
        return List.of(new UserDto());
        //TODO
    }
}