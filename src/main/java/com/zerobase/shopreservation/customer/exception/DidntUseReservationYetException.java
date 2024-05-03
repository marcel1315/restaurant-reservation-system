package com.zerobase.shopreservation.customer.exception;

import com.zerobase.shopreservation.common.exception.CustomException;
import org.springframework.http.HttpStatus;

public class DidntUseReservationYetException extends CustomException {
    public DidntUseReservationYetException() {
        super(HttpStatus.BAD_REQUEST, "You didn't use the reservation yet.");
    }
}
