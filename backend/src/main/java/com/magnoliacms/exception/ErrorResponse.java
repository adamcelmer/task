package com.magnoliacms.exception;

import jakarta.validation.ConstraintViolation;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class ErrorResponse {

    List<String> errors = new ArrayList<>();

    public static ErrorResponse simple(String message) {
        return new ErrorResponse(List.of(message));
    }

    public static ErrorResponse fromConstraintViolations(Set<ConstraintViolation<?>> constraintViolations) {
        List<String> errors = new ArrayList<>();
        constraintViolations.forEach(violation -> errors.add(violation.getMessage()));
        return new ErrorResponse(errors);
    }
}

