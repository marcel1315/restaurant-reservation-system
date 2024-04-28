package com.zerobase.shopreservation.common.exception;

import org.springframework.http.HttpStatus;

public class ShopNotExistException extends CustomException {
    public ShopNotExistException() {
        super(HttpStatus.BAD_REQUEST, "The shop you lookig for is not there");
    }
}
