package com.zerobase.restaurantreservationsystem.manager.dto;

import com.zerobase.restaurantreservationsystem.common.entity.Reservation;
import com.zerobase.restaurantreservationsystem.common.type.ApprovalState;
import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@Builder
public class ReservationDto {
    LocalDateTime schedule;
    String phone;
    ApprovalState approvalState;
    boolean checkedIn;

    public static ReservationDto of(Reservation reservation) {
        return ReservationDto.builder()
                .schedule(reservation.getSchedule())
                .phone(reservation.getPhone())
                .approvalState(reservation.getApprovalState())
                .checkedIn(reservation.isCheckedIn())
                .build();
    }
}
