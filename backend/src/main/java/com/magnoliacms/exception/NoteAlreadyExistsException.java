package com.magnoliacms.exception;

import jakarta.ws.rs.core.Response;

public class NoteAlreadyExistsException extends BusinessException {

    private static final String ERROR_MSG = "Note with title '%s' already exists";


    public NoteAlreadyExistsException(String title) {
        super(ERROR_MSG.formatted(title), Response.Status.CONFLICT, ErrorResponse.simple(ERROR_MSG.formatted(title)));
    }
}
