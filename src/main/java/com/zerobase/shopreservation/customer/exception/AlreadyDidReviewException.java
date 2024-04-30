package com.zerobase.shopreservation.customer.exception;

import com.zerobase.shopreservation.common.exception.CustomException;
import org.springframework.http.HttpStatus;

public class AlreadyDidReviewException extends CustomException {
    public AlreadyDidReviewException() {
        super(HttpStatus.BAD_REQUEST, "Customer already did write review. May consider update review");
    }
}
