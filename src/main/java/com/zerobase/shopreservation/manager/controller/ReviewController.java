package com.zerobase.shopreservation.manager.controller;

import com.zerobase.shopreservation.common.dto.ReviewOutputDto;
import com.zerobase.shopreservation.manager.service.ReviewService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController("managerReviewController")
@RequiredArgsConstructor
public class ReviewController {

    private final ReviewService reviewService;

    @GetMapping("/manager/reviews")
    public ResponseEntity<?> listReviews(@RequestParam long shopId) {
        List<ReviewOutputDto> list = reviewService.listReviews(shopId);
        return ResponseEntity.ok(list);
    }

    @GetMapping("/manager/reviews/{reviewId}")
    public ResponseEntity<?> detailReview(@PathVariable long reviewId) {
        ReviewOutputDto detail = reviewService.detailReview(reviewId);
        return ResponseEntity.ok(detail);
    }

    @DeleteMapping("/manager/reviews/{reviewId}")
    public ResponseEntity<?> removeReview(@PathVariable long reviewId) {
        reviewService.removeReview(reviewId);
        return ResponseEntity.ok(null);
    }
}
