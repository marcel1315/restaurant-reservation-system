package com.zerobase.shopreservation.common.dto;

import lombok.Builder;
import lombok.Data;

import java.util.List;

@Data
@Builder
public class ReviewOutputPageDto {
    private List<ReviewOutputDto> reviews;
    private double reviewAverage;
    private long totalCount;
    private long totalPage;
    private long currentPage;
}
