package com.zerobase.shopreservation.customer.controller;

import com.zerobase.shopreservation.common.dto.ReservationOutputDto;
import com.zerobase.shopreservation.customer.dto.ReservationInputDto;
import com.zerobase.shopreservation.customer.dto.ReservationTimeTableInputDto;
import com.zerobase.shopreservation.customer.dto.ReservationTimeTableOutputDto;
import com.zerobase.shopreservation.customer.exception.CantReservePastTimeException;
import com.zerobase.shopreservation.customer.service.ReservationService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;

@Slf4j
@RestController("customerReservationController")
@RequiredArgsConstructor
public class ReservationController {

    private final ReservationService reservationService;

    /**
     * 예약을 할 수 있는 예약시간을 받아오기
     * 상점마다 예약할 수 있는 시간이 미리 지정되어 있음(매니저가 상점을 생성하거나 업데이트하여 지정)
     * e.g., 12:00, 12:30, 13:00, ..
     */
    @GetMapping("/customer/reservations/timetable")
    public ResponseEntity<ReservationTimeTableOutputDto> timetable(@Validated @ModelAttribute ReservationTimeTableInputDto dto) {
        ReservationTimeTableOutputDto timeTable = reservationService.getReservationTimeTable(dto);
        return ResponseEntity.ok(timeTable);
    }

    /**
     * 예약하기
     */
    @PostMapping("/customer/reservations")
    public ResponseEntity<?> reserve(@Validated @RequestBody ReservationInputDto reservationInputDto) {
        // 시간이 과거인지 확인
        if (reservationInputDto.getSchedule().isBefore(LocalDateTime.now())) {
            throw new CantReservePastTimeException();
        }
        long reservationId = reservationService.reserve(reservationInputDto);

        return ResponseEntity.ok(Collections.singletonMap("reservationId", reservationId));
    }

    /**
     * 자신의 예약 정보 불러오기
     * 최근(24시간 전 이후)부터 미래의 예약들만 불러옴
     */
    @GetMapping("/customer/reservations")
    public ResponseEntity<?> list() {
        List<ReservationOutputDto> list = reservationService.list();
        return ResponseEntity.ok(list);
    }
}
