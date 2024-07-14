package com.magnoliacms.exception;

import jakarta.ws.rs.core.Response;

public class NotFoundException extends BusinessException {

    private static final String ERROR_MSG = "%s with id=%s not found ";

    private NotFoundException(String entityName, String id) {
        super(ERROR_MSG.formatted(entityName, id), Response.Status.NOT_FOUND, ErrorResponse.simple(ERROR_MSG.formatted(entityName, id)));
    }

    private NotFoundException(String message) {
        super(message, Response.Status.NOT_FOUND, ErrorResponse.simple(message));
    }

    public static NotFoundException noteNotFound(String id) {
        return new NotFoundException("Note", id);
    }

    public static NotFoundException noteVersionNotFound(String id) {
        return new NotFoundException("Note version", id);
    }

    public static NotFoundException noteVersionNotFoundForNoteId(String noteId, String noteVersionId) {
        return new NotFoundException("Note version with id=%s not found for noteId=%s=".formatted(noteId, noteVersionId));
    }
}
