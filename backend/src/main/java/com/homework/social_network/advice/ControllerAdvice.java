package com.homework.social_network.advice;

import com.homework.social_network.exception.CustomException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import java.util.LinkedHashMap;
import java.util.Map;

@org.springframework.web.bind.annotation.ControllerAdvice
@Slf4j
public class ControllerAdvice extends ResponseEntityExceptionHandler {

    @ExceptionHandler(CustomException.class)
    public ResponseEntity<Object> handleCityNotFoundException(CustomException e, WebRequest request) {

        Map<String, Object> body = new LinkedHashMap<>();
        body.put("message", e.getMessage());
        body.put("code", e.getCode());
        log.error("Error: {}", e.getMessage(), e);
        return new ResponseEntity<>(body, HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<Object> handleCityNotFoundException(Exception e, WebRequest request) {

        Map<String, Object> body = new LinkedHashMap<>();
        body.put("message", e.getMessage());
        log.error("Error: {}", e.getMessage(), e);
        return new ResponseEntity<>(body, HttpStatus.INTERNAL_SERVER_ERROR);
    }
}
