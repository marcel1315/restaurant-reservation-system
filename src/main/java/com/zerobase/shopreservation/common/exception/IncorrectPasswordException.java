package com.zerobase.shopreservation.common.exception;

import org.springframework.http.HttpStatus;

public class IncorrectPasswordException extends CustomException {
    public IncorrectPasswordException() {
        super(HttpStatus.BAD_REQUEST, "Password is incorrect");
    }
}
