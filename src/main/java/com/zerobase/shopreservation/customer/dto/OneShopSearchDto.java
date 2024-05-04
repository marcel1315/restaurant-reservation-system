package com.zerobase.shopreservation.customer.dto;

import com.zerobase.shopreservation.common.type.ShopSort;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class OneShopSearchDto {
    private long id;

    // location
    private Double currentLatitude;
    private Double currentLongitude;
}
