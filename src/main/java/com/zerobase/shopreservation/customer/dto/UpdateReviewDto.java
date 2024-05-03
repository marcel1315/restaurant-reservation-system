package com.zerobase.shopreservation.customer.dto;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class UpdateReviewDto {
    @Max(5)
    @Min(0)
    private Integer rate;

    private String contents;
}
