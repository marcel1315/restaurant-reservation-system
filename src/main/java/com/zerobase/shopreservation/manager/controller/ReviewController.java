package com.zerobase.shopreservation.manager.controller;

import com.zerobase.shopreservation.common.dto.ReviewOutputDto;
import com.zerobase.shopreservation.manager.service.ReviewService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

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
}
