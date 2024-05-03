package com.zerobase.shopreservation.customer.exception;

import com.zerobase.shopreservation.common.exception.CustomException;
import org.springframework.http.HttpStatus;

public class ReviewCustomerNotMatchException extends CustomException {
    public ReviewCustomerNotMatchException() {
        super(HttpStatus.BAD_REQUEST, "The review is not from the same customer.");
    }
}
