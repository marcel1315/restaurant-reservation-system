package com.zerobase.shopreservation.customer.dto;

import com.zerobase.shopreservation.common.type.ShopSort;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.Builder;
import lombok.Data;

import static io.swagger.v3.oas.annotations.media.Schema.RequiredMode.NOT_REQUIRED;
import static io.swagger.v3.oas.annotations.media.Schema.RequiredMode.REQUIRED;

@Data
@Builder
public class ShopSearchDto {
    @Schema(example = "칼국수", requiredMode = NOT_REQUIRED)
    public String name;
    @Schema(example = "강남", requiredMode = NOT_REQUIRED)
    public String address;

    // paging
    @Positive
    @Schema(example = "10", requiredMode = REQUIRED)
    public long pageSize;

    @Positive
    @Schema(example = "1", requiredMode = REQUIRED)
    public long pageIndex;

    @Schema(hidden = true)
    public long getPageStart() {
        return (pageIndex - 1) * pageSize;
    }

    // sorting
    @NotNull
    @Schema(example = "DISTANCE", requiredMode = REQUIRED, allowableValues = {"NAME", "RATING", "DISTANCE"})
    public ShopSort sortBy;

    // location
    @Schema(example = "37.42333243", requiredMode = NOT_REQUIRED)
    public Double currentLatitude;
    @Schema(example = "127.21223433", requiredMode = NOT_REQUIRED)
    public Double currentLongitude;
}
