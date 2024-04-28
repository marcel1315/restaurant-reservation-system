package com.zerobase.restaurantreservationsystem.common.exception;

import org.springframework.http.HttpStatus;

public class RestaurantNotExistException extends CustomException {
    public RestaurantNotExistException() {
        super(HttpStatus.BAD_REQUEST, "The restaurant you lookig for is not there");
    }
}
