package com.zerobase.shopreservation.customer.controller;

import com.zerobase.shopreservation.common.dto.ReservationOutputDto;
import com.zerobase.shopreservation.customer.dto.CheckInDto;
import com.zerobase.shopreservation.customer.dto.ReservationInputDto;
import com.zerobase.shopreservation.customer.service.ReservationService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;

@Slf4j
@RestController("customerReservationController")
@RequiredArgsConstructor
public class ReservationController {

    private final ReservationService reservationService;

    @PostMapping("/customer/reservations")
    public ResponseEntity<?> reserve(@Validated @RequestBody ReservationInputDto reservationInputDto) {
        long reservationId = reservationService.reserve(reservationInputDto);
        return ResponseEntity.ok(Collections.singletonMap("reservationId", reservationId));
    }

    @GetMapping("/customer/reservations")
    public ResponseEntity<?> list() {
        List<ReservationOutputDto> list = reservationService.list();
        return ResponseEntity.ok(list);
    }

    @PostMapping("/customer/reservations/checkin")
    public ResponseEntity<?> checkIn(@RequestBody CheckInDto checkInDto) {
        if (checkInDto.getCheckInTime() == null) {
            checkInDto.setCheckInTime(LocalDateTime.now());
        }
        // 체크인 할 수 있는 상태인지 체크 (= 예약시간 10분전이 지났는지 체크)

        long reservationId = reservationService.checkIn(checkInDto);
        return ResponseEntity.ok(Collections.singletonMap("reservationId", reservationId));
    }
}
