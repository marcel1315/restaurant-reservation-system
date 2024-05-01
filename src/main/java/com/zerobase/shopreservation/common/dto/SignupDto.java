package com.zerobase.shopreservation.common.dto;

import com.zerobase.shopreservation.common.entity.Member;
import com.zerobase.shopreservation.common.type.MemberRole;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class SignupDto {
    @Email
    public String email;

    @NotBlank
    public String name;

    @NotBlank
    public String password;

    @NotBlank
    public String phone;

    public boolean partner;

    public MemberRole getRole() {
        return partner ? MemberRole.ROLE_MANAGER : MemberRole.ROLE_CUSTOMER;
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
