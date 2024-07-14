package com.magnoliacms.exception;

import jakarta.ws.rs.core.Response;
import lombok.Getter;
import lombok.Setter;

import static jakarta.ws.rs.core.Response.Status.BAD_REQUEST;

@Getter
@Setter
public class BusinessException extends RuntimeException {

    private final Response.Status status;
    private final ErrorResponse response;
    public BusinessException(String message, Response.Status status, ErrorResponse response) {
        super(message);
        this.status = status;
        this.response = response;
    }

    public static BusinessException badRequest(String message) {
        return new BusinessException(message, BAD_REQUEST, ErrorResponse.simple(message));
    }
}
