package com.zerobase.shopreservation.common.dto;

import com.zerobase.shopreservation.common.type.MemberRole;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.Builder;
import lombok.Data;

import static io.swagger.v3.oas.annotations.media.Schema.RequiredMode.NOT_REQUIRED;
import static io.swagger.v3.oas.annotations.media.Schema.RequiredMode.REQUIRED;

@Data
@Builder
public class LoginDto {
    @Email
    @NotBlank
    @Schema(example = "user@example.com", requiredMode = REQUIRED)
    public String email;

    @NotBlank
    @Schema(example = "my-secret-password")
    public String password;

    @Schema(example = "false", requiredMode = NOT_REQUIRED, defaultValue = "false")
    public boolean partner;

    @Schema(hidden = true)
    public MemberRole getRole() {
        return partner ? MemberRole.ROLE_MANAGER : MemberRole.ROLE_CUSTOMER;
    }

    @Schema(hidden = true)
    public String getRoleString() {
        return getRole().toString();
    }
}
