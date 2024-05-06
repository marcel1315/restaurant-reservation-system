package com.zerobase.shopreservation.manager.dto;

import com.zerobase.shopreservation.common.type.ApprovalState;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.Builder;
import lombok.Data;

import static io.swagger.v3.oas.annotations.media.Schema.RequiredMode.REQUIRED;

@Data
@Builder
public class ReservationApprovalDto {
    @Positive
    @Schema(example = "1", requiredMode = REQUIRED)
    public long shopId;

    @Positive
    @Schema(example = "1", requiredMode = REQUIRED)
    public long reservationId;

    @NotNull(message = "approvalState field must be provided")
    @Schema(example = "ACCEPT", requiredMode = REQUIRED, allowableValues = {"ACCEPT, DENY"})
    public ApprovalState approvalState;
}
