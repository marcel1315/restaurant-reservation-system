package com.zerobase.shopreservation.customer.service;

import com.zerobase.shopreservation.common.entity.Reservation;
import com.zerobase.shopreservation.common.entity.Review;
import com.zerobase.shopreservation.common.entity.Shop;
import com.zerobase.shopreservation.common.exception.ReservationNotExistException;
import com.zerobase.shopreservation.common.service.BaseService;
import com.zerobase.shopreservation.customer.dto.ReviewDto;
import com.zerobase.shopreservation.customer.dto.UpdateReviewDto;
import com.zerobase.shopreservation.customer.exception.*;
import com.zerobase.shopreservation.customer.repository.ReservationRepository;
import com.zerobase.shopreservation.customer.repository.ReviewRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class ReviewService extends BaseService {

    private final ReviewRepository reviewRepository;
    private final ReservationRepository reservationRepository;

    /**
     * 리뷰를 남김
     * 리뷰를 남기면서 reservation과 shop을 연결함
     */
    @Transactional
    public void review(ReviewDto reviewDto, LocalDateTime now) {
        // 예약인지 찾음(자신의 예약이 아니라면 찾아지지 않음)
        Optional<Reservation> optionalReservation = reservationRepository.findByIdAndMember(
                reviewDto.getReservationId(),
                getCustomer()
        );
        if (optionalReservation.isEmpty()) {
            throw new ReservationNotExistException();
        }
        Reservation reservation = optionalReservation.get();

        // 기존에 리뷰가 작성되었는지 검사
        Optional<Review> optionalReview = reviewRepository.findByReservationId(reservation.getId());
        if (optionalReview.isPresent()) {
            throw new AlreadyDidReviewException();
        }

        // checkIn을 했는지 검사
        if (reservation.getCheckInAt() == null) {
            throw new DidntCheckInException();
        }

        // 실제 예약 시간이 경과했는지 검사
        if (reservation.getSchedule().isAfter(now)) {
            throw new DidntUseReservationYetException();
        }

        Shop shop = reservation.getShop();
        Review review = Review.builder()
                .rate(reviewDto.getRate())
                .contents(reviewDto.getContents())
                .reservation(reservation)
                .shop(shop)
                .build();

        reviewRepository.save(review);
    }

    /**
     * 리뷰 보기
     */
    public List<Review> getReviews() {
        return new ArrayList<>();
    }

    /**
     * 자신이 작성했던 리뷰 내용을 업데이트함
     */
    @Transactional
    public void updateReview(long reviewId, UpdateReviewDto dto) {
        // 리뷰가 존재하는지 검사
        Optional<Review> optionalReview = reviewRepository.findById(reviewId);
        if (optionalReview.isEmpty()) {
            throw new ReviewNotExistException();
        }
        Review review = optionalReview.get();

        // 자신의 리뷰인지 검사
        if (getCustomer().getId() != review.getReservation().getMember().getId()) {
            throw new ReviewCustomerNotMatchException();
        }

        if (dto.getRate() != null) review.setRate(dto.getRate());
        if (dto.getContents() != null) review.setContents(dto.getContents());

        reviewRepository.save(review);
    }

    /**
     * 자신이 작성했던 리뷰를 제거함
     */
    @Transactional
    public void deleteReview(long reviewId) {
        // 리뷰가 존재하는지 검사
        Optional<Review> optionalReview = reviewRepository.findById(reviewId);
        if (optionalReview.isEmpty()) {
            throw new ReviewNotExistException();
        }
        Review review = optionalReview.get();

        // 자신의 리뷰인지 검사
        if (getCustomer().getId() != review.getReservation().getMember().getId()) {
            throw new ReviewCustomerNotMatchException();
        }
        reviewRepository.delete(review);
    }
}
