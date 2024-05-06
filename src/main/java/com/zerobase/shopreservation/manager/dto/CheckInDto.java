package com.zerobase.shopreservation.manager.dto;

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
public class CheckInDto {
    @Positive
    @Schema(example = "1", requiredMode = REQUIRED)
    private long reservationId;

    @NotNull
    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
    @Schema(example = "2024-05-06T12:55:00", defaultValue = "2024-05-06T12:55:00")
    private LocalDateTime now;
}
