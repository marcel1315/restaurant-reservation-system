package com.zerobase.shopreservation.customer.exception;

import com.zerobase.shopreservation.common.exception.CustomException;
import org.springframework.http.HttpStatus;

public class CantReservePastTimeException extends CustomException {
    public CantReservePastTimeException() {
        super(HttpStatus.BAD_REQUEST, "Schedule time shouldn't be past.");
    }
}
