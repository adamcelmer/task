package com.magnoliacms.exception;

import jakarta.ws.rs.core.Response;

public class AuthenticationException extends BusinessException {

    private static final String ERROR_MSG = "Invalid password provided";

    public AuthenticationException() {
        super(ERROR_MSG, Response.Status.UNAUTHORIZED, ErrorResponse.simple(ERROR_MSG));
    }

    public AuthenticationException(String email) {
        super(ERROR_MSG, Response.Status.UNAUTHORIZED, ErrorResponse.simple(ERROR_MSG));
    }
}
