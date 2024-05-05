package com.zerobase.shopreservation.manager.controller;

import com.zerobase.shopreservation.common.dto.ReservationOutputPageDto;
import com.zerobase.shopreservation.common.dto.ReservationsOfShopDto;
import com.zerobase.shopreservation.manager.dto.ReservationApprovalDto;
import com.zerobase.shopreservation.manager.service.ReservationService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@RestController("managerReservationController")
@RequiredArgsConstructor
public class ReservationController {

    private final ReservationService reservationService;

    @GetMapping("/manager/reservations")
    public ResponseEntity<?> list(@ModelAttribute ReservationsOfShopDto dto) {
        ReservationOutputPageDto page = reservationService.list(dto);
        return ResponseEntity.ok(page);
    }

    @PostMapping("/manager/reservations")
    public ResponseEntity<?> updateApproval(@Validated @RequestBody ReservationApprovalDto approval) {
        reservationService.updateApproval(approval);
        return ResponseEntity.ok(null);
    }
}
