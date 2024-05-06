package com.zerobase.shopreservation.common.dto;

import com.zerobase.shopreservation.common.type.ReviewSort;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.Builder;
import lombok.Data;

import static io.swagger.v3.oas.annotations.media.Schema.RequiredMode.REQUIRED;

@Data
@Builder
public class ReviewsOfShopDto {
    @Positive
    @Schema(example = "1", requiredMode = REQUIRED)
    private long shopId;

    @NotNull
    @Schema(example = "RATING", requiredMode = REQUIRED, allowableValues = {"RATING", "RECENT"})
    private ReviewSort sortBy;

    // pagination
    @Positive
    @Schema(example = "10", requiredMode = REQUIRED)
    private int pageSize;

    @Positive
    @Schema(example = "1", requiredMode = REQUIRED)
    private int pageIndex;

    @Schema(hidden = true)
    public int getPageStart() {
        return (pageIndex - 1) * pageSize;
    }
}
