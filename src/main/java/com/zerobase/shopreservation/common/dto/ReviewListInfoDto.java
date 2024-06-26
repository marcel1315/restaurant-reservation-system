package com.zerobase.shopreservation.common.dto;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class ReviewListInfoDto {
    private long reviewCount;
    private double reviewAverage;
}