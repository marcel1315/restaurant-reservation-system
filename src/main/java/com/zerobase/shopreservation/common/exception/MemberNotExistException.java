package com.zerobase.shopreservation.common.exception;

import org.springframework.http.HttpStatus;

public class MemberNotExistException extends CustomException {
    public MemberNotExistException() {
        super(HttpStatus.BAD_REQUEST, "Member you're looking for is not exist. Member is identified by both email and role");
    }

    public MemberNotExistException(String username, String role) {
        super(HttpStatus.BAD_REQUEST,
                String.format("Username: %s, Role: %s, Member you're looking for is not exist. Member is identified by both email and role", username, role)
        );
    }
}
