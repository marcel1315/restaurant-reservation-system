package com.zerobase.shopreservation.customer.exception;

import com.zerobase.shopreservation.common.exception.CustomException;
import org.springframework.http.HttpStatus;

public class ReviewNotExistException extends CustomException {
    public ReviewNotExistException() {
        super(HttpStatus.BAD_REQUEST, "A Review from the reviewId doesn't exist.");
    }
}
