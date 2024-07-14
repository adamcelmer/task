package com.magnoliacms.security;

import com.magnoliacms.domain.user.UserDto;
import com.magnoliacms.domain.user.UserService;
import io.smallrye.jwt.build.Jwt;
import jakarta.annotation.security.PermitAll;
import jakarta.validation.Validator;
import jakarta.ws.rs.Consumes;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import lombok.RequiredArgsConstructor;
import org.eclipse.microprofile.config.inject.ConfigProperty;
import org.jboss.logging.Logger;

import java.util.Set;

@RequiredArgsConstructor
@Path("/auth")
public class AuthResource {

    private static final Logger LOG = Logger.getLogger(AuthResource.class);
    private final UserService userService;
    private final Validator validator;

    @ConfigProperty(name = "quarkus.jwt.verify.issuer")
    String jwtIssuer;

    @POST
    @PermitAll
    @Consumes(MediaType.MULTIPART_FORM_DATA)
    @Path("register")
    public Response register(UserDto userDto) {
        validator.validate(userDto);
        userService.registerUser(userDto);
        return Response.status(201)
                .build();
    }

    @POST
    @PermitAll
    @Consumes(MediaType.MULTIPART_FORM_DATA)
    @Produces(MediaType.APPLICATION_JSON)
    @Path("login")
    public JWTTokenResponse login(UserDto authRequest) {
        userService.authenticate(authRequest);
        String token = Jwt.issuer(jwtIssuer)
                .upn(authRequest.getEmail())
                .groups(Set.of("User"))
                .sign();
        return new JWTTokenResponse(token);
    }
}

