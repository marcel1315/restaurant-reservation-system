package com.zerobase.shopreservation.customer.controller;

import com.zerobase.shopreservation.customer.dto.ReservationInputDto;
import com.zerobase.shopreservation.customer.dto.ReservationOutputDto;
import com.zerobase.shopreservation.customer.service.ReservationService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.Collections;
import java.util.List;

@Slf4j
@RestController("customerReservationController")
@RequiredArgsConstructor
public class ReservationController {

    private final ReservationService reservationService;

    @PostMapping("/customer/reservations")
    public ResponseEntity<?> reserve(@RequestBody ReservationInputDto reservationInputDto) {
        reservationService.reserve(reservationInputDto);
        return ResponseEntity.ok(Collections.singletonMap("success", true));
    }

    @GetMapping("/customer/reservations")
    public ResponseEntity<?> list() {
        List<ReservationOutputDto> list = reservationService.list();
        return ResponseEntity.ok(list);
    }
}
