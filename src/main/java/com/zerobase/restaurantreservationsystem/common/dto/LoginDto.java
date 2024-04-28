package com.zerobase.restaurantreservationsystem.common.dto;

import com.zerobase.restaurantreservationsystem.common.type.MemberRole;
import lombok.Data;

@Data
public class LoginDto {
    public String username;
    public String password;
    public boolean partner;

    public MemberRole getRole() {
        return partner ? MemberRole.ROLE_MANAGER : MemberRole.ROLE_CUSTOMER;
    }

    public String getRoleString() {
        return getRole().toString();
    }
}
