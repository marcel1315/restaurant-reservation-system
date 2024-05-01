package com.zerobase.shopreservation.manager.dto;

import com.zerobase.shopreservation.common.type.ApprovalState;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class ReservationApprovalDto {
    @Positive
    public long shopId;

    @Positive
    public long reservationId;

    @NotNull(message = "approvalState field must be provided")
    public ApprovalState approvalState;
}
