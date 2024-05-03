package com.zerobase.shopreservation.customer.exception;

import com.zerobase.shopreservation.common.exception.CustomException;
import org.springframework.http.HttpStatus;

public class PassedCheckInTimeException extends CustomException {
    public PassedCheckInTimeException() {
        super(HttpStatus.BAD_REQUEST, "Your reservation has passed check-in time");
    }
}
