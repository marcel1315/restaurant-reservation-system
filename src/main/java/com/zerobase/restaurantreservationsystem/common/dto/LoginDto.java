package com.zerobase.restaurantreservationsystem.common.dto;

import lombok.Data;

@Data
public class LoginDto {
    public String username;
    public String password;
    public boolean partner;

    public String getRole() {
        return partner ? "ROLE_MANAGER" : "ROLE_CUSTOMER";
    }
}
