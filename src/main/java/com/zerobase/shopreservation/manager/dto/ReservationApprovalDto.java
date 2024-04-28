package com.zerobase.shopreservation.manager.dto;

import com.zerobase.shopreservation.common.type.ApprovalState;
import lombok.Data;

@Data
public class ReservationApprovalDto {
    public long shopId;
    public long reservationId;
    public ApprovalState approvalState;
}
