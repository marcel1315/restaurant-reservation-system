package com.zerobase.shopreservation.common.exception;

import org.springframework.http.HttpStatus;

public class ReservationNotExistException extends CustomException {
    public ReservationNotExistException() {
        super(HttpStatus.BAD_REQUEST, "No matching reservation exists");
    }
}
