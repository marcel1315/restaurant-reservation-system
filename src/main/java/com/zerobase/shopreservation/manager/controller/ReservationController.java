package com.zerobase.shopreservation.manager.controller;

import com.zerobase.shopreservation.common.dto.ReservationOutputDto;
import com.zerobase.shopreservation.manager.dto.ReservationApprovalDto;
import com.zerobase.shopreservation.manager.service.ReservationService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController("managerReservationController")
@RequiredArgsConstructor
public class ReservationController {

    private final ReservationService reservationService;

    @GetMapping("/manager/reservations")
    public ResponseEntity<?> list(@RequestParam Long shopId) {
        List<ReservationOutputDto> list = reservationService.list(shopId);
        return ResponseEntity.ok(list);
    }

    @PostMapping("/manager/reservations")
    public ResponseEntity<?> updateApproval(@Validated @RequestBody ReservationApprovalDto approval) {
        reservationService.updateApproval(approval);
        return ResponseEntity.ok(null);
    }
}
