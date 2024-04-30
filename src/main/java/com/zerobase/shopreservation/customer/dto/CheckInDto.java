package com.zerobase.shopreservation.customer.dto;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class CheckInDto {
    private long reservationId;
    private LocalDateTime checkInTime;
}
