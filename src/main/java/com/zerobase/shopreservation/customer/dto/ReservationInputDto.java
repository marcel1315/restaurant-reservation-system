package com.zerobase.shopreservation.customer.dto;

import com.zerobase.shopreservation.common.entity.Reservation;
import com.zerobase.shopreservation.common.type.ApprovalState;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.Builder;
import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDateTime;

import static io.swagger.v3.oas.annotations.media.Schema.RequiredMode.REQUIRED;

@Data
@Builder
public class ReservationInputDto {
    @Positive
    @Schema(example = "1", requiredMode = REQUIRED)
    public long shopId;

    @NotNull
    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
    @Schema(example = "2024-05-06T13:00:00", defaultValue = "2024-05-06T13:00:00")
    public LocalDateTime schedule;

    @NotNull
    @Schema(example = "010-2222-3333")
    public String phone;

    public Reservation toReservation() {
        return Reservation.builder()
                .schedule(schedule)
                .phone(getPhone())
                .approvalState(ApprovalState.PENDING)
                .checkInAt(null)
                .build();
    }
}
