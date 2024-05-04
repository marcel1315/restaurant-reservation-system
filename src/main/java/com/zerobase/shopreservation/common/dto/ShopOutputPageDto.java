package com.zerobase.shopreservation.common.dto;

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
