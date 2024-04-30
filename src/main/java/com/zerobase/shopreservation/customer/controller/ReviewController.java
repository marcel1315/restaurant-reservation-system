package com.zerobase.shopreservation.customer.controller;

import com.zerobase.shopreservation.customer.dto.ReviewDto;
import com.zerobase.shopreservation.customer.service.ReviewService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.Collections;

@RestController("customerReviewController")
@RequiredArgsConstructor
public class ReviewController {

    private final ReviewService reviewService;

    @PostMapping("/customer/review")
    public ResponseEntity<?> review(@RequestBody ReviewDto reviewDto) {
        reviewService.review(reviewDto);
        return ResponseEntity.ok(Collections.singletonMap("success", true));
    }
}
