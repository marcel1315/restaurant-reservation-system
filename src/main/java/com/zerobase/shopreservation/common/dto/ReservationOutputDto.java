package com.zerobase.shopreservation.common.dto;

import com.zerobase.shopreservation.common.entity.Reservation;
import com.zerobase.shopreservation.common.type.ApprovalState;
import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@Builder
public class ReservationOutputDto {
    long id;
    ApprovalState approvalState;
    LocalDateTime checkInAt;
    LocalDateTime schedule;
    String phone;

    public static ReservationOutputDto of(Reservation reservation) {
        return ReservationOutputDto.builder()
                .id(reservation.getId())
                .schedule(reservation.getSchedule())
                .phone(reservation.getPhone())
                .approvalState(reservation.getApprovalState())
                .checkInAt(reservation.getCheckInAt())
                .build();
    }
}
