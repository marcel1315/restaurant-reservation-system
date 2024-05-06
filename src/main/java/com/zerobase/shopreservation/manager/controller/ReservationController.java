package com.zerobase.shopreservation.manager.controller;

import com.zerobase.shopreservation.common.dto.ReservationOutputPageDto;
import com.zerobase.shopreservation.common.dto.ReservationsOfShopDto;
import com.zerobase.shopreservation.manager.dto.CheckInDto;
import com.zerobase.shopreservation.manager.dto.ReservationApprovalDto;
import com.zerobase.shopreservation.manager.service.ReservationService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.Collections;

@RestController("managerReservationController")
@RequiredArgsConstructor
public class ReservationController {

    private final ReservationService reservationService;

    /**
     * 매니저가 상점에 달려있는 예약들을 불러오기
     * Pagination 되어 있음
     */
    @GetMapping("/manager/reservations")
    public ResponseEntity<?> list(@ModelAttribute ReservationsOfShopDto dto) {
        ReservationOutputPageDto page = reservationService.list(dto);
        return ResponseEntity.ok(page);
    }

    /**
     * 매니저가 예약에 대해 승인/거절하기
     */
    @PostMapping("/manager/reservations")
    public ResponseEntity<?> updateApproval(@Validated @RequestBody ReservationApprovalDto approval) {
        reservationService.updateApproval(approval);
        return ResponseEntity.ok(null);
    }

    /**
     * 키오스크에서 고객의 체크인을 도움
     *   키오스크는 매니저의 아이디로 로그인되어 있고, 상점정보가 저정되어있다고 가정함
     *   고객은 자신의 전화번호로 키오스크에서 예약을 검색함
     */
    @PostMapping("/manager/reservations/checkin")
    public ResponseEntity<?> checkIn(@Validated @RequestBody CheckInDto checkInDto) {
        long reservationId = reservationService.checkIn(checkInDto);
        return ResponseEntity.ok(Collections.singletonMap("reservationId", reservationId));
    }
}
