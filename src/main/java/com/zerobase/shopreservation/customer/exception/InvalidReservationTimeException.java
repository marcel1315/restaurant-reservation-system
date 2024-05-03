package com.zerobase.shopreservation.customer.exception;

import com.zerobase.shopreservation.common.exception.CustomException;
import org.springframework.http.HttpStatus;

public class InvalidReservationTimeException extends CustomException {
    public InvalidReservationTimeException() {
        super(HttpStatus.BAD_REQUEST, "The selected time is not valid");
    }
}
