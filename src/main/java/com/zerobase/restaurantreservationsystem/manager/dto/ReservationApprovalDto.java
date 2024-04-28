package com.zerobase.restaurantreservationsystem.manager.dto;

import lombok.Data;

@Data
public class ReservationApprovalDto {
    public long restaurantId;
    public long reservationId;
    public String approval;
}
