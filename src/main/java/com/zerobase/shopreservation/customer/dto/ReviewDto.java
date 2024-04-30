package com.zerobase.shopreservation.customer.dto;

import lombok.Data;

@Data
public class ReviewDto {
    private long reservationId;
    private int rate;
    private String contents;
}
