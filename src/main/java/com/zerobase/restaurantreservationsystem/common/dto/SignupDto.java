package com.zerobase.restaurantreservationsystem.common.dto;

import com.zerobase.restaurantreservationsystem.common.entity.Member;
import lombok.Data;

@Data
public class SignupDto {
    public String email;
    public String name;
    public String password;
    public String phone;
    public boolean partner;

    public String getRole() {
        return partner ? "ROLE_MANAGER" : "ROLE_CUSTOMER";
    }

    public Member toMember() {
        return Member.builder()
                .email(getEmail())
                .phone(getPhone())
                .name(getName())
                .role(getRole())
                .password(getPassword())
                .build();
    }
}
