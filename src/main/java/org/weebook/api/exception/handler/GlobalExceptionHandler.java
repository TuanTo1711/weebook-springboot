package org.weebook.api.exception.handler;


import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.weebook.api.exception.ErrorMessages;
import org.weebook.api.exception.ValidationError;
import org.weebook.api.exception.error.InsufficientFundsException;
import org.weebook.api.web.response.ErrorResponse;
import org.weebook.api.web.response.JwtResponse;
import org.weebook.api.web.response.ResultResponse;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@RestControllerAdvice
@Slf4j
public class GlobalExceptionHandler {
    public static final String DEFAULT_INTERNAL_SERVER_ERROR_MESSAGE = ErrorMessages.DEFAULT_INTERNAL_SERVER_ERROR;

    @ExceptionHandler(MethodArgumentNotValidException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ErrorResponse<List<ValidationError>> handleValidationException(MethodArgumentNotValidException ex) {
        List<ValidationError> errors = new ArrayList<>();
        ex.getBindingResult().getFieldErrors()
                .forEach(fieldError -> {
                    ValidationError validationError = new ValidationError();
                    validationError.setReference(fieldError.getField());
                    validationError.setMessage(fieldError.getDefaultMessage());
                    errors.add(validationError);
                });
        return new ErrorResponse<>(HttpStatus.BAD_REQUEST.value(), "Validation Error ", errors);
    }

    @ExceptionHandler({Exception.class, Throwable.class})
    public ResultResponse<JwtResponse> internalExceptionHandler(Exception ex) {
        return new ResultResponse<> (HttpStatus.INTERNAL_SERVER_ERROR.value(),
                Objects.nonNull(ex.getLocalizedMessage()) ? ex.getLocalizedMessage() : ErrorMessages.DEFAULT_INTERNAL_SERVER_ERROR,
                null);
    }

    @ExceptionHandler(UsernameNotFoundException.class)
    public ResultResponse<JwtResponse> userNotFoundException(Exception ex) {
        return new ResultResponse<>(HttpStatus.NOT_FOUND.value(), ex.getMessage(), null);
    }

    @ExceptionHandler(InsufficientFundsException.class)
    public ResultResponse<JwtResponse> insufficientFundsException(Exception ex) {
        log.error(ex.getLocalizedMessage(), ex);
        return new ResultResponse<>(HttpStatus.BAD_REQUEST.value(),
                Objects.nonNull(ex.getLocalizedMessage()) ? ex.getLocalizedMessage() : ErrorMessages.INSUFFICIENT_FUNDS_ERROR,
                null);
    }
}
