package com.zerobase.shopreservation.customer.exception;

import com.zerobase.shopreservation.common.exception.CustomException;
import org.springframework.http.HttpStatus;

public class DidntCheckInException extends CustomException {
    public DidntCheckInException() {
        super(HttpStatus.BAD_REQUEST, "Check-in isn't done with this reservation");
    }
}
