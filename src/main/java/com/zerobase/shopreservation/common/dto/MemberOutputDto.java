package com.zerobase.shopreservation.common.dto;

import com.zerobase.shopreservation.common.entity.Member;
import com.zerobase.shopreservation.common.type.MemberRole;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class MemberOutputDto {
    long id;
    String email;
    String name;
    String phone;
    MemberRole role;

    public static MemberOutputDto of(Member member) {
        return MemberOutputDto.builder()
                .id(member.getId())
                .name(member.getName())
                .email(member.getEmail())
                .phone(member.getPhone())
                .role(member.getRole())
                .build();
    }
}
