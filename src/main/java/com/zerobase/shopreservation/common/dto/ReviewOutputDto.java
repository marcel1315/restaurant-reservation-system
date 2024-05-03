package com.zerobase.shopreservation.common.dto;

import com.zerobase.shopreservation.common.entity.Review;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class ReviewOutputDto {
    long id;
    int rate;
    String contents;
    long reservationId;

    public static ReviewOutputDto of(Review review) {
        return ReviewOutputDto.builder()
                .id(review.getId())
                .rate(review.getRate())
                .contents(review.getContents())
                .reservationId(review.getReservation().getId())
                .build();
    }
}
