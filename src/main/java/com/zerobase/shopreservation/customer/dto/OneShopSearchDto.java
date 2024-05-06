package com.zerobase.shopreservation.customer.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Positive;
import lombok.Builder;
import lombok.Data;

import static io.swagger.v3.oas.annotations.media.Schema.RequiredMode.NOT_REQUIRED;
import static io.swagger.v3.oas.annotations.media.Schema.RequiredMode.REQUIRED;

@Data
@Builder
public class OneShopSearchDto {

    @Positive
    @Schema(example = "1", requiredMode = REQUIRED)
    private long id;

    // location
    @Schema(example = "37.42333243", requiredMode = NOT_REQUIRED)
    private Double currentLatitude;
    @Schema(example = "127.21223433", requiredMode = NOT_REQUIRED)
    private Double currentLongitude;
}
