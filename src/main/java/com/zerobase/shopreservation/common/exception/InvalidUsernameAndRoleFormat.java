package com.zerobase.shopreservation.common.exception;

import org.springframework.http.HttpStatus;

public class InvalidUsernameAndRoleFormat extends CustomException {
    public InvalidUsernameAndRoleFormat() {
        super(HttpStatus.INTERNAL_SERVER_ERROR, "Username and role are concatenated by space. Both are needed to identify a member");
    }
}
