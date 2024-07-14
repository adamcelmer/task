package com.magnoliacms.exception;

import jakarta.ws.rs.core.Response;

public class UserAlreadyExistsException extends BusinessException {

    private static final String ERROR_MSG = "User already exists";

    public UserAlreadyExistsException() {
        super(ERROR_MSG, Response.Status.CONFLICT, ErrorResponse.simple(ERROR_MSG));
    }
}