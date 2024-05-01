package com.zerobase.shopreservation.common.dto;

import com.zerobase.shopreservation.common.type.MemberRole;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class LoginDto {
    @Email
    @NotBlank
    public String email;

    @NotBlank
    public String password;

    public boolean partner;

    public MemberRole getRole() {
        return partner ? MemberRole.ROLE_MANAGER : MemberRole.ROLE_CUSTOMER;
    }

    public String getRoleString() {
        return getRole().toString();
    }
}
