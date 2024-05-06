package com.zerobase.shopreservation.common.dto;

import com.zerobase.shopreservation.common.entity.Member;
import com.zerobase.shopreservation.common.type.MemberRole;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.Builder;
import lombok.Data;

import static io.swagger.v3.oas.annotations.media.Schema.RequiredMode.REQUIRED;

@Data
@Builder
public class SignupDto {
    @Email
    @NotBlank
    @Schema(example = "user@example.com", requiredMode = REQUIRED)
    public String email;

    @NotBlank
    @Schema(example = "이영수")
    public String name;

    @NotBlank
    @Schema(example = "my-secret-password")
    public String password;

    @NotBlank
    @Schema(example = "010-1111-2222")
    public String phone;

    @Schema(example = "false", requiredMode = REQUIRED, defaultValue = "false")
    public boolean partner;

    @Schema(hidden = true)
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
