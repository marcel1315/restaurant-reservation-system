package com.zerobase.shopreservation.customer.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.Builder;
import lombok.Data;

import static io.swagger.v3.oas.annotations.media.Schema.RequiredMode.NOT_REQUIRED;
import static io.swagger.v3.oas.annotations.media.Schema.RequiredMode.REQUIRED;

@Data
@Builder
public class ReviewDto {
    @Positive
    @Schema(example = "1", requiredMode = REQUIRED)
    private long reservationId;

    @NotNull
    @Max(5)
    @Min(0)
    @Schema(example = "4", requiredMode = REQUIRED)
    private Integer rate;

    @Schema(example = "좋은 곳이었습니다", requiredMode = NOT_REQUIRED)
    private String contents;
}
