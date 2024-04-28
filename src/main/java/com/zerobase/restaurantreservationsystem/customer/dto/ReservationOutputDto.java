package com.zerobase.restaurantreservationsystem.customer.dto;

import com.zerobase.restaurantreservationsystem.common.entity.Reservation;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class ReservationOutputDto {
    public long restaurantId;
    public String schedule; // ISO8601
    public int personCount;
    public String phone;

    public static ReservationOutputDto of(Reservation reservation) {
        return ReservationOutputDto.builder()
                .schedule(reservation.getSchedule().toString())
                .phone(reservation.getPhone())
                .personCount(reservation.getPersonCount())
                .build();
    }
}
