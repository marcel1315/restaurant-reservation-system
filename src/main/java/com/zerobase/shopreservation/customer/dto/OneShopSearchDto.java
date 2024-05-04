package com.zerobase.shopreservation.customer.dto;

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
