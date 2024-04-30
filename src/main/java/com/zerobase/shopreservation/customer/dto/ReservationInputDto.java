package com.zerobase.shopreservation.customer.dto;

import com.zerobase.shopreservation.common.entity.Reservation;
import com.zerobase.shopreservation.common.type.ApprovalState;
import lombok.Data;

import java.time.LocalDateTime;

@Data
public class ReservationInputDto {
    public long shopId;
    public String schedule; // ISO8601
    public String phone;

    public Reservation toReservation() {
        return Reservation.builder()
                .schedule(LocalDateTime.parse(schedule))
                .phone(getPhone())
                .approvalState(ApprovalState.PENDING)
                .checkInAt(null)
                .build();
    }
}
