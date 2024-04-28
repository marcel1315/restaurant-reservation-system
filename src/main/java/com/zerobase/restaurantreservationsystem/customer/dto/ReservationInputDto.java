package com.zerobase.restaurantreservationsystem.customer.dto;

import com.zerobase.restaurantreservationsystem.common.entity.Reservation;
import com.zerobase.restaurantreservationsystem.common.type.ApprovalState;
import lombok.Data;

import java.time.LocalDateTime;

@Data
public class ReservationInputDto {
    public long restaurantId;
    public String schedule; // ISO8601
    public String phone;

    public Reservation toReservation() {
        return Reservation.builder()
                .schedule(LocalDateTime.parse(schedule))
                .phone(getPhone())
                .approvalState(ApprovalState.PENDING)
                .checkedIn(false)
                .build();
    }
}
