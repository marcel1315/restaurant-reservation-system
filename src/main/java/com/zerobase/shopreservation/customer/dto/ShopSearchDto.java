package com.zerobase.shopreservation.customer.dto;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class ShopSearchDto {
    public String name;
    public String address;
}
