package com.zerobase.shopreservation.customer.dto;

import lombok.Builder;
import lombok.Data;

import java.util.List;

@Data
@Builder
public class ShopOutputPageDto {
    private List<ShopOutputDto> shops;
    private long totalCount;
    private long totalPage;
    private long currentPage;
}
