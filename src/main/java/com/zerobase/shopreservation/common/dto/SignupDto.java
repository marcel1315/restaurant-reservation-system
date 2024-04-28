package com.zerobase.shopreservation.common.dto;

import com.zerobase.shopreservation.common.entity.Member;
import com.zerobase.shopreservation.common.type.MemberRole;
import lombok.Data;

@Data
public class SignupDto {
    public String email;
    public String name;
    public String password;
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
