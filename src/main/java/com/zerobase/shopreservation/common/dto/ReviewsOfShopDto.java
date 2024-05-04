package com.zerobase.shopreservation.common.dto;

import com.zerobase.shopreservation.common.type.ReviewSort;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class ReviewsOfShopDto {
    @Positive
    private long shopId;

    @NotNull
    private ReviewSort sortBy;

    // paging
    @Positive
    private int pageSize;

    @Positive
    private int pageIndex;

    public int getPageStart() {
        return (pageIndex - 1) * pageSize;
    }
}
