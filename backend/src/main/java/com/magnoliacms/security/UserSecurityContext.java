package com.magnoliacms.security;

import com.magnoliacms.exception.AuthenticationException;
import com.magnoliacms.domain.user.UserEntity;
import com.magnoliacms.domain.user.UserRepository;
import jakarta.enterprise.context.RequestScoped;
import lombok.RequiredArgsConstructor;
import org.eclipse.microprofile.jwt.JsonWebToken;
import org.jboss.logging.Logger;

@RequestScoped
@RequiredArgsConstructor
public class UserSecurityContext {

    private final static Logger LOG = Logger.getLogger(UserSecurityContext.class);

    private final JsonWebToken jwt;
    private final UserRepository userRepository;

    public UserEntity getCurrentUser() {
        return userRepository.findByEmail(jwt.getClaim("upn"))
                .orElseThrow(() -> new AuthenticationException(jwt.getClaim("upn")));
    }
}
