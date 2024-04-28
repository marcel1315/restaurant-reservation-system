package com.zerobase.restaurantreservationsystem.common.entity;

import com.zerobase.restaurantreservationsystem.common.type.MemberRole;
import jakarta.persistence.*;
import lombok.*;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Entity
@Builder
public class Member {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    long id;
    String email;
    String password;
    String name;
    String phone;

    @Enumerated(EnumType.STRING)
    MemberRole role;
}
