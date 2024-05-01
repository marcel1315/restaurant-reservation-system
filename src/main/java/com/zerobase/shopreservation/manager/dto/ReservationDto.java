package com.zerobase.shopreservation.manager.dto;

import com.zerobase.shopreservation.common.entity.Reservation;
import com.zerobase.shopreservation.common.type.ApprovalState;
import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@Builder
public class ReservationDto {
    long id;
    LocalDateTime schedule;
    String phone;
    ApprovalState approvalState;
    LocalDateTime checkInAt;

    public static ReservationDto of(Reservation reservation) {
        return ReservationDto.builder()
                .id(reservation.getId())
                .schedule(reservation.getSchedule())
                .phone(reservation.getPhone())
                .approvalState(reservation.getApprovalState())
                .checkInAt(reservation.getCheckInAt())
                .build();
    }
}
