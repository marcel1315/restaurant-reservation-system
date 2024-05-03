package com.zerobase.shopreservation.customer.exception;

import com.zerobase.shopreservation.common.exception.CustomException;
import com.zerobase.shopreservation.common.type.ApprovalState;
import org.springframework.http.HttpStatus;

public class ReservationNotAcceptedException extends CustomException {
    public ReservationNotAcceptedException(ApprovalState currentState) {
        super(HttpStatus.BAD_REQUEST,
                String.format("Reservation is not accepted. Current state is %s", currentState.toString()));
    }
}
