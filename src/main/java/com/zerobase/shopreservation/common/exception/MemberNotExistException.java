package com.zerobase.shopreservation.common.exception;

import org.springframework.http.HttpStatus;

public class MemberNotExistException extends CustomException {
    public MemberNotExistException() {
        super(HttpStatus.BAD_REQUEST, "Member you're looking for is not exist. Member is identified by both email and role");
    }
}
