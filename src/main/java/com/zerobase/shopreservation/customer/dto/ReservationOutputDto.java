package com.zerobase.shopreservation.customer.dto;

import com.zerobase.shopreservation.common.entity.Reservation;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class ReservationOutputDto {
    public String schedule; // ISO8601
    public String phone;

    public static ReservationOutputDto of(Reservation reservation) {
        return ReservationOutputDto.builder()
                .schedule(reservation.getSchedule().toString())
                .phone(reservation.getPhone())
                .build();
    }
}
