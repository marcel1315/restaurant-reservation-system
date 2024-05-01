package com.zerobase.shopreservation.customer.service;

import com.zerobase.shopreservation.common.entity.Reservation;
import com.zerobase.shopreservation.common.entity.Review;
import com.zerobase.shopreservation.common.service.BaseService;
import com.zerobase.shopreservation.customer.dto.ReviewDto;
import com.zerobase.shopreservation.customer.exception.AlreadyDidReviewException;
import com.zerobase.shopreservation.customer.repository.ReservationRepository;
import com.zerobase.shopreservation.customer.repository.ReviewRepository;
import com.zerobase.shopreservation.manager.exception.ReservationNotExistException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class ReviewService extends BaseService {

    private final ReviewRepository reviewRepository;
    private final ReservationRepository reservationRepository;

    public void review(ReviewDto reviewDto) {
        Optional<Reservation> optionalReservation = reservationRepository.findByIdAndMember(
                reviewDto.getReservationId(),
                getCustomer()
        );

        if (optionalReservation.isEmpty()) {
            throw new ReservationNotExistException();
        }

        Optional<Review> optionalReview = reviewRepository.findByReservationId(optionalReservation.get().getId());
        if (optionalReview.isPresent()) {
            throw new AlreadyDidReviewException();
        }

        // TODO: 리뷰를 할 수 있는 상태인지 체크(= 예약시간 경과 && 체크인)

        Review review = Review.builder()
                .rate(reviewDto.getRate())
                .contents(reviewDto.getContents())
                .reservation(optionalReservation.get())
                .build();

        reviewRepository.save(review);
    }
}
