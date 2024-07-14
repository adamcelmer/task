package com.magnoliacms.exception;

import jakarta.ws.rs.NotFoundException;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.ext.ExceptionMapper;
import jakarta.ws.rs.ext.Provider;
import org.jboss.logging.Logger;

@Provider
public class GlobalExceptionMapper implements ExceptionMapper<Exception> {

    private static final Logger LOG = Logger.getLogger(GlobalExceptionMapper.class);

    @Override
    public Response toResponse(Exception exception) {
        if (exception instanceof BusinessException) {
            LOG.warn("Handling %s: %s".formatted(exception.getClass().getName(), exception.getMessage()));
            return Response
                    .status(((BusinessException) exception).getStatus())
                    .entity(((BusinessException) exception).getResponse())
                    .build();
        }
        if (exception instanceof NotFoundException) {
            LOG.warn("Handling %s: %s".formatted(exception.getClass().getName(), exception.getMessage()));
            return Response
                    .status(((NotFoundException) exception).getResponse().getStatus())
                    .entity(((NotFoundException) exception).getResponse().getEntity())
                    .build();
        }
        exception.printStackTrace();
        return Response
                .status(500)
                .entity("Internal server error")
                .build();
    }
}
