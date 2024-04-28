package com.zerobase.restaurantreservationsystem.common.exception;

import org.springframework.http.HttpStatus;

public class MemberAlreadyExistException extends CustomException {
    public MemberAlreadyExistException() {
        super(HttpStatus.BAD_REQUEST, "The member that has the email and role is already exist");
    }
}
