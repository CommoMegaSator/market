package com.task.exception;

import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.exc.InvalidFormatException;
import com.task.dto.ExceptionResponseData;
import jakarta.persistence.EntityExistsException;
import jakarta.persistence.EntityNotFoundException;
import jakarta.validation.ValidationException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.web.firewall.RequestRejectedException;
import org.springframework.web.HttpMediaTypeNotSupportedException;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.ServletRequestBindingException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.time.LocalDateTime;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@RestControllerAdvice
@RequestMapping(value = "/error", produces = MediaType.APPLICATION_JSON_VALUE)
public class GlobalExceptionHandler {

    private static final Logger logger = LoggerFactory.getLogger(GlobalExceptionHandler.class);

    @ExceptionHandler({IllegalStateException.class, IllegalArgumentException.class, ValidationException.class,
            ServletRequestBindingException.class, HttpMessageNotReadableException.class, RequestRejectedException.class})
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ExceptionResponseData handle(Exception exception) {
        return ExceptionResponseData.builder().errorCode("incorrectRequestData").errorMessage(exception.getMessage()).build();
    }

    @ExceptionHandler({HttpRequestMethodNotSupportedException.class})
    @ResponseStatus(HttpStatus.METHOD_NOT_ALLOWED)
    public ExceptionResponseData handleMethodNotSupported(HttpRequestMethodNotSupportedException exception) {
        return ExceptionResponseData.builder().errorCode("methodNotSupported")
                .errorMessage(exception.getMessage())
                .build();
    }

    @ExceptionHandler({MethodArgumentNotValidException.class})
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ExceptionResponseData handleMethodArgumentNotValid(MethodArgumentNotValidException exception) {
        return ExceptionResponseData.builder().errorCode("incorrectRequestData")
                .errorMessage(exception.getFieldErrors().stream()
                        .map(fieldError -> fieldError.getField() + " " + fieldError.getDefaultMessage())
                        .collect(Collectors.joining(", ")))
                .build();
    }

    @ExceptionHandler(BadCredentialsException.class)
    @ResponseStatus(HttpStatus.UNAUTHORIZED)
    public ExceptionResponseData handleBadCredentialsException(BadCredentialsException exception) {
        return ExceptionResponseData.builder().errorCode("badCredentials").errorMessage(exception.getMessage()).build();
    }

    @ExceptionHandler(EntityNotFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public ExceptionResponseData handleUserNotFoundException(EntityNotFoundException exception) {
        return ExceptionResponseData.builder().errorCode("entityNotFoundException").errorMessage(exception.getMessage()).build();
    }

    @ExceptionHandler(UsernameNotFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public ExceptionResponseData handleUserNotFoundException(UsernameNotFoundException exception) {
        return ExceptionResponseData.builder().errorCode("userNotFound").errorMessage(exception.getMessage()).build();
    }

    @ExceptionHandler(InvalidFormatException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ExceptionResponseData handle(InvalidFormatException exception) {
        StringBuilder errorMessage = new StringBuilder("Bad value '")
                .append(exception.getValue() != null ? exception.getValue().toString() : "null").append("' for field '")
                .append(exception.getPath().stream().map(JsonMappingException.Reference::getFieldName).collect(Collectors.joining("'.")))
                .append(".");
        if (exception.getTargetType().isEnum()) {
            errorMessage.append(" Expected one of these values: ")
                    .append(Stream.of(exception.getTargetType().getEnumConstants()).map(v -> ((Enum<?>) v).name())
                            .collect(Collectors.joining(", ")));
        }
        return ExceptionResponseData.builder().errorCode("incorrectRequestData").errorMessage(errorMessage.toString()).build();
    }

    @ExceptionHandler(HttpMediaTypeNotSupportedException.class)
    @ResponseStatus(HttpStatus.UNSUPPORTED_MEDIA_TYPE)
    public void handleResponseStatusException(HttpMediaTypeNotSupportedException exception) {
    }

    @ExceptionHandler(AccessDeniedException.class)
    @ResponseStatus(HttpStatus.FORBIDDEN)
    public ExceptionResponseData handleResponseStatusException(AccessDeniedException exception) {
        return ExceptionResponseData.builder().errorCode("forbidden").errorMessage(exception.getMessage()).build();
    }

    @ExceptionHandler(EntityExistsException.class)
    @ResponseStatus(HttpStatus.CONFLICT)
    public ExceptionResponseData handleAuthenticationException(Exception exception) {
        return ExceptionResponseData.builder().errorCode("userExists").errorMessage(exception.getMessage()).build();
    }

    @ExceptionHandler(value = {ServiceException.class})
    public ResponseEntity<Object> handleServiceException(ServiceException exception) {
        ApiException apiException = new ApiException(
                exception.getErrorCode(),
                exception.getMessage(),
                exception.getHttpStatus(),
                LocalDateTime.now()
        );
        return new ResponseEntity<>(apiException, exception.getHttpStatus());
    }
}