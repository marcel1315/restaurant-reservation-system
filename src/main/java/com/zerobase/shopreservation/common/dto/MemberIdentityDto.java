package com.zerobase.shopreservation.common.dto;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class MemberIdentityDto {
    public String username;
    public String role;
}
