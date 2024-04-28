package com.zerobase.restaurantreservationsystem.manager.dto;

import com.zerobase.restaurantreservationsystem.common.type.ApprovalState;
import lombok.Data;

@Data
public class ReservationApprovalDto {
    public long restaurantId;
    public long reservationId;
    public ApprovalState approvalState;
}
