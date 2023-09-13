package com.yamukja.exception;

import lombok.Getter;
import org.springframework.http.HttpStatus;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;

import javax.validation.ConstraintViolation;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

public class ErrorResponse {
    @Getter
    public static class Basic {
        private final int status;
        private final String message;

        private Basic(int status, String message) {
            this.status = status;
            this.message = message;
        }

        public static ErrorResponse.Basic of(ExceptionCode exceptionCode) {
            return new ErrorResponse.Basic(exceptionCode.getStatus(), exceptionCode.getMessage());
        }

        public static ErrorResponse.Basic of(HttpStatus httpStatus) {
            return new ErrorResponse.Basic(httpStatus.value(), httpStatus.getReasonPhrase());
        }

        public static ErrorResponse.Basic of(HttpStatus httpStatus, String message) {
            return new ErrorResponse.Basic(httpStatus.value(), message);
        }
    }

    @Getter
    public static class FieldErrors {
        private final List<CustomFieldError> fieldErrors;

        private FieldErrors(final List<CustomFieldError> fieldErrors) {
            this.fieldErrors = fieldErrors;
        }

        public static ErrorResponse.FieldErrors of(BindingResult bindingResult) {
            return new ErrorResponse.FieldErrors(CustomFieldError.of(bindingResult));
        }
    }

    @Getter
    public static class ConstraintViolations {
        private final List<CustomConstraintViolation> violations;

        private ConstraintViolations(final List<CustomConstraintViolation> violations) {
            this.violations = violations;
        }

        public static ErrorResponse.ConstraintViolations of(Set<ConstraintViolation<?>> constraintViolations) {
            return new ErrorResponse.ConstraintViolations(CustomConstraintViolation.of(constraintViolations));
        }
    }

    @Getter
    public static class CustomFieldError {
        private final String field;
        private Object rejectedValue;
        private final String reason;

        private CustomFieldError(String field, Object rejectedValue, String reason) {
            this.field = field;
            this.rejectedValue = rejectedValue;
            this.reason = reason;
        }

        public static List<CustomFieldError> of(BindingResult bindingResult) {
            final List<FieldError> fieldErrors = bindingResult.getFieldErrors();

            return fieldErrors.stream()
                    .map(fieldError -> new CustomFieldError(
                            fieldError.getField(),
                            fieldError.getRejectedValue() == null ? "" : fieldError.getRejectedValue().toString(),
                            fieldError.getDefaultMessage()))
                    .peek(customFieldError -> {
                        if(customFieldError.getField().equals("password")) {
                            customFieldError.deleteRejectedValue();
                        }
                    })
                    .collect(Collectors.toList());
        }

        // password의 경우 거절된 값을 노출하지 않기 위해 사용
        private void deleteRejectedValue() {
            this.rejectedValue = "";
        }
    }

    @Getter
    public static class CustomConstraintViolation {
        private final String propertyPath;
        private final Object rejectedValue;
        private final String reason;

        private CustomConstraintViolation(String propertyPath, Object rejectedValue, String reason) {
            this.propertyPath = propertyPath;
            this.rejectedValue = rejectedValue;
            this.reason = reason;
        }

        public static List<CustomConstraintViolation> of(Set<ConstraintViolation<?>> constraintViolations) {
            return constraintViolations.stream()
                    .map(constraintViolation -> new CustomConstraintViolation(
                            constraintViolation.getPropertyPath().toString(),
                            constraintViolation.getInvalidValue().toString(),
                            constraintViolation.getMessage()
                    )).collect(Collectors.toList());
        }
    }
}
