package com.zerobase.restaurantreservationsystem.manager.dto;

import com.zerobase.restaurantreservationsystem.common.entity.Reservation;
import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@Builder
public class ReservationDto {
    LocalDateTime schedule;
    String phone;
    String approval;
    boolean checkedIn;

    public static ReservationDto of(Reservation reservation) {
        return ReservationDto.builder()
                .schedule(reservation.getSchedule())
                .phone(reservation.getPhone())
                .approval(reservation.getApproval())
                .checkedIn(reservation.isCheckedIn())
                .build();
    }
}
