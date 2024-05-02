package com.zerobase.shopreservation.customer.exception;

import com.zerobase.shopreservation.common.exception.CustomException;
import org.springframework.http.HttpStatus;

public class InvalidTimeTableException extends CustomException {
    public InvalidTimeTableException() {
        super(HttpStatus.BAD_REQUEST, "Check the start time and final time. Start time must proceed final time.");
    }
}
