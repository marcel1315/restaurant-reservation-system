package com.zerobase.shopreservation.customer.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class UpdateReviewDto {
    @Max(5)
    @Min(0)
    @Schema(example = "3", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
    private Integer rate;

    @Schema(example = "좋은 곳이었습니다", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
    private String contents;
}
