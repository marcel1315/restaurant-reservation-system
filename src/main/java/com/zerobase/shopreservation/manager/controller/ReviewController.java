package com.zerobase.shopreservation.manager.controller;

import com.zerobase.shopreservation.common.dto.ReviewOutputDto;
import com.zerobase.shopreservation.common.dto.ReviewOutputPageDto;
import com.zerobase.shopreservation.common.dto.ReviewsOfShopDto;
import com.zerobase.shopreservation.manager.service.ReviewService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController("managerReviewController")
@RequiredArgsConstructor
@Tag(name = "4 - Manager Review", description = "매니저 리뷰 관련")
public class ReviewController {

    private final ReviewService reviewService;

    @Operation(
            summary = "상점의 모든 리뷰를 보기",
            description = "Pagination 되어 있음"
    )
    @GetMapping("/manager/reviews")
    public ResponseEntity<?> listReviews(@ModelAttribute ReviewsOfShopDto dto) {
        ReviewOutputPageDto page = reviewService.listReviews(dto);
        return ResponseEntity.ok(page);
    }

    @Operation(
            summary = "특정 리뷰를 보기"
    )
    @GetMapping("/manager/reviews/{reviewId}")
    public ResponseEntity<?> detailReview(
            @PathVariable
            @Parameter(
                    schema = @Schema(defaultValue = "1")
            )
            long reviewId) {
        ReviewOutputDto detail = reviewService.detailReview(reviewId);
        return ResponseEntity.ok(detail);
    }

    @Operation(
            summary = "매니저가 자신의 상점의 특정 리뷰를 제거하기"
    )
    @DeleteMapping("/manager/reviews/{reviewId}")
    public ResponseEntity<?> removeReview(
          @PathVariable
          @Parameter(
                  schema = @Schema(defaultValue = "1")
          )
          long reviewId) {
        reviewService.removeReview(reviewId);
        return ResponseEntity.ok(null);
    }
}
