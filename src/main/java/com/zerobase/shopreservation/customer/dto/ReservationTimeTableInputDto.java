package com.zerobase.shopreservation.customer.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.Builder;
import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDate;

import static io.swagger.v3.oas.annotations.media.Schema.RequiredMode.REQUIRED;

@Data
@Builder
public class ReservationTimeTableInputDto {
    @Positive
    @Schema(example = "1", requiredMode = REQUIRED)
    long shopId;

    @NotNull
    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
    @Schema(example = "2024-05-06")
    LocalDate date;
}
