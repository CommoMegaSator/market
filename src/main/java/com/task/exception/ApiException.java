package com.task.exception;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import org.springframework.http.HttpStatus;

import java.time.LocalDateTime;

@Setter
@Getter
@RequiredArgsConstructor
@AllArgsConstructor
public class ApiException {
    private String errorCode;
    private String message;
    private HttpStatus httpStatus;
    private LocalDateTime dateTime;
}