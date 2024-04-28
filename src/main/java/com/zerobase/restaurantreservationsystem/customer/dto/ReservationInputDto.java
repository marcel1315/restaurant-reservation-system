package com.zerobase.restaurantreservationsystem.customer.dto;

import com.zerobase.restaurantreservationsystem.common.entity.Reservation;
import lombok.Data;

import java.time.LocalDateTime;

@Data
public class ReservationInputDto {
    public long restaurantId;
    public String schedule; // ISO8601
    public int personCount;
    public String phone;

    public Reservation toReservation() {
        return Reservation.builder()
                .schedule(LocalDateTime.parse(schedule))
                .phone(getPhone())
                .personCount(getPersonCount())
                .approval("PENDING")
                .checkedIn(false)
                .build();
    }
}
