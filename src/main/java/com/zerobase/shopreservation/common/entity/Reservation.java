package com.zerobase.shopreservation.common.entity;

import com.zerobase.shopreservation.common.type.ApprovalState;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
public class Reservation extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    long id;
    LocalDateTime schedule;
    String phone;
    LocalDateTime checkInAt;

    @Enumerated(EnumType.STRING)
    ApprovalState approvalState;

    @ManyToOne
    @JoinColumn(name = "member_id")
    Member member;

    @ManyToOne
    @JoinColumn(name = "shop_id")
    @ToString.Exclude
    Shop shop;
}
