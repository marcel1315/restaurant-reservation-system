package com.zerobase.shopreservation.common.dto;

import com.zerobase.shopreservation.common.type.ReservationSort;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.Builder;
import lombok.Data;

import java.time.LocalDate;

@Data
@Builder
public class ReservationsOfShopDto {
    @Positive
    private long shopId;

    // sorting
    @NotNull
    private ReservationSort sortBy;

    // search
    private LocalDate date;
    private String phone;
    private String customerId;

    // pagination
    @Positive
    private int pageSize;

    @Positive
    private int pageIndex;

    public int getPageStart() {
        return (pageIndex - 1) * pageSize;
    }
}
