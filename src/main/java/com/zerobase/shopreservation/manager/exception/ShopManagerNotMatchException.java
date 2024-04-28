package com.zerobase.shopreservation.manager.exception;

import com.zerobase.shopreservation.common.exception.CustomException;
import org.springframework.http.HttpStatus;

public class ShopManagerNotMatchException extends CustomException {
    public ShopManagerNotMatchException() {
        super(HttpStatus.BAD_REQUEST, "Shop manager may not be calling own shop");
    }
}
