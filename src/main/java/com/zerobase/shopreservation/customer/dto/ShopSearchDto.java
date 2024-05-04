package com.zerobase.shopreservation.customer.dto;

import com.zerobase.shopreservation.common.type.ShopSort;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class ShopSearchDto {
    public String name;
    public String address;

    // paging
    @Positive
    public long pageSize;

    @Positive
    public long pageIndex;

    public long getPageStart() {
        return (pageIndex - 1) * pageSize;
    }

    // sorting
    @NotNull
    public ShopSort sortBy;

    // location
    public Double currentLatitude;
    public Double currentLongitude;
}
