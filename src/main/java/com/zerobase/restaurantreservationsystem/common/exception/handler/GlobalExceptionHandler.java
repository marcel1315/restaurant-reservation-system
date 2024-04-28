package com.zerobase.restaurantreservationsystem.common.exception.handler;

import com.zerobase.restaurantreservationsystem.common.exception.CustomException;
import com.zerobase.restaurantreservationsystem.manager.exception.RestaurantManagerNotMatchException;
import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.HashMap;
import java.util.Map;

@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(CustomException.class)
    public ResponseEntity<?> handler(CustomException e, HttpServletRequest request) {

        log.error("CustomException, {}, {}", request.getRequestURI(), e.getMessage());

        HttpHeaders headers = new HttpHeaders();

        Map<String, String> body = new HashMap<>();
        body.put("errorType", e.getHttpStatusType());
        body.put("message", e.getMessage());

        return new ResponseEntity<>(body, headers, e.getHttpStatusCode());
    }
}