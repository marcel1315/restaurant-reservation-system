package com.zerobase.shopreservation.customer.dto;

import com.zerobase.shopreservation.common.entity.Reservation;
import com.zerobase.shopreservation.common.type.ApprovalState;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.Builder;
import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDateTime;

@Data
@Builder
public class ReservationInputDto {
    @Positive
    public long shopId;

    @NotNull
    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
    public LocalDateTime schedule;

    @NotNull
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
